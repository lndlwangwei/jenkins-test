def buildProjectName = 'rbm-build'
def appDir = '/data/apps/fh_pilot_server'
def artifact = 'file-handler-webapp/target/rbm-file-handler-pilotrun.war'
def scriptPath = 'scripts/rbm-pilot/*'
def scriptLocalDir = "/data/jenkins/rbm-pilot/scripts"

node('rbmfh') {

    git 'https://github.com/lndlwangwei/jenkins-test.git'

    stage('prepare artifacts') {
        copyArtifacts(projectName: "${buildProjectName}")

        sh "rm -rf ${appDir}/webapps/ROOT/*"
        sh "cp ${artifact} ${appDir}/webapps/ROOT/"
    }

    stage('prepare scripts') {
        copyArtifacts(projectName: "${buildProjectName}")
        if (!fileExists(scriptLocalDir)) {
            sh "mkdir -p $scriptLocalDir"
        }
        cp "$scriptPath $scriptLocalDir"
        sh "chmod +x $scriptDir/*.sh"
    }

    stage('stop server') {
        sh "$scriptDir/jetty-rbm-filehandler.sh stop"
    }

    stage('deploy') {
        sh "$scriptDir/unzip.sh filehandler"
        withEnv(['JENKINS_NODE_COOKIE=dontkillme']) {
            sh "$scriptDir/jetty-rbm-filehandler.sh start"
        }
    }
}