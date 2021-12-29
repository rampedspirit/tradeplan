import { NestedStack, NestedStackProps } from 'aws-cdk-lib';
import { Vpc } from 'aws-cdk-lib/aws-ec2';
import { SubnetType } from 'aws-cdk-lib/aws-ec2';

import { Construct } from 'constructs';

export class VpcStack extends NestedStack {
    vpc: Vpc;

    constructor(scope: Construct, id: string, props?: NestedStackProps) {
        super(scope, id, props);

        const stackPrefix = this.node.tryGetContext('stackPrefix');
        this.vpc = new Vpc(this, stackPrefix + "-Vpc", {
            vpcName: stackPrefix + "-Vpc",
            maxAzs: 2,
            enableDnsSupport: true,
            enableDnsHostnames: true,
            subnetConfiguration: [
                {
                    name: "privateSubnet",
                    subnetType: SubnetType.PRIVATE_WITH_NAT,
                    cidrMask: 24
                },
                {
                    name: "publicSubnet",
                    subnetType: SubnetType.PUBLIC,
                    cidrMask: 24
                },
            ]
        });
    }
}