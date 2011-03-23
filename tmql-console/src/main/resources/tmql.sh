#!/bin/sh
if [[ ! -d $JAVA_HOME ]] ;
then
  echo JAVA_HOME not defined or does not exist!
else
  SCRIPT_LOCATION=`dirname "$0"`
  "${JAVA_HOME}/bin/java" -Djava.ext.dirs=$SCRIPT_LOCATION/libs -jar $SCRIPT_LOCATION/TMQLConsole.jar $*
fi
