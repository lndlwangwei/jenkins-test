def buildProjectName = 'rbm-build-prod'
def appDir = '/data/apps/rbm_client'
def artifactName = 'rbm_prod_client.zip'

node('rbm') {
    stage('deploy') {
        copyArtifacts(projectName: "${buildProjectName}")

        sh "rm -rf ${appDir}/*"
        sh "cp console-website/${artifactName} ${appDir}"
        sh "unzip ${appDir}/${artifactName} -d ${appDir}"
        sh "rm -f ${appDir}/${artifactName}"
    }

}