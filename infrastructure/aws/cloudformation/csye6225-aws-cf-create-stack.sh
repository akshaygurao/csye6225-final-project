#!/bin/bash
var1="$1"

# name=$(aws cloudformation describe-stacks --stack-name rohiltest --query "Stacks[*].StackName" --output text)
# echo $?
# echo $name

# if [ "$?" -eq "255" ];then		
# 	continue
# else
# 	echo "the stack exists"
# 	exit 0
# fi



aws cloudformation create-stack --stack-name $var1 --parameters ParameterKey=vpcName,ParameterValue=${var1}-csye6225-vpc ParameterKey=gatewayName,ParameterValue=${var1}-csye6225-InternetGateway ParameterKey=tableName,ParameterValue=${var1}-csye6225-public-route-table --template-body file://csye6225-cf-networking.json

aws cloudformation wait stack-create-complete --stack-name $var1

exit 0