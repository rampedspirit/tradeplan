import { CfnOutput, RemovalPolicy, Stack, StackProps } from "aws-cdk-lib";
import { InstanceType, IVpc, Peer, Port, SecurityGroup, Vpc } from "aws-cdk-lib/aws-ec2";
import { Cluster, ContainerDependencyCondition, ContainerImage, Ec2Service, Ec2TaskDefinition, EcsOptimizedImage, LogDriver, NetworkMode, Scope } from "aws-cdk-lib/aws-ecs";
import { NetworkLoadBalancer, NetworkTargetGroup, Protocol } from "aws-cdk-lib/aws-elasticloadbalancingv2";
import { Policy, PolicyStatement } from "aws-cdk-lib/aws-iam";
import { LogGroup } from "aws-cdk-lib/aws-logs";
import { ISecret, Secret } from "aws-cdk-lib/aws-secretsmanager";
import { Construct } from "constructs";

export interface EcsDbStackProps extends StackProps {
    vpcName: string
}

export class EcsDbStack extends Stack {

    constructor(scope: Construct, id: string, props: EcsDbStackProps) {
        super(scope, id, props);

        //Find VPC
        const vpc: IVpc = Vpc.fromLookup(this, props.vpcName, {
            vpcName: props.vpcName,
            region: 'ap-south-1'
        });

        // Secrets
        const dbCredentials = Secret.fromSecretCompleteArn(this, 'DbCredentials', 'arn:aws:secretsmanager:ap-south-1:838293343811:secret:prod/db/credentials-vquhXO');

        //Log Group
        const logGroup = new LogGroup(this, props.stackName + "-db-loggroup", {
            logGroupName: props.stackName + "-db-loggroup",
            removalPolicy: RemovalPolicy.DESTROY
        });

        //Cluster
        const cluster: Cluster = this.createCluster(props.stackName!, vpc);

        //Network Load Balancer
        const dbNetworkLoadbalancer = this.createNetworkLoadBalancer(props.stackName!, vpc);
        const loadBalancerUrl = dbNetworkLoadbalancer.loadBalancerDnsName + ":" + 19092;

        //Export load balancer dns
        new CfnOutput(this, "dbLoadBalancerDnsName", {
            value: dbNetworkLoadbalancer.loadBalancerDnsName,
            exportName: props.stackName + "-nlb-dns",
        });

        //Services
        this.createApplicationDatabaseService(props.stackName!, vpc, logGroup, dbNetworkLoadbalancer, cluster, dbCredentials);
        this.createStockDatabaseService(props.stackName!, vpc, logGroup, dbNetworkLoadbalancer, cluster, dbCredentials);
        this.createKafkaService(props.stackName!, vpc, logGroup, dbNetworkLoadbalancer, cluster, loadBalancerUrl + ":19092");
    }

    /**
     * Creates the Database Cluster
     * @param stackName 
     */
    private createCluster(stackName: string, vpc: IVpc): Cluster {
        const cluster = new Cluster(this, stackName + "-DatabaseCluster", {
            clusterName: stackName + "-DatabaseCluster",
            vpc: vpc
        });

        //Auto scaling group (EC2 Instance)
        const autoScalingGroup = cluster.addCapacity(stackName + "-DatabaseCluster-AutoScalingGroup", {
            instanceType: new InstanceType('t3a.medium'),
            machineImage: EcsOptimizedImage.amazonLinux(),
            desiredCapacity: 1,
            minCapacity: 1,
            maxCapacity: 1,
            vpcSubnets: {
                subnets: vpc.privateSubnets
            }
        });

        //Security group for ingress form NLB
        const securityGroup = new SecurityGroup(this, stackName + "-SecurityGroup", {
            securityGroupName: stackName + "-SecurityGroup",
            vpc: vpc
        });
        securityGroup.addIngressRule(Peer.anyIpv4(), Port.tcpRange(31000, 61000));
        autoScalingGroup.addSecurityGroup(securityGroup);

        //Install rexray volume driver
        autoScalingGroup.addUserData('docker plugin install rexray/ebs REXRAY_PREEMPT=true EBS_REGION=ap-south-1 --grant-all-permissions \nstop ecs \nstart ecs');

        //Add EBS Policy
        const ebsPolicy = this.createEBSPolicy();
        autoScalingGroup.role.attachInlinePolicy(ebsPolicy);

        return cluster;
    }

    private createEBSPolicy(): Policy {
        const ec2PolicyEbs = new Policy(this, 'ec2-policy-create-ebs', {
            policyName: 'REXRay-EBS',
            statements: [
                PolicyStatement.fromJson({
                    Effect: 'Allow',
                    Action: [
                        'ec2:AttachVolume',
                        'ec2:CreateVolume',
                        'ec2:CreateSnapshot',
                        'ec2:CreateTags',
                        'ec2:DeleteVolume',
                        'ec2:DeleteSnapshot',
                        'ec2:DescribeAvailabilityZones',
                        'ec2:DescribeInstances',
                        'ec2:DescribeVolumes',
                        'ec2:DescribeVolumeAttribute',
                        'ec2:DescribeVolumeStatus',
                        'ec2:DescribeSnapshots',
                        'ec2:CopySnapshot',
                        'ec2:DescribeSnapshotAttribute',
                        'ec2:DetachVolume',
                        'ec2:ModifySnapshotAttribute',
                        'ec2:ModifyVolumeAttribute',
                        'ec2:DescribeTags',
                    ],
                    Resource: '*',
                }),
            ],
        });
        return ec2PolicyEbs;
    }

    /**
     * Creates NetworkLoadBalancer
     * @param stackName
     * @param vpc 
     * @returns 
     */
    private createNetworkLoadBalancer(stackName: string, vpc: IVpc): NetworkLoadBalancer {
        let loadBalancer = new NetworkLoadBalancer(this, "nlb", {
            vpc: vpc,
            internetFacing: false,
            loadBalancerName: stackName + "-nlb",
            vpcSubnets: {
                subnets: vpc.privateSubnets
            }
        });
        return loadBalancer;
    }

    /**
     * Creates the application database service
     * @param stackName 
     * @param vpc 
     * @param logGroup 
     * @param loadbalancer 
     * @param cluster 
     * @param dbCredentials 
     */
    private createApplicationDatabaseService(stackName: string, vpc: IVpc, logGroup: LogGroup,
        loadbalancer: NetworkLoadBalancer, cluster: Cluster, dbCredentials: ISecret) {
        //Load Balancer Config
        let appDbTargetGroup = new NetworkTargetGroup(this, stackName + "-appdb-target-group", {
            vpc: vpc,
            port: 5000,
            protocol: Protocol.TCP
        });
        let appdbListener = loadbalancer.addListener("appdb-nlb-listener", {
            protocol: Protocol.TCP,
            port: 5000
        });
        appdbListener.addTargetGroups("appdb-listener-target-group", appDbTargetGroup);

        //Service Config
        let taskDefinition = new Ec2TaskDefinition(this, stackName + '-appdb-taskdef');

        let volumeName = stackName + "-appdb-volume";
        taskDefinition.addVolume({
            name: volumeName,
            dockerVolumeConfiguration: {
                autoprovision: true,
                scope: Scope.SHARED,
                driver: 'rexray/ebs',
                driverOpts: {
                    volumetype: 'gp2',
                    size: '10',
                },
            },
        });

        const containerDefinition = taskDefinition.addContainer(stackName + "-appdb-container", {
            image: ContainerImage.fromRegistry("enterprisedb/postgresql:12"),
            cpu: 50,
            memoryLimitMiB: 1024,
            essential: true,
            environment: {
                "POSTGRES_USER": dbCredentials.secretValueFromJson("UserName").toString(),
                "POSTGRES_PASSWORD": dbCredentials.secretValueFromJson("Password").toString(),
                "POSTGRES_DB": "appdb"
            },
            portMappings: [{
                containerPort: 5432
            }],
            logging: LogDriver.awsLogs({
                logGroup: logGroup,
                streamPrefix: logGroup.logGroupName
            })
        });

        containerDefinition.addMountPoints({
            sourceVolume: volumeName,
            containerPath: '/usr/local/pgsql/data',
            readOnly: false
        });

        let service = new Ec2Service(this, stackName + "-appdb-service", {
            cluster: cluster,
            desiredCount: 1,
            taskDefinition: taskDefinition
        });
        service.attachToNetworkTargetGroup(appDbTargetGroup);
    }

    /**
     * Creates the stock database service
     * @param stackName 
     * @param vpc 
     * @param logGroup 
     * @param loadbalancer 
     * @param cluster 
     * @param dbCredentials 
     */
    private createStockDatabaseService(stackName: string, vpc: IVpc, logGroup: LogGroup,
        loadbalancer: NetworkLoadBalancer, cluster: Cluster, dbCredentials: ISecret) {
        //Load Balancer Config
        let stockDbTargetGroup = new NetworkTargetGroup(this, stackName + "-stockdb-target-group", {
            vpc: vpc,
            port: 5001,
            protocol: Protocol.TCP
        });
        let stockdbListener = loadbalancer.addListener("stockdb-nlb-listener", {
            protocol: Protocol.TCP,
            port: 5001
        });
        stockdbListener.addTargetGroups("stockdb-listener-target-group", stockDbTargetGroup);

        //Service Config
        let taskDefinition = new Ec2TaskDefinition(this, stackName + '-stockdb-taskdef');

        let volumeName = stackName + "-stockdb-volume";
        taskDefinition.addVolume({
            name: volumeName,
            dockerVolumeConfiguration: {
                autoprovision: true,
                scope: Scope.SHARED,
                driver: 'rexray/ebs',
                driverOpts: {
                    volumetype: 'gp2',
                    size: '10',
                },
            },
        });

        const containerDefinition = taskDefinition.addContainer(stackName + "-stockdb-container", {
            image: ContainerImage.fromRegistry("enterprisedb/postgresql:12"),
            cpu: 50,
            memoryLimitMiB: 1024,
            essential: true,
            environment: {
                "POSTGRES_USER": dbCredentials.secretValueFromJson("UserName").toString(),
                "POSTGRES_PASSWORD": dbCredentials.secretValueFromJson("Password").toString(),
                "POSTGRES_DB": "stockdb"
            },
            portMappings: [{
                containerPort: 5432
            }],
            logging: LogDriver.awsLogs({
                logGroup: logGroup,
                streamPrefix: logGroup.logGroupName
            }),
        });

        containerDefinition.addMountPoints({
            sourceVolume: volumeName,
            containerPath: '/usr/local/pgsql/data',
            readOnly: false
        });

        let service = new Ec2Service(this, stackName + "-stockdb-service", {
            cluster: cluster,
            desiredCount: 1,
            taskDefinition: taskDefinition
        });
        service.attachToNetworkTargetGroup(stockDbTargetGroup);
    }

    /**
    * Creates the Kafka service
    * @param stackName 
    * @param vpc 
    * @param logGroup 
    * @param loadbalancer 
    * @param cluster 
    * @param dbCredentials 
    * @param dbLoadBalancerUrl
    */
    private createKafkaService(stackName: string, vpc: IVpc, logGroup: LogGroup,
        loadbalancer: NetworkLoadBalancer, cluster: Cluster, kafkaBootstrapUrl: string) {

        //Load Balancer Config
        let targetGroup = new NetworkTargetGroup(this, stackName + "-kafka-target-group", {
            vpc: vpc,
            port: 19092,
            protocol: Protocol.TCP
        });

        const listener = loadbalancer.addListener(stackName + "kafka-listener", {
            protocol: Protocol.TCP,
            port: 19092
        });
        listener.addTargetGroups(stackName + "kafka-listener-targetgroup", targetGroup);

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
                "KAFKA_INTER_BROKER_LISTENER_NAME": "INTERNAL"
            },
            portMappings: [{
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

        taskDefinition.defaultContainer = kafkaContainerDefinition;

        let service = new Ec2Service(this, stackName + "-kafka-service", {
            cluster: cluster,
            desiredCount: 1,
            taskDefinition: taskDefinition
        });
        service.attachToNetworkTargetGroup(targetGroup);
    }
}