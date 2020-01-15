def buildProjectName = 'rbm-build-test'
def appDir = '/data/apps/rbm_client'
def artifactName = 'rbm_test_client.zip'

node('159test') {

    stage('prepare appDir') {
        if (!fileExists("${appDir}")) {
            sh "sudo mkdir -p ${appDir}"
            sh "sudo chown -R wangwei.wangwei ${appDir}"
        }
    }

    stage('deploy') {
        copyArtifacts(projectName: "${buildProjectName}")

        sh "rm -rf ${appDir}/*"
        sh "cp console-website/${artifactName} ${appDir}"
        sh "unzip ${appDir}/${artifactName} -d ${appDir}"
        sh "rm -f ${appDir}/${artifactName}"
        sh "echo 'wangwei'"
    }

}