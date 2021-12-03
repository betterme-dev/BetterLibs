#!groovy
import groovy.json.JsonOutput

def notifySlack(text, attachments) {
    //#android-engineering
    def slackURL = 'https://hooks.slack.com/services/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'

    def payload = JsonOutput.toJson([text       : text,
                                     username   : "JenkinsCI",
                                     attachments: attachments
    ])

    sh "curl -X POST --data-urlencode \'payload=${payload}\' ${slackURL}"
}

node('android') {
    try {
        stage('Checkout') {
            checkout scm
        }
        stage('Deploy library') {
            sh("sh ./gradlew publish")
        }

        stage('Notify') {
            notifySlack("", [
                    [
                            title     : "🏋🏻‍♀️️ ${env.JOB_NAME}, build #${env.BUILD_NUMBER}",
                            title_link: "${env.BUILD_URL}",
                            color     : "good",
                            fields    : [
                                    [
                                            title: "New version is ready:",
                                            value: "Tag release version! ⚡️⚡️⚡️",
                                            short: false
                                    ],
                            ]
                    ]
            ])
        }
    } catch (Exception e) {
        notifySlack("", [
                [
                        title     : "🏋🏻‍♀️️ ${env.JOB_NAME}, build #${env.BUILD_NUMBER}",
                        title_link: "${env.BUILD_URL}",
                        color     : "danger",
                        fields    : [
                                [
                                        title: "🤦🏻 ️Failed to deploy new version!",
                                        value: "${e}",
                                        short: false
                                ],
                        ]
                ]
        ])
        throw e
    }
}