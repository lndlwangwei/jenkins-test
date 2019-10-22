def buildProjectName = 'rbm-build-pilot'
def sqlScriptName = 'rbm_update.sql'
def sqlScriptLocalDir = "/data/jenkins/rbm/sql"

node('rbmpl') {

    stage('prepare sql script') {
        if (!fileExists(sqlScriptLocalDir)) {
            sh "mkdir -p $sqlScriptLocalDir"
        }
        sh "cp sql/$sqlScriptName $sqlScriptLocalDir"
    }

    stage('apply sql') {
        sh "mysql -uxkw -uxkw.com1QAZ < $sqlScriptLocalDir/$sqlScriptName"
    }
}