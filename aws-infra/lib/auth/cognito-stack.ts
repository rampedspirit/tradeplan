import { Stack, StackProps } from "aws-cdk-lib";
import { Construct } from "constructs";
import { AccountRecovery, StringAttribute, UserPool, UserPoolClient, VerificationEmailStyle } from "aws-cdk-lib/aws-cognito";

export class CognitoStack extends Stack {

    constructor(scope: Construct, id: string, props: StackProps) {
        super(scope, id, props);

        let userPool: UserPool = new UserPool(this, 'tradeplan-userpool', {
            selfSignUpEnabled: true,
            passwordPolicy: {
                minLength: 8,
                requireDigits: true
            },
            userVerification: {
                emailSubject: 'Verify your email for TradePlan',
                emailBody: 'Hi {name},\n\nPlease use verification code {####}\n\nRegards,\nTeam TradePlan',
                emailStyle: VerificationEmailStyle.CODE
            },
            autoVerify: {
                email: true
            },
            standardAttributes: {
                email: {
                    required: true
                }
            },
            customAttributes: {
                "name": new StringAttribute({ minLen: 5, maxLen: 15 })
            },
            accountRecovery: AccountRecovery.EMAIL_ONLY
        });

        let userPoolClient: UserPoolClient = userPool.addClient("tradeplan-userpool-client", {
            authFlows: {
                userPassword: true
            }
        });
    }
}