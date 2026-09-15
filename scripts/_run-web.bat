@echo off
chcp 65001 >nul
set "JAVA_TOOL_OPTIONS=-Dfile.encoding=UTF-8 -Dsun.jnu.encoding=UTF-8"
cd /d "%~dp0.."
REM Full gradlew path: with NoDefaultCurrentDirectoryInExePath set, cmd won't run it from the cwd.
REM The -x tasks always rerun and write into ghlove-common; start-all.bat already ran them once,
REM and skipping them here stops web and api from writing there at the same time.
call "%~dp0..\gradlew.bat" -Dspring.profiles.active=local -x :ghlove-common:copyApplicationYml -x :ghlove-common:processResources :ghlove-web:bootRun
