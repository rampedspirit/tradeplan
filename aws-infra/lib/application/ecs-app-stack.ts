import { CfnOutput, Fn, RemovalPolicy, Stack, StackProps } from "aws-cdk-lib";
import { InstanceType, IVpc, Peer, Port, SecurityGroup, Vpc } from "aws-cdk-lib/aws-ec2";
import { Repository } from "aws-cdk-lib/aws-ecr";
import { Cluster, ContainerDependencyCondition, ContainerImage, Ec2Service, Ec2TaskDefinition, EcsOptimizedImage, LogDriver, NetworkMode } from "aws-cdk-lib/aws-ecs";
import { ApplicationListener, ApplicationListenerRule, ApplicationLoadBalancer, ApplicationProtocol, ApplicationTargetGroup, ListenerAction, ListenerCondition, NetworkLoadBalancer, Protocol, TargetType } from "aws-cdk-lib/aws-elasticloadbalancingv2";
import { LogGroup } from "aws-cdk-lib/aws-logs";
import { ISecret, Secret } from "aws-cdk-lib/aws-secretsmanager";
import { Construct } from "constructs";

export interface EcsAppStackProps extends StackProps {
    vpcName: string
    dbLoadBalancerDnsExportName: string
}
export class EcsAppStack extends Stack {

    constructor(scope: Construct, id: string, props: EcsAppStackProps) {
        super(scope, id, props);

        //Find VPC
        const vpc: IVpc = Vpc.fromLookup(this, props.vpcName, {
            vpcName: props.vpcName,
            region: 'ap-south-1'
        });

        //Find DB Load Balancer
        const dbLoadBalancerUrl = Fn.importValue(props.dbLoadBalancerDnsExportName);

        // Secrets
        const dbCredentials = Secret.fromSecretCompleteArn(this, 'DbCredentials', 'arn:aws:secretsmanager:ap-south-1:838293343811:secret:prod/db/credentials-vquhXO');

        //Log Group
        const logGroup = new LogGroup(this, props.stackName + "-app-loggroup", {
            logGroupName: props.stackName + "-app-loggroup",
            removalPolicy: RemovalPolicy.DESTROY
        });

        //Cluster
        const cluster: Cluster = this.createCluster(props.stackName!, vpc);

        //Application Load Balancer
        const applicationLoadbalancer = this.createApplicationLoadBalancer(props.stackName!, vpc);
        const kafkaBootstrapUrl = applicationLoadbalancer.loadBalancerDnsName + ":" + 19092;

        //Export load balancer dns
        new CfnOutput(this, "dbLoadBalancerDnsName", {
            value: applicationLoadbalancer.loadBalancerDnsName,
            exportName: props.stackName + "-alb-dns",
        });

        //Kafka
        this.createKafkaService(props.stackName!, vpc, logGroup, applicationLoadbalancer, cluster, kafkaBootstrapUrl);

        //Services
        const applicationListener = applicationLoadbalancer.addListener(props.stackName + "application-listener", {
            protocol: ApplicationProtocol.HTTP,
            port: 80,
            defaultAction: ListenerAction.fixedResponse(200, {
                contentType: "text/plain",
                messageBody: "Hello There! Looks like you have hit a wrong end point."
            })
        });
        this.createFilterService(props.stackName!, vpc, logGroup, applicationListener, cluster, dbCredentials, dbLoadBalancerUrl);
        this.createConditionService(props.stackName!, vpc, logGroup, applicationListener, cluster, dbCredentials, dbLoadBalancerUrl);
        this.createScreenerService(props.stackName!, vpc, logGroup, applicationListener, cluster, dbCredentials, dbLoadBalancerUrl, kafkaBootstrapUrl);
    }

    /**
     * Creates the Database Cluster
     * @param stackName 
     */
    private createCluster(stackName: string, vpc: IVpc): Cluster {
        const cluster = new Cluster(this, stackName + "-ApplicationCluster", {
            clusterName: stackName + "-ApplicationCluster",
            vpc: vpc
        });

        //Auto scaling group (EC2 Instance)
        const autoScalingGroup = cluster.addCapacity(stackName + "-ApplicationCluster-AutoScalingGroup", {
            instanceType: new InstanceType('t3a.medium'),
            machineImage: EcsOptimizedImage.amazonLinux(),
            desiredCapacity: 1,
            minCapacity: 1,
            maxCapacity: 1,
            vpcSubnets: {
                subnets: vpc.privateSubnets
            }
        });

        //Security group for ingress form ALB
        const securityGroup = new SecurityGroup(this, stackName + "-SecurityGroup", {
            securityGroupName: stackName + "-SecurityGroup",
            vpc: vpc
        });
        securityGroup.addIngressRule(Peer.anyIpv4(), Port.tcpRange(31000, 61000));
        autoScalingGroup.addSecurityGroup(securityGroup);

        return cluster;
    }

    /**
     * Creates ApplicationLoadBalancer
     * @param stackName
     * @param vpc 
     * @returns 
     */
    private createApplicationLoadBalancer(stackName: string, vpc: IVpc): ApplicationLoadBalancer {
        let loadBalancer = new ApplicationLoadBalancer(this, "alb", {
            vpc: vpc,
            internetFacing: true,
            loadBalancerName: stackName + "-alb",
            vpcSubnets: {
                subnets: vpc.publicSubnets
            }
        });

        //Security group for ingress form ALB
        const securityGroup = new SecurityGroup(this, stackName + "-ALB-SecurityGroup", {
            securityGroupName: stackName + "-ALB-SecurityGroup",
            vpc: vpc
        });
        securityGroup.addIngressRule(Peer.anyIpv4(), Port.tcp(19092));
        loadBalancer.addSecurityGroup(securityGroup);

        return loadBalancer;
    }

    /**
     * Creates the Kafka service
     * @param stackName 
     * @param vpc 
     * @param logGroup 
     * @param applicationLoadbalancer 
     * @param cluster 
     * @param dbCredentials 
     * @param dbLoadBalancerUrl
     */
    private createKafkaService(stackName: string, vpc: IVpc, logGroup: LogGroup,
        applicationLoadbalancer: ApplicationLoadBalancer, cluster: Cluster, kafkaBootstrapUrl: string) {

        //Load Balancer Config
        let targetGroup = new ApplicationTargetGroup(this, stackName + "-kafka-target-group", {
            vpc: vpc,
            targetType: TargetType.IP,
            port: 19092,
            protocol: ApplicationProtocol.HTTP,
            healthCheck: {
                port: "9093",
                path: "/actuator/health"
            }
        });

        applicationLoadbalancer.addListener(stackName + "kafka-listener", {
            protocol: ApplicationProtocol.HTTP,
            port: 19092,
            defaultAction: ListenerAction.forward([targetGroup])
        });

        //Service Config
        let taskDefinition = new Ec2TaskDefinition(this, stackName + '-kafka-taskdef', {
            networkMode: NetworkMode.HOST
        });

        const zookeeperContainerDefinition = taskDefinition.addContainer(stackName + "-zookeeper-container", {
            image: ContainerImage.fromRegistry("zookeeper:latest"),
            cpu: 50,
            memoryLimitMiB: 500,
            essential: true,
            environment: {
                "ZOOKEEPER_CLIENT_PORT": "2181"
            },
            portMappings: [{
                hostPort: 2181,
                containerPort: 2181
            }],
            logging: LogDriver.awsLogs({
                logGroup: logGroup,
                streamPrefix: logGroup.logGroupName
            }),
        });

        const kafkaContainerDefinition = taskDefinition.addContainer(stackName + "-kafka-container", {
            image: ContainerImage.fromRegistry("confluentinc/cp-kafka:7.0.1"),
            cpu: 50,
            memoryLimitMiB: 1024,
            essential: true,
            environment: {
                "KAFKA_BROKER_ID": "1",
                "KAFKA_ZOOKEEPER_CONNECT": "localhost:2181",
                "KAFKA_LISTENERS": "INTERNAL://:9092,EXTERNAL://:19092",
                "KAFKA_ADVERTISED_LISTENERS": "INTERNAL://localhost:9092,EXTERNAL://" + kafkaBootstrapUrl,
                "KAFKA_LISTENER_SECURITY_PROTOCOL_MAP": "INTERNAL:PLAINTEXT,EXTERNAL:PLAINTEXT",
                "KAFKA_INTER_BROKER_LISTENER_NAME":"INTERNAL"
            },
            portMappings: [{
                hostPort: 19092,
                containerPort: 19092
            }],
            logging: LogDriver.awsLogs({
                logGroup: logGroup,
                streamPrefix: logGroup.logGroupName
            })
        });

        kafkaContainerDefinition.addContainerDependencies({
            container: zookeeperContainerDefinition,
            condition: ContainerDependencyCondition.START
        });

        const kafkaMonitorContainerDefinition = taskDefinition.addContainer(stackName + "-kafka-monitor-service-container", {
            image: ContainerImage.fromEcrRepository(Repository.fromRepositoryName(this, "gtk-kafka-monitor-service", "gtk-kafka-monitor-service")),
            cpu: 50,
            memoryLimitMiB: 256,
            essential: true,
            environment: {
                "SERVER_PORT": "9093",
                "KAFKA_BOOTSTRAP_ADDRESS": "localhost:9092"
            },
            portMappings: [{
                hostPort: 9093,
                containerPort: 9093
            }],
            logging: LogDriver.awsLogs({
                logGroup: logGroup,
                streamPrefix: logGroup.logGroupName
            })
        });

        kafkaMonitorContainerDefinition.addContainerDependencies({
            container: kafkaContainerDefinition,
            condition: ContainerDependencyCondition.START
        });

        taskDefinition.defaultContainer = kafkaContainerDefinition;

        let service = new Ec2Service(this, stackName + "-kafka-service", {
            cluster: cluster,
            desiredCount: 1,
            taskDefinition: taskDefinition
        });
        service.attachToApplicationTargetGroup(targetGroup);
    }

    /**
     * Creates the filter service
     * @param stackName 
     * @param vpc 
     * @param logGroup 
     * @param applicationLoadbalancer 
     * @param cluster 
     * @param dbCredentials 
     * @param dbLoadBalancerUrl
     */
    private createFilterService(stackName: string, vpc: IVpc, logGroup: LogGroup,
        applicationListener: ApplicationListener, cluster: Cluster, dbCredentials: ISecret, dbLoadBalancerUrl: string) {

        //Load Balancer Config
        let targetGroup = new ApplicationTargetGroup(this, stackName + "-filter-service-target-group", {
            vpc: vpc,
            port: 5000,
            protocol: ApplicationProtocol.HTTP
        });

        new ApplicationListenerRule(this, "filterservice-listener-rule", {
            listener: applicationListener,
            priority: 1,
            conditions: [
                ListenerCondition.pathPatterns(["/v1/filter", "/v1/filter/*"])
            ],
            action: ListenerAction.forward([targetGroup])
        });

        //Service Config
        let taskDefinition = new Ec2TaskDefinition(this, stackName + '-filter-service-taskdef');

        taskDefinition.addContainer(stackName + "-filter-service-container", {
            image: ContainerImage.fromEcrRepository(Repository.fromRepositoryName(this, "gtk-filter-service", "gtk-filter-service")),
            cpu: 50,
            memoryLimitMiB: 256,
            essential: true,
            environment: {
                "SERVER_PORT": "5000",
                "DB_HOST": dbLoadBalancerUrl,
                "DB_PORT": "5000",
                "DB_NAME": "appdb",
                "DB_USER_NAME": dbCredentials.secretValueFromJson("UserName").toString(),
                "DB_PASSWORD": dbCredentials.secretValueFromJson("Password").toString()
            },
            portMappings: [{
                containerPort: 5000
            }],
            logging: LogDriver.awsLogs({
                logGroup: logGroup,
                streamPrefix: logGroup.logGroupName
            }),
        });

        let service = new Ec2Service(this, stackName + "-filter-service", {
            cluster: cluster,
            desiredCount: 1,
            taskDefinition: taskDefinition
        });
        service.attachToApplicationTargetGroup(targetGroup);
    }

    /**
     * Creates the condition service
     * @param stackName 
     * @param vpc 
     * @param logGroup 
     * @param applicationLoadbalancer 
     * @param cluster 
     * @param dbCredentials 
     * @param dbLoadBalancerUrl
     */
    private createConditionService(stackName: string, vpc: IVpc, logGroup: LogGroup,
        applicationListener: ApplicationListener, cluster: Cluster, dbCredentials: ISecret, dbLoadBalancerUrl: string) {

        //Load Balancer Config
        let targetGroup = new ApplicationTargetGroup(this, stackName + "-condition-service-target-group", {
            vpc: vpc,
            port: 5001,
            protocol: ApplicationProtocol.HTTP
        });

        new ApplicationListenerRule(this, "conditionservice-listener-rule", {
            listener: applicationListener,
            priority: 2,
            conditions: [
                ListenerCondition.pathPatterns(["/v1/condition", "/v1/condition/*"])
            ],
            action: ListenerAction.forward([targetGroup])
        });

        //Service Config
        let taskDefinition = new Ec2TaskDefinition(this, stackName + '-condition-service-taskdef');

        taskDefinition.addContainer(stackName + "-condition-service-container", {
            image: ContainerImage.fromEcrRepository(Repository.fromRepositoryName(this, "gtk-condition-service", "gtk-condition-service")),
            cpu: 50,
            memoryLimitMiB: 256,
            essential: true,
            environment: {
                "SERVER_PORT": "5001",
                "DB_HOST": dbLoadBalancerUrl,
                "DB_PORT": "5000",
                "DB_NAME": "appdb",
                "DB_USER_NAME": dbCredentials.secretValueFromJson("UserName").toString(),
                "DB_PASSWORD": dbCredentials.secretValueFromJson("Password").toString()
            },
            portMappings: [{
                containerPort: 5001
            }],
            logging: LogDriver.awsLogs({
                logGroup: logGroup,
                streamPrefix: logGroup.logGroupName
            }),
        });

        let service = new Ec2Service(this, stackName + "-condition-service", {
            cluster: cluster,
            desiredCount: 1,
            taskDefinition: taskDefinition
        });
        service.attachToApplicationTargetGroup(targetGroup);
    }

    /**
     * Creates the screener service
     * @param stackName 
     * @param vpc 
     * @param logGroup 
     * @param applicationLoadbalancer 
     * @param cluster 
     * @param dbCredentials 
     * @param dbLoadBalancerUrl
     * @param kafkaBootstrapUrl
     */
    private createScreenerService(stackName: string, vpc: IVpc, logGroup: LogGroup, applicationListener: ApplicationListener,
        cluster: Cluster, dbCredentials: ISecret, dbLoadBalancerUrl: string, kafkaBootstrapUrl: string) {

        //Load Balancer Config
        let targetGroup = new ApplicationTargetGroup(this, stackName + "-screener-service-target-group", {
            vpc: vpc,
            port: 5002,
            protocol: ApplicationProtocol.HTTP,
            healthCheck: {
                path: "/actuator/health"
            }
        });

        new ApplicationListenerRule(this, "screenerservice-listener-rule", {
            listener: applicationListener,
            priority: 3,
            conditions: [
                ListenerCondition.pathPatterns(["/v1/screener", "/v1/screener/*"])
            ],
            action: ListenerAction.forward([targetGroup])
        });

        //Service Config
        let taskDefinition = new Ec2TaskDefinition(this, stackName + '-screener-service-taskdef');

        taskDefinition.addContainer(stackName + "-screener-service-container", {
            image: ContainerImage.fromEcrRepository(Repository.fromRepositoryName(this, "gtk-screener-service", "gtk-screener-service")),
            cpu: 50,
            memoryLimitMiB: 256,
            essential: true,
            environment: {
                "SERVER_PORT": "5002",
                "DB_HOST": dbLoadBalancerUrl,
                "DB_PORT": "5000",
                "DB_NAME": "appdb",
                "DB_USER_NAME": dbCredentials.secretValueFromJson("UserName").toString(),
                "DB_PASSWORD": dbCredentials.secretValueFromJson("Password").toString(),
                "KAFKA_BOOTSTRAP_ADDRESS": kafkaBootstrapUrl
            },
            portMappings: [{
                containerPort: 5002
            }],
            logging: LogDriver.awsLogs({
                logGroup: logGroup,
                streamPrefix: logGroup.logGroupName
            }),
        });

        let service = new Ec2Service(this, stackName + "-screener-service", {
            cluster: cluster,
            desiredCount: 1,
            taskDefinition: taskDefinition
        });
        service.attachToApplicationTargetGroup(targetGroup);
    }
}