#!/bin/bash
echo "Hello World"
#There needs to be two inputs. one of networking stack and one of our new stack
#first parameter is the new stack name
Stack_Name="$1"

#second parameter is the networking stack name
ref_stack_name="$2"
#
# #validations for script inputs
# if [[ ( -z $ref_stack_name && -z $Stack_Name ) ]];then
# 	echo "no parameters were provided"
# 	exit 0
# elif [[ ( -z $ref_stack_name || -z $Stack_Name ) ]]; then
# 	echo "Insufficient parameters provided"
# 	exit 0
# else
# 	#check if networking stack exists
# 	net_name=$(aws cloudformation wait stack-exists --stack-name $ref_stack_name 2>&1)
# 	if [[ -z $net_name ]]; then
# 		#check if the new stack to be added already exists
# 		name=$(aws cloudformation wait stack-exists --stack-name $Stack_Name 2>&1)
# 		if [[ -z $name ]]; then
# 			echo "The stack already exists. Please select a different name"
# 			exit 0
# 		fi
# 	else
# 		echo "No such networking stack exists"
# 		exit 0
# 	fi
# fi

#Get the VPC ID and DB SecurityGroup
vpc_name=${ref_stack_name}-csye6225-vpc
vpc_id=$(aws ec2 describe-vpcs --filters Name=tag:Name,Values=rohil-csye6225-vpc --query "Vpcs[0].VpcId" --output text)
web_subnet=$(aws ec2 describe-subnets --filters Name=vpc-id,Values=$vpc_id Name=tag:Name,Values=myWEBsubnet --query "Subnets[0].SubnetId" --output text)
db_subnet1=$(aws ec2 describe-subnets --filters Name=vpc-id,Values=$vpc_id Name=tag:Name,Values=myDBsubnet1 --query "Subnets[0].SubnetId" --output text)
rds_sg=$(aws ec2 describe-security-groups --filters Name=vpc-id,Values=$vpc_id Name=tag:Name,Values=csye-rds --query "SecurityGroups[0].GroupId" --output text)
web_sg=$(aws ec2 describe-security-groups --filters Name=vpc-id,Values=$vpc_id Name=tag:Name,Values=csye-webapp --query "SecurityGroups[0].GroupId" --output text)
db_subnet2=$(aws ec2 describe-subnets --filters Name=vpc-id,Values=$vpc_id Name=tag:Name,Values=myDBsubnet2 --query "Subnets[0].SubnetId" --output text)

aws cloudformation create-stack --template-body file://csye6225-cf-application.json --stack-name $Stack_Name --parameters ParameterKey=InstanceType,ParameterValue=t2.micro ParameterKey=vpcid,ParameterValue=$vpc_id ParameterKey=websubnet,ParameterValue=$web_subnet ParameterKey=dbsubnet1,ParameterValue=$db_subnet1 ParameterKey=dbsubnet2,ParameterValue=$db_subnet2 ParameterKey=rdssg,ParameterValue=$rds_sg ParameterKey=websg,ParameterValue=$web_sg ParameterKey=KeyName,ParameterValue=aws1 ParameterKey=AMIName,ParameterValue=ami-66506c1c ParameterKey=RootVolumeType,ParameterValue=gp2

aws cloudformation wait stack-create-complete --stack-name $Stack_Name

aws cloudformation describe-stacks --stack-name $Stack_Name --query "Stacks[*].StackStatus" --output text

exit 0
