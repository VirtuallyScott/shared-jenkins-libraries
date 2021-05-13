# Leverages Slack Notification Plugin
https://plugins.jenkins.io/slack/

***

# Assumptions
## Slack
- Slack Workspace
- Slack Token for bot user
    - https://my.slack.com/services/new/jenkins-ci
    - Create Jenkins Credential at Global or Project Level
- Channel to post to
-

## O365

***


# Configure Shared Libraries
1. Log into Jenkins >
2. Manage Jenkins >
3. Configure System >
4. Global Pipeline Libraries >
5. Configure
    1. Name Library "shared-libraries" >
    2. Default Version "main" >
    3. Retrieval Method, Modern SCM >
    4. Git Source >
    4. Credentials
    6. Repository URL (https)
    7. Save
6. Upon first execution of script including libraries, Script Approval may be required
    1. Log into Jenkins >
    2. Manage Jenkins >
    3. Under "Security", In-Process Script Approval
    4. Approve Script
    5. Save

# usage

Include the shared library in Jenkinsfile
       
# usage for slackNotifier
1. Set env variable globally or set in each Jenkinsfile

```
// import shared libraries
@Library('shared-libraries')_

script {
    env.slackChannel = "#slackChannel"
}

timestamps {

    try {

        node {

                stage ('prep to build') {
                    slackNotifier('STARTED')
                    cleanWs()
                    sh 'cd ${WORKSPACE}'
                    checkout scm
                }

                stage ('build and test') {
                    build ...
                    tests ...
                }

                stage ('create docker image') {
                    package and publish artifacts ...
                }

        }

    } catch(e) {
        currentBuild.result = "FAILED"
        throw e
    } finally {
        slackNotifier(currentBuild.result)
    }

}
```
     
           
               
# usage for teamsNotifier
1. Create webhook for O365
2. Login into O365 and go to https://outlook.office365.com/connectors/GroupPicker/Index?Provider=JenkinsCI&src=store
3. Copy the webhook URL and declare it as the value for env.webhookURL in your Jenkinsfile
4. Set env variable globally or set in each Jenkinsfile 

Declare env.webhookURL in your job's Jenkinsfile

```
// import shared libraries
@Library('shared-libraries')_

script {
    env.webhookURL = ""
}

timestamps {

    try {

        node {

                stage ('prep to build') {
                    teamsNotifier('STARTED')
                    cleanWs()
                    sh 'cd ${WORKSPACE}'
                    checkout scm
                }

                stage ('build and test') {
                    build ...
                }

                stage ('create docker image') {
                    package and publish artifacts ...
                }

        }

    } catch(e) {
        currentBuild.result = "FAILED"
        throw e
    } finally {
        teamsNotifier(currentBuild.result)
    }

}
```