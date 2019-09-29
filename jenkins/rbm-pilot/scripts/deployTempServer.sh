#!/usr/bin/env bash

. $WORKSPACE/jenkins/rbm-pilot/scripts/temp_server_profile

#停止临时服务，如果不存在此服务，没有副作用
${script_home}/${app_name}/stopTempServer.sh

#删除临时服务的代码
rm -rf ${temp_app_home}/${app_name}/webapps/*

#将当前正式服务的代码复制到临时服务
cp -r ${server_home}/webapps/ROOT/ ${temp_server_home}/webapps

#修改临时服务的配置
sed -i 's/rbm.instance.type=prod/rbm.instance.type=temp/g' ${temp_server_home}/webapps/ROOT/WEB-INF/classes/services.properties

#启动临时服务
java -javaagent:/data/lib/spring-instrument-5.0.2.RELEASE.jar -jar $JETTY_HOME/start.jar jetty.base=${temp_server_home} &

exit $?