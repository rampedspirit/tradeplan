import { CfnOutput, Stack, StackProps } from "aws-cdk-lib";
import { Construct } from "constructs";
import { Bucket } from 'aws-cdk-lib/aws-s3';
import { PolicyStatement, AnyPrincipal } from 'aws-cdk-lib/aws-iam';

export class S3Stack extends Stack {

    constructor(scope: Construct, id: string, props: StackProps) {
        super(scope, id, props);

        let bucket = new Bucket(this, props.stackName + "-bucket");
        
        bucket.addToResourcePolicy(new PolicyStatement({
            actions: ['s3:ListBucket'],
            resources: [bucket.bucketArn],
            principals: [new AnyPrincipal()],
        }));

        bucket.addToResourcePolicy(new PolicyStatement({
            actions: ['s3:PutObject', 's3:DeleteObject'],
            resources: [bucket.arnForObjects("*")],
            principals: [new AnyPrincipal()],
        }));

        //Export bucket name
        new CfnOutput(this, "bucketName", {
            value: bucket.bucketName,
            exportName: props.stackName + "-bucketName",
        });
    }
}