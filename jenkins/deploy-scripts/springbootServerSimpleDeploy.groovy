// 项目服务端的部署，如console，api等服务
def deploy(env) {
    copyArtifacts(projectName: "${env.buildProjectName}")
    copyArtifacts(projectName: "${env.buildScriptsProjectName}")

    stage('prepare appDir') {
        if (!fileExists("${env.appDir}")) {
            sh "sudo mkdir -p ${env.appDir}"
            sh "sudo chown -R xkwx.xkwx ${env.appDir}"
        }
    }

    if (env.libDirInServer != null) {
        stage('prepare aspectjweaver jar') {
            if (!fileExists(env.libDirInServer)) {
                sh "sudo mkdir -p ${env.libDirInServer}"
                sh "sudo chown -R xkwx.xkwx ${env.libDirInServer}"
            }
            if (!fileExists("${env.libDirInServer}/${env.aspectjweaverJarName}")) {
                sh "cp ${env.libDirInJenkins}/${env.aspectjweaverJarName} ${env.libDirInServer}"
            }
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
            sh "${env.scriptLocalDir}/startJarServer.sh ${env.appDir} ${env.artifactName} ${env.env} ${env.libDirInServer}/${env.aspectjweaverJarName} &"
        }
    }
}

return this