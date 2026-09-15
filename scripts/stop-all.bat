@echo off
REM Stops whatever is listening on the 3 local dev ports (8080/9080/3000), regardless of
REM how it was started (gradle daemon child process, node, etc.) - killing by port is the
REM only reliable way since `gradlew bootRun` detaches into a persistent Gradle daemon.

setlocal enabledelayedexpansion

for %%P in (8080 9080 3000) do call :killport %%P

echo.
echo Done.
endlocal
goto :eof

:killport
set "PORT=%~1"
set "FOUND=0"
for /f "tokens=5" %%A in ('netstat -ano ^| findstr /R /C:":%PORT%[^0-9]" ^| findstr "LISTENING"') do (
    echo Stopping process on port %PORT% ^(PID %%A^)...
    taskkill /F /PID %%A >nul 2>&1
    set "FOUND=1"
)
if "!FOUND!"=="0" echo Nothing listening on port %PORT%.
goto :eof
