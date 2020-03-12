//def params = currentBuild.projectName.find(/\(([^\)]+)\)/, {group -> group[1]})
//def nodeName = params.split('-')[0].trim()
// env: test,pilotrun,product
//def env = params.split('-')[1].trim()
def env = 'test'

//println "nodeName: ${nodeName}, env: $env"

node('159test') {
    checkout([$class: 'SubversionSCM', additionalCredentials: [], excludedCommitMessages: '', excludedRegions: '', excludedRevprop: '', excludedUsers: '', filterChangelog: false, ignoreDirPropChanges: false, includedRegions: '', locations: [[cancelProcessOnExternalsFail: true, credentialsId: 'svn_credential', depthOption: 'infinity', ignoreExternalsOption: true, local: '.', remote: 'http://114.55.64.147/svn/rbm/trunk']], quietOperation: true, workspaceUpdater: [$class: 'UpdateUpdater']])
    git 'https://github.com/lndlwangwei/jenkins-test.git'


    def build = load 'jenkins/deploy-scripts/projectBuild.groovy'

//    def envProps = [env: rbmEnv]
    build.build(env)
}