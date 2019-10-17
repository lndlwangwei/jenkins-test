def buildProjectName = 'rbm-build'
def appDir = '/data/apps/fh_pilot_server'
def tempAppDir = ''
def artifact = 'file-handler-webapp/target/rbm-file-handler-pilotrun.war'
def scriptPath = 'jenkins/rbm-pilot/scripts/*'
def scriptLocalDir = "/data/jenkins/rbm-pilot/scripts"
def env = "filehandler-pilot"

node('rbmfh') {

    copyArtifacts(projectName: "${buildProjectName}")

    stage('prepare artifacts') {
        sh "rm -rf ${appDir}/webapps/ROOT/*"
        sh "cp ${artifact} ${appDir}/webapps/ROOT/"
    }

    stage('prepare scripts') {
        if (!fileExists(scriptLocalDir)) {
            sh "mkdir -p $scriptLocalDir"
        }
        sh "cp -r $scriptPath $scriptLocalDir"
        sh "chmod +x $scriptLocalDir/*.sh"
    }

    stage('deploy temp server') {

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