#!/bin/bash

# In order to avoid password prompting create the file:
#
#    ~/.pgpass
#
# with contents:
#
#     hostname:port:database:username:password
#
# See "The Password File" in the postgres docs
#
# Since there is not a way to set the search path when using a jdbc
# datasource, you can change the default search_path for the user:
#
#     ALTER USER postgres SET search_path TO tia,public;

BUILD_DIR=../build
CREATE_LIST=$BUILD_DIR/create_list.txt
CREATE_SCRIPT=create_schema_objects.sql

mkdir -p $BUILD_DIR

echo -- Dynamic script generation start > $CREATE_SCRIPT
echo "select now();"                   >> $CREATE_SCRIPT

export IFS=
# Generate a script to create schema objects
while read fileName; do
    if [ ! -z `echo $fileName` ]
    then
        echo ---------------------------   >> $CREATE_SCRIPT
        echo -- Processing File: $fileName >> $CREATE_SCRIPT
        echo ---------------------------   >> $CREATE_SCRIPT
        cat $fileName                      >> $CREATE_SCRIPT
    fi
done < $CREATE_LIST




# =============================================
# == Run generated script
# =============================================
psql -p 5432 -e a4 postgres < $CREATE_SCRIPT

# pause
