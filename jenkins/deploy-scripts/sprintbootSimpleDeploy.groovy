// 用于eureka server,springcloud gateway等springcloud通用组件服务的部署
def deploy(env) {
    copyArtifacts(projectName: "${env.buildProjectName}")
    copyArtifacts(projectName: "${env.buildScriptsProjectName}")

    stage('prepare appDir') {
        if (!fileExists("${env.appDir}")) {
            sh "sudo mkdir -p ${env.appDir}"
            sh "sudo chown -R xkwx.xkwx ${env.appDir}"
        }
    }

    stage('prepare scripts') {
        if (!fileExists(env.scriptLocalDir)) {
            sh "mkdir -p ${env.scriptLocalDir}"
        }
        sh "cp -r ${env.scriptPath} ${env.scriptLocalDir}"
        sh "chmod +x ${env.scriptLocalDir}/*.sh"
    }

    stage('backup old artifact') {
        if (fileExists("${env.appDir}/${env.artifactName}")) {
            sh "rm -rf ${env.appDir}.bak/*"

            if (!fileExists("${env.appDir}.bak")) {
                sh "mkdir -p ${env.appDir}.bak"
            }

            sh "cp ${env.appDir}/${env.artifactName} ${env.appDir}.bak"
        }
    }

    stage('stop server') {
        sh "${env.scriptLocalDir}/stopJarServer.sh ${env.appDir}"
    }

    stage('prepare artifacts') {
        sh "rm -rf ${env.appDir}/*"
        sh "cp ${env.artifact} ${env.appDir}/"
    }

    stage('deploy') {
        withEnv(['JENKINS_NODE_COOKIE=dontkillme']) {
            sh "$scriptLocalDir/startJarServer.sh ${env.appDir} ${env.artifactName} ${env.env} &"
        }
    }
}

return this