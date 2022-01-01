import { NestedStack, NestedStackProps } from "aws-cdk-lib";
import { Protocol, SecurityGroup, Vpc } from "aws-cdk-lib/aws-ec2";
import { Cluster, ContainerImage, Ec2Service, Ec2TaskDefinition, LogDriver } from "aws-cdk-lib/aws-ecs";
import { ApplicationListener, ApplicationListenerRule, ApplicationLoadBalancer, ApplicationProtocol, ApplicationTargetGroup, ListenerAction, ListenerCondition, NetworkLoadBalancer } from "aws-cdk-lib/aws-elasticloadbalancingv2";
import { Construct } from "constructs";
import * as secretsmanager from 'aws-cdk-lib/aws-secretsmanager';
import { ILogGroup, LogGroup } from "aws-cdk-lib/aws-logs";
import { Repository } from "aws-cdk-lib/aws-ecr";

export interface EcsAppStackProps extends NestedStackProps {
    vpc: Vpc
    cluster: Cluster
    dbNetworkLoadBalancer: NetworkLoadBalancer
    appClusterSecurityGroup: SecurityGroup
}

export class EcsAppStack extends NestedStack {

    constructor(scope: Construct, id: string, props: EcsAppStackProps) {
        super(scope, id, props);

        const stackPrefix = this.node.tryGetContext('stackPrefix');

        // Secrets
        const dbCredentials = secretsmanager.Secret.fromSecretCompleteArn(this, 'DbCredentials', 'arn:aws:secretsmanager:ap-south-1:838293343811:secret:prod/db/credentials-vquhXO');

        //Log Group
        const logGroup = new LogGroup(this, stackPrefix + "-app-loggroup", {
            logGroupName: stackPrefix + "-app-loggroup"
        });

        //Application Load Balancer
        const appLoadbalancer = this.createApplicationLoadBalancer(stackPrefix, props.vpc);
        appLoadbalancer.addSecurityGroup(props.appClusterSecurityGroup);

        const applicationListener = appLoadbalancer.addListener(stackPrefix + "application-listener", {
            protocol: ApplicationProtocol.HTTP,
            port: 80,
            defaultAction: ListenerAction.fixedResponse(200, {
                contentType: "text/plain",
                messageBody: "Hello There! Looks like you have hit a wrong end point."
            })
        });

        //Filter Service
        this.createFilterService(stackPrefix, props.cluster, props.vpc, applicationListener,
            props.dbNetworkLoadBalancer, dbCredentials, logGroup);

    }

    private createApplicationLoadBalancer(stackName: string, vpc: Vpc): ApplicationLoadBalancer {
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

    private createFilterService(stackPrefix: string, cluster: Cluster, vpc: Vpc, applicationListener: ApplicationListener,
        dbNetworkLoadBalancer: NetworkLoadBalancer, dbCredentials: secretsmanager.ISecret, logGroup: ILogGroup) {

        const filterServiceTargetGroup = new ApplicationTargetGroup(this, stackPrefix + "-filterservice-target-group", {
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
            action: ListenerAction.forward([filterServiceTargetGroup])
        });

        //Service
        let taskDefinition = new Ec2TaskDefinition(this, stackPrefix + '-filterservice-taskdef', {
        });

        taskDefinition.addContainer(stackPrefix + "-filterservice-container", {
            image: ContainerImage.fromEcrRepository(Repository.fromRepositoryName(this, "gtk-filter-service", "gtk-filter-service")),
            cpu: 50,
            memoryLimitMiB: 500,
            essential: true,
            environment: {
                "SERVER_PORT": "5000",
                "DB_HOST": dbNetworkLoadBalancer.loadBalancerDnsName,
                "DB_PORT": "5000",
                "DB_NAME": "appdb",
                "DB_USER_NAME": dbCredentials.secretValueFromJson("UserName").toString(),
                "DB_PASSWORD": dbCredentials.secretValueFromJson("Password").toString(),
            },
            portMappings: [{
                containerPort: 5000
            }],
            logging: LogDriver.awsLogs({
                logGroup: logGroup,
                streamPrefix: logGroup.logGroupName
            }),
        });

        let service = new Ec2Service(this, stackPrefix + "-filter-service", {
            cluster: cluster,
            desiredCount: 1,
            taskDefinition: taskDefinition
        });

        return service;
    }
}