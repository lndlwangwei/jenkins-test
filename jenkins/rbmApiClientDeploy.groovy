def nodeName = currentBuild.projectName.find(/\(([^\)]+)\)/, {group -> group[1]})
def projectName = currentBuild.projectName.split('\\(')[0]

def allEnvPropd = [
    'rbm-pilot-api-docs': [
        buildProjectName: 'rbm-build-pilot',
        appDir: '/data/apps/rbs_client',
        artifactName: 'api-docs.zip',
        domain: 'rbm-pilot.xkw.com'
    ],
    'rbm-prod-api-docs': [
        buildProjectName: 'rbm-build-prod',
        appDir: '/data/apps/rbs_client',
        artifactName: 'api-docs.zip',
        domain: 'rbm.xkw.com'
    ],
    'rbm-test-api-docs': [
        buildProjectName: 'rbm-build-test',
        appDir: '/data/apps/rbs_client',
        artifactName: 'api-docs.zip',
        domain: '10.1.23.159:8087'
    ]
]