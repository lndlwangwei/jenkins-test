node('28test') {
    stage('prepare scripts') {
        git 'https://github.com/lndlwangwei/jenkins-test.git'

        archiveArtifacts 'jenkins/**/*'
    }
}