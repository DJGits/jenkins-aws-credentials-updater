def Branch = 'master'
folder('AWS')
pipelineJob('AWS/RotateAccessKeys') {  
   triggers {
     cron('0 23 * * *') //rotate AWS Keys every night at 11PM
   }
   parameters {
      stringParam (
                    'JenkinsCredentialsID',
                    'aws_profile_acct_123456',
                    'Enter the Jenkins Credentials ID'
      )
   }
   definition {
    cpsScm {
      scm {
        git {
            remote {
                url('git@github.com:DJGits/jenkins-aws-credentials-updater.git')
            }
            branch(Branch)
            extensions {
                cleanBeforeCheckout()
            }
        }
      }
      scriptPath("aws_credentials_updater.jenkinsfile")
    }
  }
 }