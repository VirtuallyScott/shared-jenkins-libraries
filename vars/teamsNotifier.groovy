def call(String buildStatus = 'STARTED') {
    buildStatus = buildStatus ?: 'SUCCESS'
    def colorCode = '#FF0000'
    def jobName = "${env.JOB_NAME}".replaceAll("%2F", "/")
    def subject = "${buildStatus}: *${jobName} [Build #${env.BUILD_NUMBER}]*"
    def links = "Links: <${env.BUILD_URL}|Build> | <${env.BUILD_URL}console|Console> | <${env.BUILD_URL}changes|Changes>"
    if (env.CHANGE_URL != null) {
        links = links + " | <${env.CHANGE_URL}|Pull Request>"
    }
    def summary = "${subject}\n${links}"

    if (buildStatus == 'STARTED') {
        colorCode = '#FFFF00'
        summary = "⏳ " + summary
    } else if (buildStatus == 'SUCCESS') {
        colorCode = '#00FF00'
        summary = "✅ " + summary
    } else {
        colorCode = '#FF0000'
        summary = "❌ " + summary
    }

    office365ConnectorSend color: colorCode, message: summary, webhookUrl: env.webhookURL

}