import { RemovalPolicy, Stack, StackProps } from "aws-cdk-lib";
import { InstanceType, IVpc, Peer, Port, SecurityGroup, Vpc } from "aws-cdk-lib/aws-ec2";
import { Repository } from "aws-cdk-lib/aws-ecr";
import { Cluster, ContainerImage, Ec2Service, Ec2TaskDefinition, EcsOptimizedImage, LogDriver } from "aws-cdk-lib/aws-ecs";
import { ApplicationListener, ApplicationListenerRule, ApplicationLoadBalancer, ApplicationProtocol, ApplicationTargetGroup, ListenerAction, ListenerCondition } from "aws-cdk-lib/aws-elasticloadbalancingv2";
import { LogGroup } from "aws-cdk-lib/aws-logs";
import { ISecret, Secret } from "aws-cdk-lib/aws-secretsmanager";
import { Construct } from "constructs";

export interface EcsAppStackProps extends StackProps {
    vpcName: string
}
export class EcsAppStack extends Stack {

    constructor(scope: Construct, id: string, props: EcsAppStackProps) {
        super(scope, id, props);

        //Find VPC
        const vpc: IVpc = Vpc.fromLookup(this, props.vpcName, {
            isDefault: false,
            vpcName: props.vpcName
        });

        // Secrets
        const dbCredentials = Secret.fromSecretCompleteArn(this, 'DbCredentials', 'arn:aws:secretsmanager:ap-south-1:838293343811:secret:prod/db/credentials-vquhXO');

        //Log Group
        const logGroup = new LogGroup(this, props.stackName + "-app-loggroup", {
            logGroupName: props.stackName + "-app-loggroup",
            removalPolicy: RemovalPolicy.DESTROY
        });

        //Cluster
        const cluster: Cluster = this.createCluster(props.stackName!, vpc);

        //Network Load Balancer
        const applicationLoadbalancer = this.createApplicationLoadBalancer(props.stackName!, vpc);
        const applicationListener = applicationLoadbalancer.addListener(props.stackName + "application-listener", {
            protocol: ApplicationProtocol.HTTP,
            port: 80,
            defaultAction: ListenerAction.fixedResponse(200, {
                contentType: "text/plain",
                messageBody: "Hello There! Looks like you have hit a wrong end point."
            })
        });

        //Services
        this.createFilterService(props.stackName!, vpc, logGroup, applicationListener, cluster, dbCredentials);
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
            instanceType: new InstanceType('t3a.small'),
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
        const securityGroup = new SecurityGroup(this, stackName + "-SecurityGroup", {
            securityGroupName: stackName + "-SecurityGroup",
            vpc: vpc
        });
        securityGroup.addIngressRule(Peer.anyIpv4(), Port.tcp(5000));
        securityGroup.addIngressRule(Peer.anyIpv4(), Port.tcp(5001));
        securityGroup.addIngressRule(Peer.anyIpv4(), Port.tcp(5002));
        loadBalancer.addSecurityGroup(securityGroup);

        return loadBalancer;
    }

    /**
     * Creates the filter service
     * @param stackName 
     * @param vpc 
     * @param logGroup 
     * @param applicationLoadbalancer 
     * @param cluster 
     * @param dbCredentials 
     */
    private createFilterService(stackName: string, vpc: IVpc, logGroup: LogGroup,
        applicationListener: ApplicationListener, cluster: Cluster, dbCredentials: ISecret) {

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
                "DB_HOST": "5000",
                "DB_PORT": "5000",
                "DB_NAME": "appdb",
                "DB_USER_NAME": dbCredentials.secretValueFromJson("UserName").toString(),
                "DB_PASSWORD": dbCredentials.secretValueFromJson("Password").toString()
            },
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
}