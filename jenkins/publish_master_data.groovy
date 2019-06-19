pipeline {

    environment {
        scriptPath = "${WORKSPACE}/scripts/publish_master_data"
    }

    agent none

    stages {
        // 准备相关的脚本
        stage('make scripts executable') {
            agent {label '28test'}
            steps {
                sh "chmod +x ${scriptPath}/*"
            }
        }

        stage('generate diff sql') {
            agent {label '28test'}
            steps {
                sh "python $scriptPath/GenerateDiffSql.py"
            }
        }

    }
}
