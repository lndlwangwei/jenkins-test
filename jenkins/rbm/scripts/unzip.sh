#!/usr/bin/env bash

. ./rbm_pilot_profile
serverDir=${server_home}/webapps/ROOT
unzip -d $serverDir $serverDir/${app_name}.war
if [ $? -eq 0 ]; then
rm -f $serverDir/${app_name}.war
fi
exit $?