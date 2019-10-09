def buildProjectName = 'rbm-build'
def appDir = '/home/data/apps/rbm_server'
def artifact = 'console-webapp/target/rbm-console-pilotrun.war'

node('rbmpl') {
    def scriptDir = "$WORKSPACE/jenkins/rbm-pilot/scripts"
    git 'https://github.com/lndlwangwei/jenkins-test.git'

    stage('prepare artifacts') {
        copyArtifacts(projectName: "${buildProjectName}")

        sh "rm -rf ${appDir}/webapps/ROOT/*"
        sh "cp console-webapp/target/rbm-console-test.war ${appDir}/webapps/ROOT/"
    }

    stage('prepare scripts') {
        sh "chmod +x $scriptDir/*.sh"
    }

    stage('stop server') {
        sh "$scriptDir/jetty-rbm-console.sh stop"
    }

    stage('deploy') {
        sh "$scriptDir/unzip.sh"
        withEnv(['JENKINS_NODE_COOKIE=dontkillme']) {
            sh "$scriptDir/jetty-rbm-console.sh start > /dev/null"
        }
    }
}