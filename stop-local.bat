@echo off
set "TOMCAT_DIR=C:\Users\Mohamed Mubeen\tools\apache-tomcat-9.0.86"
echo Stopping Tomcat 9...
call "%TOMCAT_DIR%\bin\catalina.bat" stop
echo Tomcat stopped.
