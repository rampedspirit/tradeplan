import { NestedStack, NestedStackProps } from "aws-cdk-lib";
import { InstanceType, Peer, Port, SecurityGroup, Vpc } from "aws-cdk-lib/aws-ec2";
import { Cluster, EcsOptimizedImage } from "aws-cdk-lib/aws-ecs";
import { Construct } from "constructs";

export interface EcsClusterStackProps extends NestedStackProps {
    vpc: Vpc
}

export class EcsClusterStack extends NestedStack {
    dbCluster: Cluster;

    appCluster: Cluster;
    appClusterSecurityGroup: SecurityGroup;

    constructor(scope: Construct, id: string, props: EcsClusterStackProps) {
        super(scope, id, props);

        const stackPrefix = this.node.tryGetContext('stackPrefix');

        this.dbCluster = new Cluster(this, stackPrefix + "-DatabaseCluster", {
            clusterName: stackPrefix + "-DatabaseCluster",
            vpc: props.vpc,
            capacity: {
                instanceType: new InstanceType('t3a.small'),
                machineImage: EcsOptimizedImage.amazonLinux(),
                desiredCapacity: 1,
                minCapacity: 1,
                maxCapacity: 1,
                vpcSubnets: {
                    subnets: props.vpc.privateSubnets
                }
            }
        });

        this.appClusterSecurityGroup = new SecurityGroup(this, stackPrefix + "-ApplicationLoadBalancer-SecurityGroup", {
            vpc: props.vpc
        });
        this.appClusterSecurityGroup.addIngressRule(Peer.anyIpv4(), Port.tcpRange(5000, 5010), "Incoming HTTP traffic for Application LoadBalancer");

        this.appCluster = new Cluster(this, stackPrefix + "-ApplicationCluster", {
            clusterName: stackPrefix + "-ApplicationCluster",
            vpc: props.vpc,
            capacity: {
                instanceType: new InstanceType('t3a.small'),
                machineImage: EcsOptimizedImage.amazonLinux(),
                desiredCapacity: 1,
                minCapacity: 1,
                maxCapacity: 1,
                vpcSubnets: {
                    subnets: props.vpc.privateSubnets
                }
            }
        });
    }
}