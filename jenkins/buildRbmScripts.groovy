echo "env props: "
echo "${env.WORKSPACE}, ${env.BUILD_ID}, ${env.BUILD_DISPLAY_NAME}"
echo "${currentBuild.projectName}"

def params = [a: "wangwei", b: "15"]
def deploy = load "testLoadFunction.groovy"
deploy.execute(params)

node('159test') {
    stage('prepare scripts') {
        git 'https://github.com/lndlwangwei/jenkins-test.git'

        archiveArtifacts 'jenkins/**/*'
    }
}