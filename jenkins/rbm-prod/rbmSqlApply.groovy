def buildProjectName = 'rbm-build-prod'
def sqlScriptName = 'rbm_update.sql'
def sqlScriptLocalDir = "/data/jenkins/rbm/sql"

node('rbm') {

    copyArtifacts(projectName: "${buildProjectName}")

    stage('prepare sql script') {
        if (!fileExists(sqlScriptLocalDir)) {
            sh "mkdir -p $sqlScriptLocalDir"
        }
        sh "cp sql/$sqlScriptName $sqlScriptLocalDir"
    }

    stage('apply sql') {
        sh "mysql -uxkw -pxkw.com1QAZ < $sqlScriptLocalDir/$sqlScriptName"
    }
}