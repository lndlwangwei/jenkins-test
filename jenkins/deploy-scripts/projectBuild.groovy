build(env1Var) {
    println "inner params: ${env1Var}"

    stage('build') {
        checkout([$class: 'SubversionSCM', additionalCredentials: [], excludedCommitMessages: '', excludedRegions: '', excludedRevprop: '', excludedUsers: '', filterChangelog: false, ignoreDirPropChanges: false, includedRegions: '', locations: [[cancelProcessOnExternalsFail: true, credentialsId: '1f9e7cc3-d484-4dea-bdd7-b1ed30e595d3', depthOption: 'infinity', ignoreExternalsOption: true, local: '.', remote: 'http://114.55.64.147/svn/rbm/trunk']], quietOperation: true, workspaceUpdater: [$class: 'UpdateUpdater']])

        sh 'mvn clean'
        sh "mvn install -Dmaven.test.skip=true -P ${env1Var}"

        dir('console-website') {
            sh 'npm install'
            sh "ng build --configuration=${env1Var}; cd dist/; zip -r ../rbm_${env1Var}_client.zip ./"
        }

        dir('api-docs') {
            sh 'zip -r api-docs.zip .'
        }

        archiveArtifacts 'console-webapp/target/*.war'
        archiveArtifacts 'file-handler-webapp/target/*.war'
        archiveArtifacts 'api-webapp/target/*.war'
        archiveArtifacts 'api-docs/api-docs.zip'
        archiveArtifacts "console-website/rbm_${env1Var}_client.zip"
    }
}

return this