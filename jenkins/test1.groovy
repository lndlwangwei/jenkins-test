def execute(params) {
    println "hello"
    println "${params.a}, ${params.b}"

    stage('prepare scripts') {
        println 'prepare scripts'

        git 'https://github.com/lndlwangwei/jenkins-test.git'

        archiveArtifacts 'jenkins/**/*'
    }
}

return this;