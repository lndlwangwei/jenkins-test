def buildProjectName = 'rbm-build'
def appDir = '/home/data/apps/rbm_client'
def artifactName = 'rbm_test_client.zip'

node('37test') {
    stage('deploy') {
        copyArtifacts(projectName: "${buildProjectName}")

        sh "rm -rf ${appDir}/*"
        sh "cp console-website/${artifactName} ${appDir}"
        sh "unzip ${appDir}/${artifactName}"
        sh "rm -f ${appDir}/${artifactName}"
    }

}