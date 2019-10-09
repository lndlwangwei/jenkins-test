def buildProjectName = 'rbm-build'
def appDir = '/data/apps/rbm_client'
def artifactName = 'rbm_pilotrun_client.zip'

node('rbmpl') {
    stage('deploy') {
        copyArtifacts(projectName: "${buildProjectName}")

        sh "rm -rf ${appDir}/*"
        sh "cp console-website/${artifactName} ${appDir}"
        sh "unzip ${appDir}/${artifactName} -d ${appDir}"
        sh "rm -f ${appDir}/${artifactName}"
    }

}