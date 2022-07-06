import { CfnOutput, Stack, StackProps } from "aws-cdk-lib";
import { Construct } from "constructs";
import { Bucket } from 'aws-cdk-lib/aws-s3';

export class S3Stack extends Stack {

    constructor(scope: Construct, id: string, props: StackProps) {
        super(scope, id, props);

        let bucket = new Bucket(this, props.stackName + "-bucket");
        bucket.grantPublicAccess

        //Export bucket name
        new CfnOutput(this, "bucketName", {
            value: bucket.bucketName,
            exportName: props.stackName + "-bucketName",
        });
    }
}