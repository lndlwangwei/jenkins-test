def buildProjectName = 'rbm-build-test'
def buildScriptsProjectName = 'rbm-build-scripts'
def appDir = '/home/data/apps/rbm_server'
def artifact = 'console-webapp/target/xkw-rbm-console-webapp-1.0-SNAPSHOT.jar'
def artifactName = 'xkw-rbm-console-webapp-1.0-SNAPSHOT.jar'
def serverPort = 8085
def scriptPath = 'jenkins/rbm-test/scripts/*'
def scriptLocalDir = "/home/data/jenkins/rbm/scripts"
def env = 'test'

node('37test') {
    copyArtifacts(projectName: "${buildProjectName}")
    copyArtifacts(projectName: "${buildScriptsProjectName}")

    stage('prepare scripts') {
        if (!fileExists(scriptLocalDir)) {
            sh "mkdir -p $scriptLocalDir"
        }
        sh "cp -r $scriptPath $scriptLocalDir"
        sh "chmod +x $scriptLocalDir/*.sh"
    }

    stage('backup old artifact') {
        if (fileExists("${appDir}/${artifactName}")) {
            sh "rm -rf ${appDir}.bak/*"
            sh "cp ${appDir}/${artifactName} ${appDir}.bak"
        }
    }

    stage('stop server') {
        sh "$scriptLocalDir/stopJarServer.sh $appDir"
    }

    stage('prepare artifacts') {
        sh "rm -rf ${appDir}/*"
        sh "cp $artifact ${appDir}/"
    }

    stage('deploy') {
        withEnv(['JENKINS_NODE_COOKIE=dontkillme']) {
            sh "$scriptLocalDir/startJarServer.sh ${appDir} ${artifactName} ${serverPort} ${env} &"
        }
    }
}