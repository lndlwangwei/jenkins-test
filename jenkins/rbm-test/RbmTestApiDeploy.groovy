def buildProjectName = 'rbm-build-test'
def buildScriptsProjectName = 'rbm-build-scripts'
def scriptPath = 'jenkins/rbm-test/scripts/*'
def scriptLocalDir = "/data/jenkins/rbm/scripts"

def env = 'test'
def appDir = '/data/apps/rbs_server'
def artifact = 'api-webapp/target/xkw-rbm-api-webapp-1.0-SNAPSHOT.jar'
def artifactName = 'xkw-rbm-api-webapp-1.0-SNAPSHOT.jar'
def libDirInJenkins = "jenkins/lib"
def libDirInServer = "/data/lib"
def aspectjweaverJarName = "aspectjweaver-1.9.5.jar"

node('159test') {
    copyArtifacts(projectName: "${buildProjectName}")
    copyArtifacts(projectName: "${buildScriptsProjectName}")

    stage('prepare appDir') {
        if (!fileExists("${appDir}")) {
            sh "sudo mkdir -p ${appDir}"
            sh "sudo chown -R xkwx.xkwx ${appDir}"
        }
    }

    stage('prepare aspectjweaver jar') {
        if (!fileExists(libDirInServer)) {
            sh "sudo mkdir -p ${libDirInServer}"
            sh "sudo chown -R xkwx.xkwx ${libDirInServer}"
        }
        if (!fileExists("${libDirInServer}/${aspectjweaverJarName}")) {
            sh "cp ${libDirInJenkins}/${aspectjweaverJarName} ${libDirInServer}"
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