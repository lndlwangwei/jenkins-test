#!/usr/bin/env bash

env=$1
if [ "$env" == 'filehandler' ]; then
    . $WORKSPACE/jenkins/rbm-test/scripts/rbm_test_file_handler_profile
else
    . $WORKSPACE/jenkins/rbm-test/scripts/rbm_test_profile
fi

echo "server_name $server_home"
echo "app_name ${app_name}"

serverDir=${server_home}/webapps/ROOT
unzip -d $serverDir $serverDir/${app_name}.war
if [ $? -eq 0 ]; then
rm -f $serverDir/${app_name}.war
fi
exit $?