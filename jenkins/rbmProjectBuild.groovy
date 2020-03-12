def params = currentBuild.projectName.find(/\(([^\)]+)\)/, {group -> group[1]})
def nodeName = params.split(',')[0].trim()
// env: test,pilotrun,product
def rbmEnv = params.split(',')[1].trim()

println "nodeName: ${nodeName}, env: $rbmEnv"

node(nodeName) {
    git 'https://github.com/lndlwangwei/jenkins-test.git'
    def build = load 'jenkins/deploy-scripts/projectBuild.groovy'
//
//    def envProps = [env: rbmEnv]
//    build.build(envProps)
}