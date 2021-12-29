import { NestedStack, NestedStackProps } from "aws-cdk-lib";
import { Construct } from "constructs";
import { EcrStack } from "./ecr-stack";

export class BaseInfraStack extends NestedStack {

    constructor(scope: Construct, id: string, props?: NestedStackProps) {
        super(scope, id, props);

        //Create ECR Repositories
        const ecrStack = new EcrStack(this, "EcrStack", {
            repoNames: ["gtk-filter-service", "gtk-condition-service", "gtk-screener-service", "gtk-execution-service"]
        });
    }
}