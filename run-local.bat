@echo off
setlocal
echo ========================================================
echo         MubeenMart - Local Server Launcher
echo ========================================================

set "PROJECT_DIR=%~dp0"
set "TOMCAT_DIR=C:\Users\Mohamed Mubeen\tools\apache-tomcat-9.0.86"
set "WAR_FILE=%PROJECT_DIR%target\mubeenmart.war"

if not exist "%TOMCAT_DIR%\bin\catalina.bat" (
    echo [ERROR] Tomcat 9 not found at %TOMCAT_DIR%
    pause
    exit /b 1
)

if not exist "%WAR_FILE%" (
    echo [INFO] Building MubeenMart WAR package...
    call mvn clean package -DskipTests
    if errorlevel 1 (
        echo [ERROR] Maven build failed.
        pause
        exit /b 1
    )
)

echo [INFO] Deploying mubeenmart.war to Tomcat ROOT...
if exist "%TOMCAT_DIR%\webapps\ROOT" rd /s /q "%TOMCAT_DIR%\webapps\ROOT"
copy /y "%WAR_FILE%" "%TOMCAT_DIR%\webapps\ROOT.war" > nul

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
call "%TOMCAT_DIR%\bin\catalina.bat" run
