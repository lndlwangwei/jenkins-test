pipeline {

    environment {
        scriptPath = "${WORKSPACE}/jenkins/scripts/publish_master_data"
        diffSqlFile = "/home/xkw/wangwei/mdm_diff.sql"
        dbBackupFile = "/home/xkw/wangwei/backup/mdm.sql"
    }

    agent {label '28test'}

    stages {
        // 生成diff sql文件
        stage('generate diff sql') {

            steps {
                // 将脚本文件设置为可执行
                sh "chmod +x ${scriptPath}/*"

                // 执行生成diff sql的脚本文件
                sh "python $scriptPath/generateDiffSql.py $diffSqlFile"
            }
        }

        stage('backup mdm prod database') {
            steps {
                sh "mysqldump -uxkw -pxkw.com1QAZ mdm > $dbBackupFile"
            }
        }

        // 将diff sql应用到正式环境
        stage('apply diff sql') {
            steps {
                sh "mysql -uxkw -pxkw.com1QAZ -e \"source $diffSqlFile\""
            }
        }
    }
}
