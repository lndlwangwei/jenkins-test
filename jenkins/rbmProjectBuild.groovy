def params = currentBuild.projectName.find(/\(([^\)]+)\)/, {group -> group[1]})
def nodeName = params.split(',')[0]
// env: test,pilotrun,product
def env = params.split(',')[1]

println "nodeName: ${nodeName}, env: $env"

def projectName = currentBuild.projectName.split('\\(')[0]

node(nodeName) {
    git 'https://github.com/lndlwangwei/jenkins-test.git'
    def build = load 'jenkins/deploy-scripts/projectBuild.groovy'

    build.build(env)
}