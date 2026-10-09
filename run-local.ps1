# MubeenMart Local Server Launcher for PowerShell
Write-Host "========================================================" -ForegroundColor Cyan
Write-Host "        MubeenMart - Local Server Launcher (PowerShell)  " -ForegroundColor Cyan
Write-Host "========================================================" -ForegroundColor Cyan

$env:JAVA_HOME = "C:\Program Files\Microsoft\jdk-17.0.20.101-hotspot"
$env:CATALINA_HOME = "C:\Users\Mohamed Mubeen\tools\apache-tomcat-9.0.86"
$env:CATALINA_BASE = "C:\Users\Mohamed Mubeen\tools\apache-tomcat-9.0.86"

$warPath = "C:\mrm-ai-electron\mubeenmart\target\mubeenmart.war"
$webappsRoot = "$($env:CATALINA_HOME)\webapps\ROOT"
$webappsWar = "$($env:CATALINA_HOME)\webapps\ROOT.war"

if (-not (Test-Path $warPath)) {
    Write-Host "[INFO] Building MubeenMart WAR package..." -ForegroundColor Yellow
    & "C:\Users\Mohamed Mubeen\tools\apache-maven-3.9.6\bin\mvn.cmd" clean package -DskipTests
}

Write-Host "[INFO] Deploying mubeenmart.war to Tomcat ROOT..." -ForegroundColor Yellow
if (Test-Path $webappsRoot) { Remove-Item -Recurse -Force $webappsRoot -ErrorAction SilentlyContinue }
Copy-Item -Path $warPath -Destination $webappsWar -Force

Write-Host "========================================================" -ForegroundColor Green
Write-Host "  MubeenMart is starting on: http://localhost:8088/" -ForegroundColor Green
Write-Host ""
Write-Host "  Demo Accounts:" -ForegroundColor White
Write-Host "    Admin:  admin@mubeenmart.com   / Admin@123" -ForegroundColor White
Write-Host "    Seller: seller1@mubeenmart.com  / Seller@123" -ForegroundColor White
Write-Host "    Buyer:  buyer1@mubeenmart.com   / Buyer@123" -ForegroundColor White
Write-Host ""
Write-Host "  Press Ctrl+C in this window to stop the server." -ForegroundColor Yellow
Write-Host "========================================================" -ForegroundColor Green

Start-Process "http://localhost:8088/"
Set-Location "$($env:CATALINA_HOME)\bin"
& ".\catalina.bat" run
