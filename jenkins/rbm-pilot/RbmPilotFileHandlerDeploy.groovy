def buildProjectName = 'rbm-build'
def appDir = '/data/apps/fh_pilot_server'
def artifact = 'file-handler-webapp/target/rbm-file-handler-pilotrun.war'
def scriptPath = 'jenkins/rbm-pilot/scripts/*'
def scriptLocalDir = "/data/jenkins/rbm-pilot/scripts"
def env = "pilot"

node('rbmfh') {

    //git 'https://github.com/lndlwangwei/jenkins-test.git'
    copyArtifacts(projectName: "${buildProjectName}")

    stage('prepare artifacts') {
        sh "rm -rf ${appDir}/webapps/ROOT/*"
        sh "cp ${artifact} ${appDir}/webapps/ROOT/"
    }

    stage('prepare scripts') {
//        copyArtifacts(projectName: "${buildProjectName}")
        //if (!fileExists(scriptLocalDir)) {
        sh "mkdir -p $scriptLocalDir"
        //}
        sh "cp -r $scriptPath $scriptLocalDir"
        sh "chmod +x $scriptLocalDir/*.sh"
    }

    stage('stop server') {
        sh "$scriptLocalDir/jetty.sh stop $env"
    }

    stage('deploy') {
        sh "$scriptLocalDir/unzip.sh filehandler"
        withEnv(['JENKINS_NODE_COOKIE=dontkillme']) {
            sh "$scriptLocalDir/jetty.sh start $env"
        }
    }
}