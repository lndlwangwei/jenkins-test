echo "env props: "
echo "${env.WORKSPACE}, ${env.BUILD_ID}, ${env.BUILD_DISPLAY_NAME}"
echo "${currentBuild.projectName}"

def deploy

node('159test') {
    git 'https://github.com/lndlwangwei/jenkins-test.git'
    deploy = load 'jenkins/test1.groovy'

    deploy.execute()
}


//node('159test') {
//    def params = [a: "wangwei", b: "15"]
//
////    deploy.execute(params)
//
//    stage('prepare scripts') {
//
//
//        git 'https://github.com/lndlwangwei/jenkins-test.git'
//
//        archiveArtifacts 'jenkins/**/*'
//    }
//}