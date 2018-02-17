#!/bin/bash

var1="$1"
#name=$(aws cloudformation describe-stacks --stack-name $var1 --query "Stacks[*].StackName" --output text 2>&1)

#validation code
# name=$(aws cloudformation wait stack-exists --stack-name $var1 2>&1)
#
# if [[ ! -z $name ]];then
# 	echo "the stack does not exist. please enter a different name"
# 	exit 0
# fi

id=$(aws cloudformation describe-stacks --stack-name $var1 --query "Stacks[*].StackId" --output text 2>&1)

instance_id=$(aws cloudformation describe-stack-resources --stack-name $var1 --logical-resource-id EC2Instance --query "StackResources[0].PhysicalResourceId" --output text)
aws ec2 modify-instance-attribute --instance-id $instance_id --no-disable-api-termination





aws cloudformation delete-stack --stack-name $var1

aws cloudformation wait stack-delete-complete --stack-name $var1
aws cloudformation describe-stacks --stack-name $id --query "Stacks[*].StackStatus" --output text

exit 0
