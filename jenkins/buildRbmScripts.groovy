echo "env props: "
echo env

node('159test') {
    stage('prepare scripts') {
        git 'https://github.com/lndlwangwei/jenkins-test.git'

        archiveArtifacts 'jenkins/**/*'
    }
}