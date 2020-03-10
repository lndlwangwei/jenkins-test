def execute() {
    println "hello"

    stage('prepare scripts') {
        println 'prepare scripts'

        git 'https://github.com/lndlwangwei/jenkins-test.git'

        archiveArtifacts 'jenkins/**/*'
    }
}

return this;