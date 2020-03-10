echo "env props: "
echo "${env.WORKSPACE}, ${env.BUILD_ID}"

node('159test') {
    stage('prepare scripts') {
        git 'https://github.com/lndlwangwei/jenkins-test.git'

        archiveArtifacts 'jenkins/**/*'
    }
}