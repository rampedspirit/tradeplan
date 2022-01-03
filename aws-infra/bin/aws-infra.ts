#!/usr/bin/env node
import 'source-map-support/register';
import * as cdk from 'aws-cdk-lib';
import { EcrStack } from '../lib/repository/ecr-stack';
import { VpcStack } from '../lib/network/vpc-stack';
import { EcsDbStack } from '../lib/database/ecs-db-stack';

const app = new cdk.App();

const stackPrefix = app.node.tryGetContext('stackPrefix');

new EcrStack(app, stackPrefix + "-ECR-Stack", {
    stackName: stackPrefix + "-ECR-Stack"
});

new VpcStack(app, stackPrefix + "-VPC-Stack", {
    stackName: stackPrefix + "-VPC-Stack"
});

new EcsDbStack(app, stackPrefix + "-ECS-DB-Stack", {
    stackName: stackPrefix + "-ECS-DB-Stack",
    vpcName: stackPrefix + "-VPC-Stack",
    env: {
        account: process.env.CDK_DEFAULT_ACCOUNT,
        region: process.env.CDK_DEFAULT_REGION
    }
});

new EcsDbStack(app, stackPrefix + "-ECS-APP-Stack", {
    stackName: stackPrefix + "-ECS-APP-Stack",
    vpcName: stackPrefix + "-VPC-Stack",
    env: {
        account: process.env.CDK_DEFAULT_ACCOUNT,
        region: process.env.CDK_DEFAULT_REGION
    }
});
