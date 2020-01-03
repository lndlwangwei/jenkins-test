#!/usr/bin/env bash
workspace=$1
artifactName=$2
port=$3
env=$4

if [ -f ${workspace}/pid ]; then
    echo 'pid exists, 服务已经启动！'
    exit 0
fi

java -jar ${workspace}/${artifactName} --spring.profiles.active=${env} --server.port=${port} > /dev/null &

echo "$!" > ${workspace}/pid
echo "$!"