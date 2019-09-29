def buildProjectName = 'rbm-console-build'
def appDir = '/home/data/apps/rbm_server'
def springInstrumentJar = '/data/lib/spring-instrument-5.1.4.RELEASE.jar'
def jettyStartJar = '/opt/jetty-distribution-9.4.7.v20170914/start.jar'

node('rbmpl') {
    def scriptDir = "$WORKSPACE/jenkins/rbm-test/scripts"
    git 'https://github.com/lndlwangwei/jenkins-test.git'

    stage('deploy temp service') {

    }

    stage('prepare artifacts') {
        copyArtifacts(projectName: "${buildProjectName}")

        sh "rm -rf ${appDir}/webapps/ROOT/*"
        sh "cp console-webapp/target/rbm-console-test.war ${appDir}/webapps/ROOT/"
    }

    stage('prepare scripts') {
        sh "chmod +x $scriptDir/*.sh"
    }

    stage('stop server') {
        sh "$scriptDir/jetty-rbm.sh stop"
    }

    stage('deploy') {
        sh "$scriptDir/unzip.sh"
//        sh "java -javaagent:${springInstrumentJar} -Dfile.encoding=UTF-8 -jar ${jettyStartJar} jetty.base=${appDir} > /dev/null &"
        withEnv(['JENKINS_NODE_COOKIE=dontkillme']) {
            sh "$scriptDir/jetty-rbm.sh start"
        }
    }
}