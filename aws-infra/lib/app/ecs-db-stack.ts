import { NestedStack, NestedStackProps } from "aws-cdk-lib";
import { Vpc } from "aws-cdk-lib/aws-ec2";
import { Cluster, ContainerImage, Ec2Service, Ec2TaskDefinition, LogDriver } from "aws-cdk-lib/aws-ecs";
import { NetworkTargetGroup, NetworkLoadBalancer, Protocol } from "aws-cdk-lib/aws-elasticloadbalancingv2";
import { LogGroup } from "aws-cdk-lib/aws-logs";
import { Construct } from "constructs";
import * as secretsmanager from 'aws-cdk-lib/aws-secretsmanager';

export interface EcsDbStackProps extends NestedStackProps {
    vpc: Vpc
    cluster: Cluster
}

export class EcsDbStack extends NestedStack {

    dbNetworkLoadbalancer: NetworkLoadBalancer;

    constructor(scope: Construct, id: string, props: EcsDbStackProps) {
        super(scope, id, props);

        const stackPrefix = this.node.tryGetContext('stackPrefix');

        // Secrets
        const dbCredentials = secretsmanager.Secret.fromSecretCompleteArn(this, 'DbCredentials', 'arn:aws:secretsmanager:ap-south-1:838293343811:secret:prod/db/credentials-vquhXO');

        //Log Group
        const logGroup = new LogGroup(this, stackPrefix + "-db-loggroup", {
            logGroupName: stackPrefix + "-db-loggroup"
        });

        //Network Load Balancer
        this.dbNetworkLoadbalancer = this.createNetworkLoadBalancer(stackPrefix, props.vpc);

        // APP Database
        let appDbTargetGroup = this.createAppDbTargetGroup(stackPrefix, props.vpc);
        let appdbListener = this.dbNetworkLoadbalancer.addListener("appdb-listener", {
            protocol: Protocol.TCP,
            port: 5000
        });
        appdbListener.addTargetGroups("appdb-listener-target-group", appDbTargetGroup);

        let appDbService = this.createAppDbService(stackPrefix, props.cluster, logGroup, dbCredentials);
        appDbService.attachToNetworkTargetGroup(appDbTargetGroup);

        // Stock Database
        // let stockDbTargetGroup = this.createStockDbTargetGroup(stackName, props.vpc);
        // let stockdbListener = loadbalancer.addListener("stockdb-listener", {
        //     protocol: Protocol.TCP,
        //     port: 5001
        // });
        // stockdbListener.addTargetGroups("stockdb-listener-target-group", stockDbTargetGroup);

        // let stockDbService = this.createStockDbService(stackName, props.cluster);
        // stockDbService.attachToNetworkTargetGroup(stockDbTargetGroup);
    }

    private createNetworkLoadBalancer(stackName: string, vpc: Vpc): NetworkLoadBalancer {
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

    private createAppDbTargetGroup(stackName: string, vpc: Vpc): NetworkTargetGroup {
        let targetGroup = new NetworkTargetGroup(this, stackName + "-appdb-target-group", {
            vpc: vpc,
            port: 5000,
            protocol: Protocol.TCP
        });
        return targetGroup;
    }

    private createAppDbService(stackName: string, cluster: Cluster, logGroup: LogGroup, dbCredentials: secretsmanager.ISecret): Ec2Service {
        let taskDefinition = new Ec2TaskDefinition(this, stackName + '-appdb-taskdef', {
        });

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

        return service;
    }

    private createStockDbTargetGroup(stackName: string, vpc: Vpc): NetworkTargetGroup {
        let targetGroup = new NetworkTargetGroup(this, stackName + "-stockdb-target-group", {
            vpc: vpc,
            port: 5001,
            protocol: Protocol.TCP
        });
        return targetGroup;
    }

    private createStockDbService(stackName: string, cluster: Cluster): Ec2Service {
        let taskDefinition = new Ec2TaskDefinition(this, stackName + '-stockdb-taskdef', {
        });

        taskDefinition.addContainer(stackName + "-stockdb-container", {
            image: ContainerImage.fromRegistry("enterprisedb/postgresql:12"),
            cpu: 50,
            memoryLimitMiB: 1024,
            essential: true,
            portMappings: [{
                containerPort: 5432
            }]
        });

        let service = new Ec2Service(this, stackName + "-stockdb-service", {
            cluster: cluster,
            desiredCount: 1,
            taskDefinition: taskDefinition
        });

        return service;
    }
}