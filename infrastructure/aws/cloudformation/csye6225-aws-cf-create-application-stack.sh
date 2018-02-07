#!/bin/bash
echo "Hello World"
#Input the stack name to be created
Stack_Name="$1"
name=$(aws cloudformation wait stack-exists --stack-name $var1 2>&1)
#echo $name
#echo $?

if [[ -z $name ]];then
	echo "the stack exists. please enter a different name"
	exit 0
fi

aws cloudformation create-stack --template-body file://csye6225-cf-application.json --stack-name $Stack_Name --parameters ParameterKey=InstanceType,ParameterValue=t2.micro ParameterKey=KeyName,ParameterValue=aws1 ParameterKey=AMIName,ParameterValue=ami-41e0b93b ParameterKey=RootVolumeType,ParameterValue=gp2

aws cloudformation wait stack-create-complete --stack-name $Stack_Name

aws cloudformation describe-stacks --stack-name $Stack_Name --query "Stacks[*].StackStatus" --output text

exit 0
