def buildProjectName = 'rbm-build'
def appDir = '/data/apps/rbm_server'
def artifact = 'console-webapp/target/rbm-console-product.war'
def scriptPath = 'jenkins/rbm-prod/scripts/*'
def scriptLocalDir = "/data/jenkins/rbm/scripts"
def env = 'console'

node('rbm') {
    copyArtifacts(projectName: "${buildProjectName}")

    stage('prepare artifacts') {
        sh "rm -rf ${appDir}/webapps/ROOT/*"
        sh "cp $artifact ${appDir}/webapps/ROOT/"
    }

    stage('prepare scripts') {
        if (!fileExists(scriptLocalDir)) {
            sh "mkdir -p $scriptLocalDir"
        }
        sh "cp -r $scriptPath $scriptLocalDir"
        sh "chmod +x $scriptLocalDir/*.sh"
    }

    stage('stop server') {
        sh "$scriptLocalDir/jetty.sh stop $env"
    }

    stage('deploy') {
        sh "$scriptLocalDir/unzip.sh $env"

        withEnv(['JENKINS_NODE_COOKIE=dontkillme']) {
            sh "$scriptDir/jetty.sh start $env > /dev/null"
        }
    }
}