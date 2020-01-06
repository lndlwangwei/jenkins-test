def buildProjectName = 'rbm-build-prod'
def appDir = '/data/apps/rbs_client'
def artifactName = 'api-docs.zip'

node('rbm') {
    stage('deploy') {
        copyArtifacts(projectName: "${buildProjectName}")

        if (!fileExists(appDir)) {
            sh "mkdir -p ${appDir}"
        }
        sh "rm -rf ${appDir}/*"
        sh "cp api-docs/${artifactName} ${appDir}"
        sh "unzip -o ${appDir}/${artifactName} -d ${appDir}"
        sh "rm -f ${appDir}/${artifactName}"
    }

    stage('replace domain') {
        sh "sed -i \'s/http:\\/\\/localhost:8087/http:\\/\\/rbm\\.xkw\\.com/\' ${appDir}/lib/scripts.js"
    }
}