# Welcome to your CDK TypeScript project!

This is a blank project for TypeScript development with CDK.

The `cdk.json` file tells the CDK Toolkit how to execute your app.

## Useful commands

 * `npm run build`   compile typescript to js
 * `npm run watch`   watch for changes and compile
 * `npm run test`    perform the jest unit tests
 * `cdk deploy`      deploy this stack to your default AWS account/region
 * `cdk diff`        compare deployed stack with current state
 * `cdk synth`       emits the synthesized CloudFormation template

## Local run commands
 * cdk synth -v --ca-bundle-path U:\CERT\BOSCH-CA-DE.pem --context stackPrefix=GTK-Prod --context action=start
 * cdk synth -v --ca-bundle-path U:\CERT\BOSCH-CA-DE.pem --context stackPrefix=GTK-Prod --context action=stop
 * cdk deploy -v --ca-bundle-path U:\CERT\BOSCH-CA-DE.pem --context stackPrefix=GTK-Prod --context action=start
 * cdk deploy -v --ca-bundle-path U:\CERT\BOSCH-CA-DE.pem --context stackPrefix=GTK-Prod --context action=stop

