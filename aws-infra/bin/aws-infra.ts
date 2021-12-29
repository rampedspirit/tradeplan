#!/usr/bin/env node
import 'source-map-support/register';
import * as cdk from 'aws-cdk-lib';
import { MainInfraStack } from '../lib/main-infra-stack';

const app = new cdk.App();
const stackPrefix = app.node.tryGetContext('stackPrefix');
new MainInfraStack(app, stackPrefix, { stackName: stackPrefix });