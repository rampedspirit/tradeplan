import { Stack, StackProps } from "aws-cdk-lib";
import { InstanceType, IVpc, Vpc } from "aws-cdk-lib/aws-ec2";
import { Cluster, ContainerImage, Ec2Service, Ec2TaskDefinition, EcsOptimizedImage, LogDriver } from "aws-cdk-lib/aws-ecs";
import { NetworkLoadBalancer, NetworkTargetGroup, Protocol } from "aws-cdk-lib/aws-elasticloadbalancingv2";
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
        const vpc: IVpc = Vpc.fromLookup(scope, props.vpcName, {
            isDefault: false,
            vpcName: props.vpcName
        });

        // Secrets
        const dbCredentials = Secret.fromSecretCompleteArn(this, 'DbCredentials', 'arn:aws:secretsmanager:ap-south-1:838293343811:secret:prod/db/credentials-vquhXO');

        //Log Group
        const logGroup = new LogGroup(this, props.stackName + "-db-loggroup", {
            logGroupName: props.stackName + "-db-loggroup"
        });

        //Cluster
        const cluster: Cluster = this.createCluster(props.stackName!, vpc);

        //Network Load Balancer
        const dbNetworkLoadbalancer = this.createNetworkLoadBalancer(props.stackName!, vpc);

        //Services
        this.createApplicationDatabaseService(props.stackName!, vpc, logGroup, dbNetworkLoadbalancer, cluster, dbCredentials);
        this.createStockDatabaseService(props.stackName!, vpc, logGroup, dbNetworkLoadbalancer, cluster, dbCredentials);
    }

    /**
     * Creates the Database Cluster
     * @param stackName 
     */
    private createCluster(stackName: string, vpc: IVpc): Cluster {
        return new Cluster(this, stackName + "-DatabaseCluster", {
            clusterName: stackName + "-DatabaseCluster",
            vpc: vpc,
            capacity: {
                instanceType: new InstanceType('t3a.small'),
                machineImage: EcsOptimizedImage.amazonLinux(),
                desiredCapacity: 1,
                minCapacity: 1,
                maxCapacity: 1,
                vpcSubnets: {
                    subnets: vpc.privateSubnets
                }
            }
        });
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
     * @param dbNetworkLoadbalancer 
     * @param cluster 
     * @param dbCredentials 
     */
    private createApplicationDatabaseService(stackName: string, vpc: IVpc, logGroup: LogGroup,
        dbNetworkLoadbalancer: NetworkLoadBalancer, cluster: Cluster, dbCredentials: ISecret) {
        //Load Balancer Config
        let appDbTargetGroup = new NetworkTargetGroup(this, stackName + "-appdb-target-group", {
            vpc: vpc,
            port: 5000,
            protocol: Protocol.TCP
        });
        let appdbListener = dbNetworkLoadbalancer.addListener("appdb-nlb-listener", {
            protocol: Protocol.TCP,
            port: 5000
        });
        appdbListener.addTargetGroups("appdb-listener-target-group", appDbTargetGroup);

        //Service Config
        let taskDefinition = new Ec2TaskDefinition(this, stackName + '-appdb-taskdef');

        taskDefinition.addContainer(stackName + "-appdb-container", {
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
            }),
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
     * @param dbNetworkLoadbalancer 
     * @param cluster 
     * @param dbCredentials 
     */
    private createStockDatabaseService(stackName: string, vpc: IVpc, logGroup: LogGroup,
        dbNetworkLoadbalancer: NetworkLoadBalancer, cluster: Cluster, dbCredentials: ISecret) {
        //Load Balancer Config
        let stockDbTargetGroup = new NetworkTargetGroup(this, stackName + "-stockdb-target-group", {
            vpc: vpc,
            port: 5000,
            protocol: Protocol.TCP
        });
        let stockdbListener = dbNetworkLoadbalancer.addListener("stockdb-nlb-listener", {
            protocol: Protocol.TCP,
            port: 5000
        });
        stockdbListener.addTargetGroups("stockdb-listener-target-group", stockDbTargetGroup);

        //Service Config
        let taskDefinition = new Ec2TaskDefinition(this, stackName + '-stockdb-taskdef');

        taskDefinition.addContainer(stackName + "-stockdb-container", {
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
            }),
        });

        let service = new Ec2Service(this, stackName + "-stockdb-service", {
            cluster: cluster,
            desiredCount: 1,
            taskDefinition: taskDefinition
        });
        service.attachToNetworkTargetGroup(stockDbTargetGroup);
    }
}