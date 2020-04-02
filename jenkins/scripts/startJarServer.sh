#!/usr/bin/env bash
workspace=$1
artifactName=$2
env=$3
javaagentJar=$4

echo $1
echo $javaagentJar

if [ -f ${workspace}/pid ]; then
    echo 'pid exists, 服务已经启动！'
    exit 0
fi

javaagentOption=''
if [ -n "$javaagentJar" ]; then
    javaagentOption="-javaagent:"/$javaagentJar
fi

echo "javaagentOption"
echo $javaagentOption

java $javaagentOption -Dfile.encoding=utf-8 -Djava.io.tmpdir=/data/tmp -jar ${workspace}/${artifactName} --spring.profiles.active=${env} > /dev/null &

echo "$!" > ${workspace}/pid
echo "$!"