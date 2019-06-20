#!/bin/bash

diffSqlFile=$1
mysql -uxkw -pxkw.com1QAZ -e "use mdm; source $diffSqlFile"
