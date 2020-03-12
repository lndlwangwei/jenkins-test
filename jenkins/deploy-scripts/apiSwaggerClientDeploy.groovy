// swagger前端的部署
def deploy(env) {
    stage('deploy') {
        copyArtifacts(projectName: "${env.buildProjectName}")

        if (!fileExists(env.appDir)) {
            sh "mkdir -p ${env.appDir}"
        }
        sh "rm -rf ${env.appDir}/*"
        sh "cp api-docs/${env.artifactName} ${env.appDir}"
        sh "unzip -o ${env.appDir}/${env.artifactName} -d ${env.appDir}"
        sh "rm -f ${env.appDir}/${env.artifactName}"
    }

    env.domain = env.domain.replace('\\.', '\\\\.')
    stage('replace domain') {
        sh "sed -i \'s/http:\\/\\/localhost:8087/http:\\/\\/${env.domain}/\' ${env.appDir}/lib/scripts.js"
    }
}

return this