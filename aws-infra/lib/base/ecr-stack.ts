import { NestedStack, NestedStackProps } from "aws-cdk-lib";
import { Construct } from "constructs";

import * as ecr from 'aws-cdk-lib/aws-ecr';
import { StringParameter } from "aws-cdk-lib/aws-ssm";

/**
 * Properties for ECR Stack
 */
export interface EcrStackProps extends NestedStackProps {
    repoNames: string[];
}

/**
 * Stack for ECR Resources
 */
export class EcrStack extends NestedStack {

    constructor(scope: Construct, id: string, props: EcrStackProps) {
        super(scope, id, props);

        for (var repoName of props.repoNames) {
            let repository = new ecr.CfnRepository(this, repoName, {
                repositoryName: repoName
            });
        }
    }
}