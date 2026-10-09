@echo off
setlocal
set "JAVA_HOME=C:\Program Files\Microsoft\jdk-17.0.20.101-hotspot"
set "CATALINA_HOME=C:\Users\Mohamed Mubeen\tools\apache-tomcat-9.0.86"
set "CATALINA_BASE=C:\Users\Mohamed Mubeen\tools\apache-tomcat-9.0.86"

echo Stopping Tomcat 9...
cd /d "%CATALINA_HOME%\bin"
call catalina.bat stop
echo Tomcat stopped.
