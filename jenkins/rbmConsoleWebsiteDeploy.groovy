def nodeName = currentBuild.projectName.find(/\(([^\)]+)\)/, {group -> group[1]})
def projectName = currentBuild.projectName.split('\\(')[0]

def allEnvProps = [
    'rbm-pilot-client': [
        buildProjectName: 'rbm-build-pilot',
        appDir: '/data/apps/rbm_client',
        artifactName: 'rbm_pilotrun_client.zip',
    ],
    'rbm-prod-client': [
        buildProjectName: 'rbm-build-prod',
        appDir: '/data/apps/rbm_client',
        artifactName: 'rbm_prod_client.zip',
    ],
    'rbm-test-client': [
        buildProjectName: 'rbm-build-test',
        appDir: '/data/apps/rbm_client',
        artifactName: 'rbm_test_client.zip',
    ]
]