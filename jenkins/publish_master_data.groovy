pipeline {

    environment {
        scriptPath = "${WORKSPACE}/jenkins/scripts/publish_master_data"
        diffSqlFile = "/home/xkw/wangwei/mdm_diff.sql"
        dbBackupFile = "/home/xkw/wangwei/backup/mdm.sql"
    }

    agent {label '28test'}

    stages {

        stage('export pilot tables') {
            steps {
                sh "mysqldump -uxkw -pxkw.com1QAZ --add-drop-table=false --comments=false --replace --no-create-info mdm_pilot " +
                        "knowledge_points similar_catalog_group tcatalog_kpoint textbook_attachment textbook_catalogs " +
                        "textbook_versions textbooks version_families kpoint_cards exam_areas exam_subjects exam_area_subject tricks trick_cards " +
                        " > $diffSqlFile"

                if(fileExists publish_master_data/dbUtils.py) {
                    echo "true"
                }
                else {
                    echo "false"
                }
            }
        }
        // 生成diff sql文件
        stage('generate diff sql') {

            steps {
                // 将脚本文件设置为可执行
                sh "chmod +x ${scriptPath}/*"

                // 执行生成diff sql的脚本文件
                sh "python $scriptPath/appendDiffSql.py $diffSqlFile"
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
                // diff file中添加use mdm语句
                sh "sed -i '1i\\use mdm;' $diffSqlFile"

                sh "mysql -uxkw -pxkw.com1QAZ < $diffSqlFile"
            }
        }
    }
}
