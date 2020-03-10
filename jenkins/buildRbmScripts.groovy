echo "env props: "
echo "${env.WORKSPACE}, ${env.BUILD_ID}, ${env.BUILD_DISPLAY_NAME}"
echo "${currentBuild.projectName}"



node('159test') {
    def params = [a: "wangwei", b: "15"]
    def deploy = load '/jenkins/test.groovy'
//    deploy.execute(params)

    stage('prepare scripts') {
        git 'https://github.com/lndlwangwei/jenkins-test.git'

        archiveArtifacts 'jenkins/**/*'
    }
}