// console前端的部署
def deploy(env) {
    stage('deploy') {
        copyArtifacts(projectName: "${env.buildProjectName}")

        sh "rm -rf ${env.appDir}/*"
        sh "cp console-website/${env.artifactName} ${env.appDir}"
        sh "unzip ${env.appDir}/${env.artifactName} -d ${env.appDir}"
        sh "rm -f ${env.appDir}/${env.artifactName}"
    }
}

return this