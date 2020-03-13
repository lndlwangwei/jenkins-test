def nodeName = currentBuild.projectName.find(/\(([^\)]+)\)/, {group -> group[1]})
def projectName = currentBuild.projectName.split('\\(')[0]

def allEnvProps = [
    'rbm-pilot-client': [
        buildProjectName: 'rbm-project-build(159test,pilotrun)',
        appDir: '/data/apps/rbm_client',
        artifactName: 'rbm_pilotrun_client.zip',
    ],
    'rbm-prod-client': [
        buildProjectName: 'rbm-project-build(159test,product)',
        appDir: '/data/apps/rbm_client',
        artifactName: 'rbm_product_client.zip',
    ],
    'rbm-test-client': [
        buildProjectName: 'rbm-project-build(159test,test)',
        appDir: '/data/apps/rbm_client',
        artifactName: 'rbm_test_client.zip',
    ]
]

node(nodeName) {
    git 'https://github.com/lndlwangwei/jenkins-test.git'
    def deploy = load 'jenkins/deploy-scripts/consoleWebSiteDeploy.groovy'
    deploy.deploy(envProp)
}