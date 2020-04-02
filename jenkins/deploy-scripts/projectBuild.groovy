def build(env) {
    println "inner params: ${env}"

    stage('build') {
        // checkout([$class: 'SubversionSCM', additionalCredentials: [], excludedCommitMessages: '', excludedRegions: '', excludedRevprop: '', excludedUsers: '', filterChangelog: false, ignoreDirPropChanges: false, includedRegions: '', locations: [[cancelProcessOnExternalsFail: true, credentialsId: 'svn_credential', depthOption: 'infinity', ignoreExternalsOption: true, local: '.', remote: 'http://114.55.64.147/svn/rbm/trunk']], quietOperation: true, workspaceUpdater: [$class: 'UpdateUpdater']])

        sh 'mvn clean'
        sh "mvn install -Dmaven.test.skip=true -P ${env}"

        dir('console-website') {
//            sh 'npm --registry https://registry.npm.taobao.org install'
            sh "ng build --configuration=${env}; cd dist/; zip -r ../rbm_${env}_client.zip ./"
        }

        dir('api-docs') {
            sh 'zip -r api-docs.zip .'
        }

        archiveArtifacts 'eureka-server/target/*.jar'
        archiveArtifacts 'rbm-gateway/target/*.jar'
        archiveArtifacts 'console-webapp/target/*.jar'
        archiveArtifacts 'file-handler-webapp/target/*.jar'
        archiveArtifacts 'api-webapp/target/*.jar'
        archiveArtifacts 'api-docs/api-docs.zip'
        archiveArtifacts "console-website/rbm_${env}_client.zip"
    }
}

return this