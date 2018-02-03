#!/bin/bash

var1="$1"
#aws cloudformation wait stack-exists --stack-name $var1
# echo $?

# if [ "$?" -eq "255" ];then	
# 	echo "the stack does not exists"
# 	exit 1
# fi

aws cloudformation delete-stack --stack-name $var1

aws cloudformation wait stack-delete-complete --stack-name $var1