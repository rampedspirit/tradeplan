import { Stack, StackProps } from "aws-cdk-lib";
import { SubnetType, Vpc } from "aws-cdk-lib/aws-ec2";
import { Construct } from "constructs";

export class VpcStack extends Stack {

    constructor(scope: Construct, id: string, props: StackProps) {
        super(scope, id, props);

        let vpcName: string = props.stackName!;
        new Vpc(this, vpcName, {
            vpcName: vpcName,
            maxAzs: 2,
            enableDnsSupport: true,
            enableDnsHostnames: true,
            subnetConfiguration: [
                {
                    name: "privateSubnet",
                    subnetType: SubnetType.PRIVATE_ISOLATED,
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