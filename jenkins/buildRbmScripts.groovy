//def nodeName = currentBuild.projectName.find(/\(([^\)]+)\)/, {group -> group[1]})

node('159test') {
//    git 'https://github.com/lndlwangwei/jenkins-test.git'
//    deploy = load 'jenkins/test1.groovy'

    stage('prepare scripts') {
        git 'https://github.com/lndlwangwei/jenkins-test.git'
        archiveArtifacts 'jenkins/**/*'
    }
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