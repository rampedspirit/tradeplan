import { CfnOutput, Stack, StackProps } from "aws-cdk-lib";
import { Construct } from "constructs";
import { Bucket } from 'aws-cdk-lib/aws-s3';
import { Distribution } from "aws-cdk-lib/aws-cloudfront";
import { S3Origin } from "aws-cdk-lib/aws-cloudfront-origins";
import { PolicyStatement, AnyPrincipal } from 'aws-cdk-lib/aws-iam';

export class StaticSiteStack extends Stack {

    constructor(scope: Construct, id: string, props: StackProps) {
        super(scope, id, props);

        //S3 Bucket
        let bucket = new Bucket(this, props.stackName + "-bucket");

        bucket.addToResourcePolicy(new PolicyStatement({
            actions: ['s3:ListBucket'],
            resources: [bucket.bucketArn],
            principals: [new AnyPrincipal()],
        }));

        bucket.addToResourcePolicy(new PolicyStatement({
            actions: ["*"],
            resources: [bucket.arnForObjects("*")],
            principals: [new AnyPrincipal()],
        }));

        // Cloud Front Distribution
        let distribution: Distribution = new Distribution(this, props.stackName + "-distribution", {
            defaultBehavior: {
                origin: new S3Origin(Bucket.fromBucketArn(this, props.stackName + "-bucket-cloudfront", bucket.bucketArn))
            },
            defaultRootObject: "index.html"
        });

        //Export bucket name
        new CfnOutput(this, "bucketName", {
            value: bucket.bucketName,
            exportName: props.stackName + "-bucketName",
        });

        //Export cloudfront domain name
        new CfnOutput(this, "cloudfrontDomain", {
            value: distribution.domainName,
            exportName: props.stackName + "-cloudfrontDomain",
        });
    }
}