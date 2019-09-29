#!/usr/bin/env bash

. $WORKSPACE/jenkins/rbm-test/scripts/profile
echo "server_name $server_home"
echo "app_name ${app_name}"

serverDir=${server_home}/webapps/ROOT
unzip -d $serverDir $serverDir/${app_name}.war
if [ $? -eq 0 ]; then
rm -f $serverDir/${app_name}.war
fi
exit $?