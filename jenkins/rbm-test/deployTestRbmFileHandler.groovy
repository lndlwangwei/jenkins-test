def buildProjectName = 'rbm-build'
def appDir = '/home/data/apps/rbm-file-handler_server'
def artifact = 'file-handler-webapp/target/rbm-file-handler-test.war'

node('37test') {
    def scriptDir = "$WORKSPACE/jenkins/rbm-test/scripts"
    git 'https://github.com/lndlwangwei/jenkins-test.git'

    stage('prepare artifacts') {
        copyArtifacts(projectName: "${buildProjectName}")

        sh "rm -rf ${appDir}/webapps/ROOT/*"
        sh "cp ${artifact} ${appDir}/webapps/ROOT/"
    }

    stage('prepare scripts') {
        sh "chmod +x $scriptDir/*.sh"
    }

    stage('stop server') {
        sh "$scriptDir/jetty-rbm-filehandler.sh stop"
    }

    stage('deploy') {
        sh "$scriptDir/unzip.sh filehandler"
//        withEnv(['JENKINS_NODE_COOKIE=dontkillme']) {
//            sh "$scriptDir/jetty-rbm-filehandler.sh start > /dev/null"
//        }
    }
}