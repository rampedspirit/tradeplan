import { NestedStack, NestedStackProps, } from 'aws-cdk-lib';
import { EcsApplication } from 'aws-cdk-lib/aws-codedeploy';
import { CfnRepository } from "aws-cdk-lib/aws-ecr";
import { Construct } from 'constructs';
import { EcsAppStack } from './ecs-app-stack';
import { EcsClusterStack } from './ecs-cluster-stack';
import { EcsDbStack } from './ecs-db-stack';
import { VpcStack } from './vpc-stack';

export class AppInfraStack extends NestedStack {

  constructor(scope: Construct, id: string, props?: NestedStackProps) {
    super(scope, id, props);

    const vpcStack = new VpcStack(this, "VpcStack");

    const ecsClusterStack = new EcsClusterStack(this, "EcsClusterStack", {
      vpc: vpcStack.vpc
    });
    ecsClusterStack.addDependency(vpcStack);

    const ecsDbStack = new EcsDbStack(this, "EcsDbStack", {
      vpc: vpcStack.vpc,
      cluster: ecsClusterStack.dbCluster
    });
    ecsDbStack.addDependency(ecsClusterStack);

    const ecsAppStack = new EcsAppStack(this, "EcsAppStack", {
      vpc: vpcStack.vpc,
      cluster: ecsClusterStack.appCluster,
      dbNetworkLoadBalancer: ecsDbStack.dbNetworkLoadbalancer
    });
    ecsAppStack.addDependency(ecsDbStack);
  }
}
