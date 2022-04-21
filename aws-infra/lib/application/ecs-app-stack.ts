import { CfnOutput, Fn, RemovalPolicy, Stack, StackProps } from "aws-cdk-lib";
import { InstanceType, IVpc, Peer, Port, SecurityGroup, Vpc } from "aws-cdk-lib/aws-ec2";
import { Repository } from "aws-cdk-lib/aws-ecr";
import { Cluster, ContainerDependencyCondition, ContainerImage, Ec2Service, Ec2TaskDefinition, EcsOptimizedImage, LogDriver, NetworkMode } from "aws-cdk-lib/aws-ecs";
import { ApplicationListener, ApplicationListenerRule, ApplicationLoadBalancer, ApplicationProtocol, ApplicationTargetGroup, ListenerAction, ListenerCondition, NetworkLoadBalancer, Protocol, TargetType } from "aws-cdk-lib/aws-elasticloadbalancingv2";
import { LogGroup } from "aws-cdk-lib/aws-logs";
import { ISecret, Secret } from "aws-cdk-lib/aws-secretsmanager";
import { DnsRecordType, NamespaceType } from "aws-cdk-lib/aws-servicediscovery";
import { Construct } from "constructs";

export interface EcsAppStackProps extends StackProps {
    vpcName: string
    imageTag: string
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

        //Export load balancer dns
        new CfnOutput(this, "appLoadBalancerDnsName", {
            value: applicationLoadbalancer.loadBalancerDnsName,
            exportName: props.stackName + "-alb-dns",
        });

        //Kafka
        this.createKafkaService(props.stackName!, vpc, logGroup, cluster);

        //Services
        const applicationListener = applicationLoadbalancer.addListener(props.stackName + "application-listener", {
            protocol: ApplicationProtocol.HTTP,
            port: 80,
            defaultAction: ListenerAction.fixedResponse(200, {
                contentType: "text/plain",
                messageBody: "Hello There! Looks like you have hit a wrong end point."
            })
        });
        this.createFilterService(props.stackName!, props.imageTag, vpc, logGroup, applicationListener, cluster, dbCredentials, dbLoadBalancerUrl);
        this.createConditionService(props.stackName!, props.imageTag, vpc, logGroup, applicationListener, cluster, dbCredentials, dbLoadBalancerUrl);
        this.createScreenerService(props.stackName!, props.imageTag, vpc, logGroup, applicationListener, cluster, dbCredentials, dbLoadBalancerUrl);
        this.createExpressionService(props.stackName!, props.imageTag, vpc, logGroup, applicationListener, cluster, dbCredentials, dbLoadBalancerUrl);
        this.createWatchlistService(props.stackName!, props.imageTag, vpc, logGroup, applicationListener, cluster, dbCredentials, dbLoadBalancerUrl);
        this.createStockService(props.stackName!, props.imageTag, vpc, logGroup, applicationListener, cluster, dbCredentials, dbLoadBalancerUrl);
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
                subnets: vpc.publicSubnets
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
        return loadBalancer;
    }

    /**
     * Creates the Kafka service
     * @param stackName 
     * @param vpc 
     * @param logGroup 
     * @param cluster 
     */
    private createKafkaService(stackName: string, vpc: IVpc, logGroup: LogGroup, cluster: Cluster) {

        let taskDefinition = new Ec2TaskDefinition(this, stackName + '-kafka-taskdef', {
            networkMode: NetworkMode.AWS_VPC
        });

        const zookeeperContainerDefinition = taskDefinition.addContainer(stackName + "-zookeeper-container", {
            image: ContainerImage.fromRegistry("zookeeper:latest"),
            cpu: 50,
            memoryLimitMiB: 500,
            essential: false,
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
                "KAFKA_ADVERTISED_LISTENERS": "INTERNAL://localhost:9092,EXTERNAL://kafka.gtk.com:19092",
                "KAFKA_LISTENER_SECURITY_PROTOCOL_MAP": "INTERNAL:PLAINTEXT,EXTERNAL:PLAINTEXT",
                "KAFKA_INTER_BROKER_LISTENER_NAME": "INTERNAL",
                "KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR": "1"
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

        let namespace = "gtk.com";
        const dnsNamespace = cluster.addDefaultCloudMapNamespace({ vpc, name: namespace, type: NamespaceType.DNS_PRIVATE });

        const securityGroup = new SecurityGroup(this, stackName + "-ALB-SecurityGroup", {
            securityGroupName: stackName + "-KafkaService-SecurityGroup",
            vpc: vpc
        });
        securityGroup.addIngressRule(Peer.anyIpv4(), Port.tcp(19092));

        let service = new Ec2Service(this, stackName + "-kafka-service", {
            cluster: cluster,
            desiredCount: 1,
            taskDefinition: taskDefinition,
            securityGroups: [securityGroup],
            cloudMapOptions: {
                name: "kafka",
                cloudMapNamespace: dnsNamespace,
                dnsRecordType: DnsRecordType.A,
                container: kafkaContainerDefinition,
                containerPort: 19092
            }
        });
    }

    /**
     * Creates the filter service
     * @param stackName 
     * @param imageTag
     * @param vpc 
     * @param logGroup 
     * @param applicationLoadbalancer 
     * @param cluster 
     * @param dbCredentials 
     * @param dbLoadBalancerUrl
     */
    private createFilterService(stackName: string, imageTag: string, vpc: IVpc, logGroup: LogGroup,
        applicationListener: ApplicationListener, cluster: Cluster, dbCredentials: ISecret, dbLoadBalancerUrl: string) {

        //Load Balancer Config
        let targetGroup = new ApplicationTargetGroup(this, stackName + "-filter-service-target-group", {
            vpc: vpc,
            port: 5000,
            protocol: ApplicationProtocol.HTTP,
            healthCheck: {
                path: "/actuator/filter-health"
            }
        });

        new ApplicationListenerRule(this, "filterservice-listener-rule", {
            listener: applicationListener,
            priority: 1,
            conditions: [
                ListenerCondition.pathPatterns(["/actuator/filter-health", "/v1/filter", "/v1/filter/*"])
            ],
            action: ListenerAction.forward([targetGroup])
        });

        //Service Config
        let taskDefinition = new Ec2TaskDefinition(this, stackName + '-filter-service-taskdef');

        taskDefinition.addContainer(stackName + "-filter-service-container", {
            image: ContainerImage.fromEcrRepository(Repository.fromRepositoryName(this, "gtk-filter-service", "gtk-filter-service"), imageTag),
            cpu: 50,
            memoryLimitMiB: 256,
            essential: true,
            environment: {
                "SERVER_PORT": "5000",
                "DB_HOST": dbLoadBalancerUrl,
                "DB_PORT": "5000",
                "DB_NAME": "appdb",
                "DB_USER_NAME": dbCredentials.secretValueFromJson("UserName").toString(),
                "DB_PASSWORD": dbCredentials.secretValueFromJson("Password").toString(),
                "KAFKA_BOOTSTRAP_ADDRESS": "kafka.gtk.com:19092",
                "ACTIVE_PROFILE": "dev"
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
     * @param imageTag
     * @param vpc 
     * @param logGroup 
     * @param applicationLoadbalancer 
     * @param cluster 
     * @param dbCredentials 
     * @param dbLoadBalancerUrl
     */
    private createConditionService(stackName: string, imageTag: string, vpc: IVpc, logGroup: LogGroup,
        applicationListener: ApplicationListener, cluster: Cluster, dbCredentials: ISecret, dbLoadBalancerUrl: string) {

        //Load Balancer Config
        let targetGroup = new ApplicationTargetGroup(this, stackName + "-condition-service-target-group", {
            vpc: vpc,
            port: 5001,
            protocol: ApplicationProtocol.HTTP,
            healthCheck: {
                path: "/actuator/condition-health"
            }
        });

        new ApplicationListenerRule(this, "conditionservice-listener-rule", {
            listener: applicationListener,
            priority: 2,
            conditions: [
                ListenerCondition.pathPatterns(["/actuator/condition-health", "/v1/condition", "/v1/condition/*"])
            ],
            action: ListenerAction.forward([targetGroup])
        });

        //Service Config
        let taskDefinition = new Ec2TaskDefinition(this, stackName + '-condition-service-taskdef');

        taskDefinition.addContainer(stackName + "-condition-service-container", {
            image: ContainerImage.fromEcrRepository(Repository.fromRepositoryName(this, "gtk-condition-service", "gtk-condition-service"), imageTag),
            cpu: 50,
            memoryLimitMiB: 256,
            essential: true,
            environment: {
                "SERVER_PORT": "5001",
                "DB_HOST": dbLoadBalancerUrl,
                "DB_PORT": "5000",
                "DB_NAME": "appdb",
                "DB_USER_NAME": dbCredentials.secretValueFromJson("UserName").toString(),
                "DB_PASSWORD": dbCredentials.secretValueFromJson("Password").toString(),
                "KAFKA_BOOTSTRAP_ADDRESS": "kafka.gtk.com:19092",
                "ACTIVE_PROFILE": "dev"
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
     * @param imageTag
     * @param vpc 
     * @param logGroup 
     * @param applicationLoadbalancer 
     * @param cluster 
     * @param dbCredentials 
     * @param databaseUrl
     */
    private createScreenerService(stackName: string, imageTag: string, vpc: IVpc, logGroup: LogGroup, applicationListener: ApplicationListener,
        cluster: Cluster, dbCredentials: ISecret, databaseUrl: string) {

        //Load Balancer Config
        let targetGroup = new ApplicationTargetGroup(this, stackName + "-screener-service-target-group", {
            vpc: vpc,
            port: 5002,
            protocol: ApplicationProtocol.HTTP,
            healthCheck: {
                path: "/actuator/screener-health"
            }
        });

        new ApplicationListenerRule(this, "screenerservice-listener-rule", {
            listener: applicationListener,
            priority: 3,
            conditions: [
                ListenerCondition.pathPatterns(["/actuator/screener-health", "/v1/screener", "/v1/screener/*"])
            ],
            action: ListenerAction.forward([targetGroup])
        });

        //Service Config
        let taskDefinition = new Ec2TaskDefinition(this, stackName + '-screener-service-taskdef');

        taskDefinition.addContainer(stackName + "-screener-service-container", {
            image: ContainerImage.fromEcrRepository(Repository.fromRepositoryName(this, "gtk-screener-service", "gtk-screener-service"), imageTag),
            cpu: 50,
            memoryLimitMiB: 512,
            essential: true,
            environment: {
                "SERVER_PORT": "5002",
                "DB_HOST": databaseUrl,
                "DB_PORT": "5000",
                "DB_NAME": "appdb",
                "DB_USER_NAME": dbCredentials.secretValueFromJson("UserName").toString(),
                "DB_PASSWORD": dbCredentials.secretValueFromJson("Password").toString(),
                "KAFKA_BOOTSTRAP_ADDRESS": "kafka.gtk.com:19092",
                "ACTIVE_PROFILE": "dev"
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

    /**
     * Creates the expression service
     * @param stackName 
     * @param imageTag
     * @param vpc 
     * @param logGroup 
     * @param applicationLoadbalancer 
     * @param cluster 
     * @param dbCredentials 
     * @param databaseUrl
     */
    private createExpressionService(stackName: string, imageTag: string, vpc: IVpc, logGroup: LogGroup, applicationListener: ApplicationListener,
        cluster: Cluster, dbCredentials: ISecret, databaseUrl: string) {

        //Load Balancer Config
        let targetGroup = new ApplicationTargetGroup(this, stackName + "-expression-service-target-group", {
            vpc: vpc,
            port: 5003,
            protocol: ApplicationProtocol.HTTP,
            healthCheck: {
                path: "/actuator/expression-health"
            }
        });

        new ApplicationListenerRule(this, "expressionservice-listener-rule", {
            listener: applicationListener,
            priority: 4,
            conditions: [
                ListenerCondition.pathPatterns(["/actuator/expression-health"])
            ],
            action: ListenerAction.forward([targetGroup])
        });

        //Service Config
        let taskDefinition = new Ec2TaskDefinition(this, stackName + '-expression-service-taskdef');

        taskDefinition.addContainer(stackName + "-expression-service-container", {
            image: ContainerImage.fromEcrRepository(Repository.fromRepositoryName(this, "gtk-expression-service", "gtk-expression-service"), imageTag),
            cpu: 50,
            memoryLimitMiB: 512,
            essential: true,
            environment: {
                "SERVER_PORT": "5003",
                "DB_HOST": databaseUrl,
                "DB_PORT": "5000",
                "DB_NAME": "appdb",
                "DB_USER_NAME": dbCredentials.secretValueFromJson("UserName").toString(),
                "DB_PASSWORD": dbCredentials.secretValueFromJson("Password").toString(),
                "KAFKA_BOOTSTRAP_ADDRESS": "kafka.gtk.com:19092",
                "ACTIVE_PROFILE": "dev"
            },
            portMappings: [{
                containerPort: 5003
            }],
            logging: LogDriver.awsLogs({
                logGroup: logGroup,
                streamPrefix: logGroup.logGroupName
            }),
        });

        let service = new Ec2Service(this, stackName + "-expression-service", {
            cluster: cluster,
            desiredCount: 1,
            taskDefinition: taskDefinition
        });
        service.attachToApplicationTargetGroup(targetGroup);
    }

    /**
     * Creates the watchlist service
     * @param stackName 
     * @param imageTag
     * @param vpc 
     * @param logGroup 
     * @param applicationLoadbalancer 
     * @param cluster 
     * @param dbCredentials 
     * @param dbLoadBalancerUrl
     */
     private createWatchlistService(stackName: string, imageTag: string, vpc: IVpc, logGroup: LogGroup,
        applicationListener: ApplicationListener, cluster: Cluster, dbCredentials: ISecret, dbLoadBalancerUrl: string) {

        //Load Balancer Config
        let targetGroup = new ApplicationTargetGroup(this, stackName + "-watchlist-service-target-group", {
            vpc: vpc,
            port: 5004,
            protocol: ApplicationProtocol.HTTP,
            healthCheck: {
                path: "/actuator/watchlist-health"
            }
        });

        new ApplicationListenerRule(this, "watchlistservice-listener-rule", {
            listener: applicationListener,
            priority: 5,
            conditions: [
                ListenerCondition.pathPatterns(["/actuator/watchlist-health", "/v1/watchlist", "/v1/watchlist/*"])
            ],
            action: ListenerAction.forward([targetGroup])
        });

        //Service Config
        let taskDefinition = new Ec2TaskDefinition(this, stackName + '-watchlist-service-taskdef');

        taskDefinition.addContainer(stackName + "-watchlist-service-container", {
            image: ContainerImage.fromEcrRepository(Repository.fromRepositoryName(this, "gtk-watchlist-service", "gtk-watchlist-service"), imageTag),
            cpu: 50,
            memoryLimitMiB: 256,
            essential: true,
            environment: {
                "SERVER_PORT": "5004",
                "DB_HOST": dbLoadBalancerUrl,
                "DB_PORT": "5000",
                "DB_NAME": "appdb",
                "DB_USER_NAME": dbCredentials.secretValueFromJson("UserName").toString(),
                "DB_PASSWORD": dbCredentials.secretValueFromJson("Password").toString(),
                "KAFKA_BOOTSTRAP_ADDRESS": "kafka.gtk.com:19092",
                "ACTIVE_PROFILE": "dev"
            },
            portMappings: [{
                containerPort: 5004
            }],
            logging: LogDriver.awsLogs({
                logGroup: logGroup,
                streamPrefix: logGroup.logGroupName
            }),
        });

        let service = new Ec2Service(this, stackName + "-watchlist-service", {
            cluster: cluster,
            desiredCount: 1,
            taskDefinition: taskDefinition
        });
        service.attachToApplicationTargetGroup(targetGroup);
    }

    /**
     * Creates the stock service
     * @param stackName 
     * @param imageTag
     * @param vpc 
     * @param logGroup 
     * @param applicationLoadbalancer 
     * @param cluster 
     * @param dbCredentials 
     * @param dbLoadBalancerUrl
     */
     private createStockService(stackName: string, imageTag: string, vpc: IVpc, logGroup: LogGroup,
        applicationListener: ApplicationListener, cluster: Cluster, dbCredentials: ISecret, dbLoadBalancerUrl: string) {

        //Load Balancer Config
        let targetGroup = new ApplicationTargetGroup(this, stackName + "-stock-service-target-group", {
            vpc: vpc,
            port: 5005,
            protocol: ApplicationProtocol.HTTP,
            healthCheck: {
                path: "/actuator/stock-health"
            }
        });

        new ApplicationListenerRule(this, "stockservice-listener-rule", {
            listener: applicationListener,
            priority: 5,
            conditions: [
                ListenerCondition.pathPatterns(["/actuator/stock-health", "/v1/stock", "/v1/stock/*"])
            ],
            action: ListenerAction.forward([targetGroup])
        });

        //Service Config
        let taskDefinition = new Ec2TaskDefinition(this, stackName + '-stock-service-taskdef');

        taskDefinition.addContainer(stackName + "-stock-service-container", {
            image: ContainerImage.fromEcrRepository(Repository.fromRepositoryName(this, "gtk-stock-service", "gtk-stock-service"), imageTag),
            cpu: 50,
            memoryLimitMiB: 256,
            essential: true,
            environment: {
                "SERVER_PORT": "5005",
                "DB_HOST": dbLoadBalancerUrl,
                "DB_PORT": "5001",
                "DB_NAME": "stockdb",
                "DB_USER_NAME": dbCredentials.secretValueFromJson("UserName").toString(),
                "DB_PASSWORD": dbCredentials.secretValueFromJson("Password").toString(),
                "ACTIVE_PROFILE": "dev"
            },
            portMappings: [{
                containerPort: 5005
            }],
            logging: LogDriver.awsLogs({
                logGroup: logGroup,
                streamPrefix: logGroup.logGroupName
            }),
        });

        let service = new Ec2Service(this, stackName + "-stock-service", {
            cluster: cluster,
            desiredCount: 1,
            taskDefinition: taskDefinition
        });
        service.attachToApplicationTargetGroup(targetGroup);
    }
}