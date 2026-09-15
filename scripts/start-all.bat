@echo off
REM Starts all 3 local dev processes, each in its own console window:
REM   ghlove-web      (opmanager/seller, admin)  -> :8080
REM   ghlove-api                                  -> :9080
REM   ghlove-frontend (customer-facing)            -> :3000
REM
REM Run from anywhere; paths resolve relative to this file. Anything already running on those
REM ports is stopped first, so this also works as a restart.

setlocal
chcp 65001 >nul
REM Must match the _run-*.bat helpers exactly, so their Gradle builds reuse the same daemon and
REM see the pre-build below as up to date.
set "JAVA_TOOL_OPTIONS=-Dfile.encoding=UTF-8 -Dsun.jnu.encoding=UTF-8"
cd /d "%~dp0.."

echo [1/3] Stopping anything already running on 8080 / 9080 / 3000...
call "%~dp0stop-all.bat"

REM web and api both depend on ghlove-common. Building it in both windows at the same time made
REM the two builds write ghlove-common\build\resources concurrently, and one of them randomly
REM failed with "Could not set file mode 644" - so build it once here, then launch.
echo.
echo [2/3] Building shared modules once...
call "%~dp0..\gradlew.bat" -Dspring.profiles.active=local :ghlove-common:jar :ghlove-web:classes :ghlove-api:classes
if errorlevel 1 (
    echo.
    echo Build failed - servers were not started. See the error above.
    pause
    exit /b 1
)

echo.
echo [3/3] Launching servers...
start "ghlove-web (8080)" "%~dp0_run-web.bat"
start "ghlove-api (9080)" "%~dp0_run-api.bat"
start "ghlove-frontend (3000)" "%~dp0_run-frontend.bat"

echo.
echo All 3 processes launching in separate windows.
echo   admin (opmanager/seller) : http://localhost:8080
echo   api                      : http://localhost:9080
echo   frontend (customer)      : http://localhost:3000
echo.
echo ghlove-web / ghlove-api take a while to boot (Spring Boot). Watch each window for
echo "Started SalesonWebApplication" / "Started SalesonApiApplication".
echo Use stop-all.bat to shut all 3 down.
endlocal
