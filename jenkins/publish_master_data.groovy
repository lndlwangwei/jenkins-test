pipeline {

//    environment {
//        scriptPath = "${WORKSPACE}/scripts/publish_master_data"
//    }

    agent none

    stages {
        // 准备相关的脚本
        stage('make scripts executable') {
            agent {label '28test'}
            environment {
                scriptPath = "${WORKSPACE}/scripts/publish_master_data"
            }

            steps {
                sh "chmod +x ${scriptPath}/*"
            }

//            steps {
//                sh "python $scriptPath/GenerateDiffSql.py"
//            }
        }
    }
}
