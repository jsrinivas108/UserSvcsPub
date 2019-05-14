#!/bin/bash

if [ "$#" -lt 1 ]; then
    echo "$0 <qa|prod>"
    exit 1
fi

SCRIPTS_DIR=`dirname $0`
export LIB_DIR=$SCRIPTS_DIR/../lib
export CONF_DIR=$SCRIPTS_DIR/../conf
export LOGS_DIR=$SCRIPTS_DIR/../logs

mkdir -p $LOGS_DIR

mv $CONF_DIR/common/* $CONF_DIR
mv $CONF_DIR/$1/* $CONF_DIR

chmod 700 $LIB_DIR
chmod 700 $CONF_DIR
chmod 700 $LOGS_DIR

echo "export env=$1" > ${SCRIPTS_DIR}/env.sh
