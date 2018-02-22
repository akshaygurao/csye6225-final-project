#!/bin/bash

# update the permission and ownership of WAR file in the tomcat webapps directory
cd /var/lib/tomcat7/
ls -la
chmod 755 webapps/*
