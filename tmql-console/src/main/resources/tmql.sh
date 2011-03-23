#!/bin/sh
if [[ ! -d $JAVA_HOME ]] ;
then
  echo JAVA_HOME not defined or does not exist!
else
  "${JAVA_HOME}/bin/java" -Djava.ext.dirs=./libs -jar TMQLConsole.jar $*
fi
