def deploy(env) {
    copyArtifacts(projectName: "${env.buildProjectName}")
    copyArtifacts(projectName: "${env.buildScriptsProjectName}")

    stage('prepare aspectjweaver jar') {
        if (!fileExists(libDirInServer)) {
            sh "sudo mkdir -p ${env.libDirInServer}"
            sh "sudo chown -R xkwx.xkwx ${env.libDirInServer}"
        }
        if (!fileExists("${env.libDirInServer}/${env.aspectjweaverJarName}")) {
            sh "cp ${env.libDirInJenkins}/${env.aspectjweaverJarName} ${env.libDirInServer}"
        }
    }

    stage('prepare scripts') {
        if (!fileExists(env.scriptLocalDir)) {
            sh "mkdir -p ${env.scriptLocalDir}"
        }
        sh "cp -r ${env.scriptPath} ${env.scriptLocalDir}"
        sh "chmod +x $scriptLocalDir/*.sh"
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
        sh "$scriptLocalDir/stopJarServer.sh $appDir"
    }

    stage('prepare artifacts') {
        sh "rm -rf ${env.appDir}/*"
        sh "cp $artifact ${env.appDir}/"
    }

    stage('deploy') {
        withEnv(['JENKINS_NODE_COOKIE=dontkillme']) {
            sh "$scriptLocalDir/startJarServer.sh ${env.appDir} ${env.artifactName} ${env.env} &"
        }
    }
}