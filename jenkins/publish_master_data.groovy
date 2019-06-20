pipeline {

    environment {
        scriptPath = "${WORKSPACE}/jenkins/scripts/publish_master_data"
        diffSqlFile = "/home/xkw/wangwei/mdm_diff.sql"
    }

    agent {label '28test'}

    stages {
        // 准备相关的脚本
        stage('generate diff sql') {

            steps {
                // 将脚本文件设置为可执行
                sh "chmod +x ${scriptPath}/*"

                // 执行生成diff sql的脚本文件
                sh "python $scriptPath/GenerateDiffSql.py $diffSqlFile"
            }

        }
    }
}
