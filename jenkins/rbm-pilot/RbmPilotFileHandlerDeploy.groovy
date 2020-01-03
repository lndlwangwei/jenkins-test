def buildProjectName = 'rbm-build-pilot'
def buildScriptsProjectName = 'rbm-build-scripts'
def appDir = '/data/apps/fh_pilot_server'
def artifact = 'file-handler-webapp/target/xkw-rbm-file-handler-webapp-1.0-SNAPSHOT.jar'
def artifactName = 'xkw-rbm-file-handler-webapp-1.0-SNAPSHOT.jar'
def serverPort = 8086
def scriptPath = 'jenkins/rbm-pilot/scripts/*'
def scriptLocalDir = "/data/jenkins/rbm/scripts"
def env = "pilotrun"

node('rbmfh') {
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

            if (!fileExists("${appDir}.bak")) {
                sh "mkdir -p ${appDir}.bak"
            }

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