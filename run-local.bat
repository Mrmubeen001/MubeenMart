@echo off
setlocal

echo ========================================================
echo         MubeenMart - Local Server Launcher
echo ========================================================

set "PROJECT_DIR=%~dp0"
set "JAVA_HOME=C:\Program Files\Microsoft\jdk-17.0.20.101-hotspot"
set "CATALINA_HOME=C:\Users\Mohamed Mubeen\tools\apache-tomcat-9.0.86"
set "CATALINA_BASE=C:\Users\Mohamed Mubeen\tools\apache-tomcat-9.0.86"
set "WAR_FILE=%PROJECT_DIR%target\mubeenmart.war"

if not exist "%CATALINA_HOME%\bin\catalina.bat" (
    echo [ERROR] Tomcat 9 not found at %CATALINA_HOME%
    pause
    exit /b 1
)

if not exist "%WAR_FILE%" (
    echo [INFO] Building MubeenMart WAR package...
    call "C:\Users\Mohamed Mubeen\tools\apache-maven-3.9.6\bin\mvn.cmd" clean package -DskipTests
    if errorlevel 1 (
        echo [ERROR] Maven build failed.
        pause
        exit /b 1
    )
)

echo [INFO] Deploying mubeenmart.war to Tomcat ROOT...
if exist "%CATALINA_HOME%\webapps\ROOT" rd /s /q "%CATALINA_HOME%\webapps\ROOT"
copy /y "%WAR_FILE%" "%CATALINA_HOME%\webapps\ROOT.war" > nul

echo ========================================================
echo   MubeenMart is starting on: http://localhost:8088/
echo.
echo   Demo Accounts:
echo     Admin:  admin@mubeenmart.com  / Admin@123
echo     Seller: seller1@mubeenmart.com / Seller@123
echo     Buyer:  buyer1@mubeenmart.com  / Buyer@123
echo.
echo   Press Ctrl+C in this window to stop the server.
echo ========================================================

start http://localhost:8088/
cd /d "%CATALINA_HOME%\bin"
call catalina.bat run
