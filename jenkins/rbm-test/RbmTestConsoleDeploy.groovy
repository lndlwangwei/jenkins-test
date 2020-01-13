def buildProjectName = 'rbm-build-test'
def buildScriptsProjectName = 'rbm-build-scripts'
def appDir = '/home/data/apps/rbm_server'
def artifact = 'console-webapp/target/xkw-rbm-console-webapp-1.0-SNAPSHOT.jar'
def artifactName = 'xkw-rbm-console-webapp-1.0-SNAPSHOT.jar'
def scriptPath = 'jenkins/rbm-test/scripts/*'
def scriptLocalDir = "/home/data/jenkins/rbm/scripts"
def libDirInJenkins = "jenkins/lib/"
def libDirInServer = "/data/lib"
def aspectjweaverJarName = "aspectjweaver-1.9.5.jar"
def env = 'test'

node('37test') {
    copyArtifacts(projectName: "${buildProjectName}")
    copyArtifacts(projectName: "${buildScriptsProjectName}")

    stage('prepare aspectjweaver jar') {
        if (!fileExists(libDirInServer)) {
            sh "sudo mkdir -p ${libDirInServer}"
            sh "sudo chown -R xkwx.xkwx ${libDirInServer}"
        }
        if (!fileExists("${libDir}/${aspectjweaverJarName}")) {
            sh "${libDirInJenkins}/${aspectjweaverJarName} ${libDirInServer}"
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