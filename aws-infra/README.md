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
 * cdk deploy GTK-Prod-ECR-Stack --ca-bundle-path U:\CERT\BOSCH-CA-DE.pem --context stackPrefix=GTK-Prod
 * cdk deploy GTK-Prod-VPC-Stack --ca-bundle-path U:\CERT\BOSCH-CA-DE.pem --context stackPrefix=GTK-Prod
 * cdk deploy GTK-Prod-ECS-DB-Stack --ca-bundle-path U:\CERT\BOSCH-CA-DE.pem --context stackPrefix=GTK-Prod

