#!/usr/bin/env node
import 'source-map-support/register';
import * as cdk from 'aws-cdk-lib';
import { EcrStack } from '../lib2/repository/ecr-stack';
import { VpcStack } from '../lib2/network/vpc-stack';
import { EcsDbStack } from '../lib2/database/ecs-db-stack';

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
    vpcName: stackPrefix + "-VPC-Stack"
});
