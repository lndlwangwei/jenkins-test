#!/usr/bin/env bash
workspace=$1
artifactName=$2
env=$3

if [ -f ${workspace}/pid ]; then
    echo 'pid exists, 服务已经启动！'
    exit 0
fi

java -Dfile.encoding=utf-8 -Djava.io.tmpdir=/data/tmp -jar ${workspace}/${artifactName} --spring.profiles.active=${env} > /dev/null &

echo "$!" > ${workspace}/pid
echo "$!"