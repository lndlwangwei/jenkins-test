def buildProjectName = 'rbm-build-test'
def appDir = '/home/data/apps/rbs_client'
def artifactName = 'api-docs.zip'

node('37test') {
    stage('deploy') {
        copyArtifacts(projectName: "${buildProjectName}")

        if (!fileExists(appDir)) {
            sh "mkdir -p ${appDir}"
        }
        sh "rm -rf ${appDir}/*"
        sh "cp api-docs/${artifactName} ${appDir}"
        sh "unzip ${appDir}/${artifactName} -d ${appDir}"
        sh "sed -i \'s/localhost:8087/wangwei:9002/\' ${appDir}/lib/scripts.js"
        sh "rm -f ${appDir}/${artifactName}"
    }

}