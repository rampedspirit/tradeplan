import { Stack, StackProps } from "aws-cdk-lib";
import { Construct } from "constructs";
import { AppInfraStack } from "./app/app-infra-stack";
import { BaseInfraStack } from "./base/base-infra-stack";

export class MainInfraStack extends Stack {

    constructor(scope: Construct, id: string, props?: StackProps) {
        super(scope, id, props);

        const action = this.node.tryGetContext('action');

        const baseInfraStack = new BaseInfraStack(this, "BaseInfraStack");
        if (action == "start") {
            const appInfraStack = new AppInfraStack(this, "AppInfraStack");
            appInfraStack.addDependency(baseInfraStack);
        }
    }
}