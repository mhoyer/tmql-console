@echo off

if not defined JAVA_HOME (
  echo JAVA_HOME not defined
  pause
  goto exit
)

set SCRIPT_LOCATION=%~dp0

"%JAVA_HOME%\bin\java" %JAVA_OPTS% -Djava.ext.dirs=%SCRIPT_LOCATION%\libs -jar %SCRIPT_LOCATION%\TMQLConsole-${version}.jar %*

:exit

