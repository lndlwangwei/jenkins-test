def buildProjectName = 'rbm-console-build'
//def appDir = ''

node('rbmpl') {
    stage('copy artifacts') {
//        copyArtifacts(projectName: "${buildProjectName}")
//
//        sh "cp console-webapp/target/*.war /data/apps/test"
        sh "echo 'hello rbmpl' > /data/apps/test"
    }
}