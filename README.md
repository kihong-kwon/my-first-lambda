## Requirements
* aws cli
* docker
* java8
* npm
* serveless + serverless-localstack plugin
* maven

## Technology Stack
* Spring Cloud Function
* JUnit5 + localstack-utils
* LocalStack
* AWS CloudFormation
* AWS Lambda
* DynamoDB
* S3

## Deploy
* severless deploy --stage=production (deploy on aws)
* severless deploy --stage=local (deploy on localstack)

## Reference
* https://serverless.com/framework/docs/providers/
* https://github.com/localstack/localstack