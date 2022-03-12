#!/usr/bin/env node
import 'source-map-support/register';
import * as cdk from 'aws-cdk-lib';
import { EcrStack } from '../lib/repository/ecr-stack';
import { VpcStack } from '../lib/network/vpc-stack';
import { EcsDbStack } from '../lib/database/ecs-db-stack';
import { EcsAppStack } from '../lib/application/ecs-app-stack';

const app = new cdk.App();

const stackPrefix = app.node.tryGetContext('stackPrefix');
const imageTag = app.node.tryGetContext('imageTag');

new EcrStack(app, stackPrefix + "-ECR-Stack", {
    stackName: stackPrefix + "-ECR-Stack"
});

new VpcStack(app, stackPrefix + "-VPC-Stack", {
    stackName: stackPrefix + "-VPC-Stack"
});

new EcsDbStack(app, stackPrefix + "-ECS-DB-Stack", {
    stackName: stackPrefix + "-ECS-DB-Stack",
    vpcName: stackPrefix + "-VPC-Stack",
    imageTag: imageTag,
    env: {
        account: process.env.CDK_DEFAULT_ACCOUNT,
        region: process.env.CDK_DEFAULT_REGION
    }
});

new EcsAppStack(app, stackPrefix + "-ECS-APP-Stack", {
    stackName: stackPrefix + "-ECS-APP-Stack",
    vpcName: stackPrefix + "-VPC-Stack",
    imageTag: imageTag,
    dbLoadBalancerDnsExportName: stackPrefix + "-ECS-DB-Stack-nlb-dns",
    env: {
        account: process.env.CDK_DEFAULT_ACCOUNT,
        region: process.env.CDK_DEFAULT_REGION
    }
});
