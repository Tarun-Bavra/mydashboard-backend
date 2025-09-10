@echo off
set PORT=8080

echo Checking for processes on port %PORT%...
for /f "tokens=5" %%p in ('netstat -aon ^| find ":%PORT%"') do (
echo Killing process with PID: %%p
taskkill /F /PID %%p
)

echo.
echo Starting Spring Boot application...
call mvn spring-boot:run

pause