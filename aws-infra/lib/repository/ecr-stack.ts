import { Stack, StackProps } from "aws-cdk-lib";
import { CfnRepository } from "aws-cdk-lib/aws-ecr";
import { Construct } from "constructs";

/**
 * Stack for ECR Repositories
 */
export class EcrStack extends Stack {

    private REPO_NAMES: string[] = ["appdb", "stockdb", "gtk-filter-service", "gtk-condition-service", "gtk-screener-service",
        "gtk-expression-service", "gtk-watchlist-service", "gtk-datawriter-service", "gtk-mockfeed-service"];

    constructor(scope: Construct, id: string, props?: StackProps) {
        super(scope, id, props);

        for (var repoName of this.REPO_NAMES) {
            new CfnRepository(this, repoName, {
                repositoryName: repoName
            });
        }
    }
}