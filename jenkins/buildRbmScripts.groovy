def nodeName = currentBuild.projectName.find(/\(([^\)]+)\)/, {group -> group[1]})

node(nodeName) {
    stage('prepare scripts') {
        git 'https://github.com/lndlwangwei/jenkins-test.git'
        archiveArtifacts 'jenkins/**/*'
    }
}