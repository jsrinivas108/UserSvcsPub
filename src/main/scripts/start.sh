#!/bin/bash

SCRIPTS_DIR=`dirname $0`
. ${SCRIPTS_DIR}/env.sh
export LIB_DIR=$SCRIPTS_DIR/../lib
export CONF_DIR=$SCRIPTS_DIR/../conf
export LOGS_DIR=$SCRIPTS_DIR/../logs

export JAVA_HOME=~/softwares/jdk1.8.0_111
export PATH=$JAVA_HOME/bin:$PATH

CLASSPATH=""

for jar in `ls $LIB_DIR/*.jar`; do
   export CLASSPATH=$LIB_DIR/$jar:$CLASSPATH
done

export CLASSPATH=$CONF_DIR:$CLASSPATH

read -p 'db password:' password
sed -i "s/db.password=.*/db.password=$password/g" $CONF_DIR/jdbc.properties

cd $SCRIPTS_DIR
rm $SCRIPTS_DIR/stopFile
_JAVA_OPTIONS=""
nohup $JAVA_HOME/bin/java -Xmx2G -cp $CLASSPATH -Dcomponent=${project.artifactId}-${project.version} -DstopFile=$SCRIPTS_DIR/stopFile com.wetroad.ws.userservice.App $* &

PID=$!
echo $PID > $SCRIPTS_DIR/pid

sleep 60
sed -ie 's/db.password=.*/db.password=/g' $CONF_DIR/jdbc.properties
