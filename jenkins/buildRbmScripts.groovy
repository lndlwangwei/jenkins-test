echo "env props: "
echo "${env.WORKSPACE}, ${env.BUILD_ID}, ${env.BUILD_DISPLAY_NAME}"

def nodeMatcher = currentBuild.projectName=~/\(([^\)]+)\)/
nodeMatcher.find()
def nodeName = nodeMatcher.group(1)
println nodeName

def deploy

node(nodeName) {
    git 'https://github.com/lndlwangwei/jenkins-test.git'
    deploy = load 'jenkins/test1.groovy'

    def params = [a: 'wangwei', b: '20']
    deploy.execute(params)
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