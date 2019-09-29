def buildProjectName = 'rbm-console-build'
def appDir = '/home/data/apps/rbm_server'
def springInstrumentJar = '/data/lib/spring-instrument-5.0.2.RELEASE.jar'
def jettyStartJar = '/opt/jetty-distribution-9.4.7.v20170914/start.jar'

node('37test') {
    stage('prepare artifacts') {
        copyArtifacts(projectName: "${buildProjectName}")

        sh "rm -rf ${appDir}/webapps/ROOT/*"
        sh "cp console-webapp/target/rbm-console-pilotrun ${appDir}/webapps/ROOT/"
    }

    stage('deploy') {
        sh "java -javaagent:${springInstrumentJar} -Dfile.encoding=UTF-8 -jar ${jettyStartJar} jetty.base=${appDir} > /dev/null &"
    }
}