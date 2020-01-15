def buildProjectName = 'rbm-build-test'
def buildScriptsProjectName = 'rbm-build-scripts'
def appDir = '/data/apps/rbm-gateway'
def artifact = 'rbm-gateway/target/rbm-gateway-1.0-SNAPSHOT.jar'
def artifactName = 'rbm-gateway-1.0-SNAPSHOT.jar'
def scriptPath = 'jenkins/rbm-test/scripts/*'
def scriptLocalDir = "/data/jenkins/rbm/scripts"
def env = 'test'

node('195test') {
    copyArtifacts(projectName: "${buildProjectName}")
    copyArtifacts(projectName: "${buildScriptsProjectName}")

    stage('prepare appDir') {
        if (!fileExists("${appDir}")) {
            sh "sudo mkdir -p ${appDir}"
            sh "sudo chown -R wangwei.wangwei ${appDir}"
        }
    }

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
            sh "$scriptLocalDir/startJarServer.sh ${appDir} ${artifactName} ${env} &"
        }
    }
}