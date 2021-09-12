# Jenkins - AWS Credentials Updater

Everyone loves Jenkins and so do I! If you are using jenkins plugin for storing AWS access keys then here is a solution to rotate the keys automatically without human intervention.



## Introduction

The first computer password was developed in 1961 by Dr. Fernando Corbató while working at Massachusetts Institute of Technology (MIT) for an operating system for 
computers called the Compatible Time-Sharing System (CTSS). Interestingly the system was defeated in just one year by a Ph.D. researcher Allan Scherr in 1962 by printing the password file.

Fast forward today, the concern for passwords and API keys have not changed at all. We still interact with systems using passwords and API keys. Though the password vault solutions address some of these pain points, not many have the privilege to vault solutions in their environment and certain use cases they do not have capability to rotate.

In the DevOps world, we are giving more power & privileges to the automation tools. Jenkins is one of the widely used & versatile automation tools. If you are using CloudBees AWS Credentials plugin then here is a simple Jenkins pipeline which I developed to rotate the AWS access keys without involving any manual process. The job can be 
configured to run as often as you want to rotate depending on your risk posture or the policy document. 

## Requirements

### Jenkins Plugins

| Name | URL | Description |
| ---------- | :---------- | :---------- |
| **Job DSL** | [https://plugins.jenkins.io/job-dsl/](https://plugins.jenkins.io/job-dsl/) | This plugin allows Jobs and Views to be defined via DSLs|
| **Pipeline Utility Steps** | [https://plugins.jenkins.io/pipeline-utility-steps/](https://plugins.jenkins.io/pipeline-utility-steps/) | Small, miscellaneous, cross platform utility steps for Jenkins Pipeline jobs.|
| **CloudBees AWS Credentials** | [https://plugins.jenkins.io/aws-credentials/](https://plugins.jenkins.io/aws-credentials/) | Allows storing Amazon IAM credentials within the Jenkins Credentials API. Store Amazon IAM access keys (AWSAccessKeyId and AWSSecretKey) within the Jenkins Credentials API. Also support IAM Roles and IAM MFA Token.|

### Jenkins - In-process Script Approval

Below actions needs to be approved as part of in-process script approval. 

+ method com.cloudbees.jenkins.plugins.awscredentials.AWSCredentialsImpl getAccessKey
+ method com.cloudbees.jenkins.plugins.awscredentials.AWSCredentialsImpl getSecretKey
+ method com.cloudbees.plugins.credentials.Credentials getScope
+ method com.cloudbees.plugins.credentials.CredentialsStore updateCredentials com.cloudbees.plugins.credentials.domains.Domain com.cloudbees.plugins.credentials.Credentials + com.cloudbees.plugins.credentials.Credentials
+ method com.cloudbees.plugins.credentials.SystemCredentialsProvider getStore
+ method com.cloudbees.plugins.credentials.common.IdCredentials getId
+ method com.cloudbees.plugins.credentials.common.StandardCredentials getDescription
+ method jenkins.model.Jenkins getExtensionList java.lang.String
+ new com.cloudbees.jenkins.plugins.awscredentials.AWSCredentialsImpl com.cloudbees.plugins.credentials.CredentialsScope 
+ java.lang.String java.lang.String java.lang.String java.lang.String java.lang.String java.lang.String
+ staticMethod com.cloudbees.plugins.credentials.CredentialsProvider lookupCredentials java.lang.Class hudson.model.ItemGroup
+ staticMethod com.cloudbees.plugins.credentials.domains.Domain global
+ staticMethod jenkins.model.Jenkins getInstance


### AWS IAM User

To rotate access keys in AWS the IAM user requires below permissions in AWS. Use ``Resource`` element to restrict the user to manage himself!

```json
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Sid": "LeastPrivilegesForSelfManagementOfAccesskeys",
            "Effect": "Allow",
            "Action": [
                "iam:GetUser",
                "iam:*AccessKey*"
            ],
            "Resource": "arn:aws:iam::*:user/${aws:username}"
        }
    ]
}
```

## Deployment
The jenkins job can be created manually or use the provided [groovy](aws_credentials_job.groovy) to create the jenkins pipeline.

## Bonus Tip
If you are storing AWS access keys in both jenkins credentials database & AWS credentials file, then the pipeline can be modified to update credentials file too using below AWS CLI commands.

```bash
aws configure set aws_access_key_id ${newAccessKey} --profile ${AWS_PROFILE}
aws configure set aws_secret_access_key ${newSecretKey} --profile ${AWS_PROFILE}
```

## Authors

* **Jay Durga** - *Jenkins pipeline developer* - [DJGits](https://github.com/DJGits)

## Thanks

Special thanks to [Unknown Blogger](https://www.blogger.com/profile/05007815943042361652), developer of the original [rotation script](http://blog.twimager.com/2017/08/using-jenkins-to-rotate-aws-access-key.html) rotation script
