#!/bin/bash

workspace=$1

if [ ! -f $workspace/pid ]; then
    echo 'pid文件不存在！'
    exit 0
fi

pid=`cat $workspace/pid`

rm -f $workspace/pid

if [ -z $pid ]; then
    echo '服务未启动！'
    exit 0
fi

echo ${pid}
kill -9 ${pid}
exit $?