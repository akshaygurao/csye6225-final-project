#!/bin/bash
#source csye6225-networking-setup.sh
#echo $STACK_NAME
echo "Hello World"
echo "First argument: $1"
dummy="$1"
echo $dummy
vpc_id=$(aws ec2 describe-vpcs --filters "Name=tag-value,Values="$dummy"-csye6225-vpc" --query "Vpcs[*].VpcId" --output text)
echo $vpc_id

#check if vpc exists. write code for if not exists
aws ec2 wait vpc-exists --vpc-ids $vpc_id

#delete route and route table
route_table_id=$(aws ec2 describe-route-tables --filters "Name=tag-value,Values="$dummy"-csye6225-public-route-table" --query "RouteTables[*].RouteTableId" --output text)
echo $route_table_id
aws ec2 delete-route --route-table-id $route_table_id

aws ec2 delete-route-table --route-table-id $route_table_id

# delete internet gateway
gateway_id=$(aws ec2 describe-internet-gateways --filters "Name=tag-value,Values="$dummy"-csye6225-InternetGateway" --query "InternetGateways[*].InternetGatewayId" --output text)
echo $gateway_id

aws ec2 delete-internet-gateway --internet-gateway-id $gateway_id


aws ec2 delete-vpc --vpc-id $vpc_id


