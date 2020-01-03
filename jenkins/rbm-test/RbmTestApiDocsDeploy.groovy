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
        sh "rm -f ${appDir}/${artifactName}"
    }

    stage('replace domain') {
        sh "sed -i \'s/localhost/wangwei/\' ${appDir}/lib/scripts.js"
    }
}