node('28test') {

    stage('build') {
        checkout([$class: 'SubversionSCM', additionalCredentials: [], excludedCommitMessages: '', excludedRegions: '', excludedRevprop: '', excludedUsers: '', filterChangelog: false, ignoreDirPropChanges: false, includedRegions: '', locations: [[cancelProcessOnExternalsFail: true, credentialsId: '1f9e7cc3-d484-4dea-bdd7-b1ed30e595d3', depthOption: 'infinity', ignoreExternalsOption: true, local: '.', remote: 'http://114.55.64.147/svn/rbm/trunk']], quietOperation: true, workspaceUpdater: [$class: 'UpdateUpdater']])

        sh 'mvn clean'
        sh 'mvn package -Dmaven.test.skip=true'

        dir('console-website') {
            sh 'npm install'
            sh 'ng build --configuration=test; cd dist/; zip -r ../rbm_test_client.zip ./'
        }

        dir('api-docs') {
            sh 'find . -type d -name ".svn"|xargs rm -rf'
            sh 'zip -r api-docs.zip . -x \'.svn\''
        }

        archiveArtifacts 'console-webapp/target/*.jar'
        archiveArtifacts 'file-handler-webapp/target/*.jar'
        archiveArtifacts 'api-webapp/target/*.jar'
        archiveArtifacts 'api-docs/api-docs.zip'
        archiveArtifacts 'console-website/rbm_test_client.zip'
    }
}