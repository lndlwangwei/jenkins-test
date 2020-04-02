def nodeName = currentBuild.projectName.find(/\(([^\)]+)\)/, {group -> group[1]})
def projectName = currentBuild.projectName.split('\\(')[0]

def allEnvProps = [
    'rbm-pilot-api-docs': [
        buildProjectName: 'rbm-project-build(159test,pilot)',
        appDir: '/data/apps/rbs_client',
        artifactName: 'api-docs.zip',
        domain: 'rbm-pilot.xkw.com'
    ],
    'rbm-prod-api-docs': [
        buildProjectName: 'rbm-project-build(159test,product)',
        appDir: '/data/apps/rbs_client',
        artifactName: 'api-docs.zip',
        domain: 'rbm.xkw.com'
    ],
    'rbm-test-api-docs': [
        buildProjectName: 'rbm-project-build(159test,test)',
        appDir: '/data/apps/rbs_client',
        artifactName: 'api-docs.zip',
        domain: '10.1.23.159:8087'
    ]
]

def envProp = allEnvProps[projectName]

node(nodeName) {
    git 'https://github.com/lndlwangwei/jenkins-test.git'
    def deploy = load 'jenkins/deploy-scripts/apiSwaggerClientDeploy.groovy'
    deploy.deploy(envProp)
}