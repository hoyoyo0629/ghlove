@echo off
chcp 65001 >nul
cd /d "%~dp0..\ghlove-frontend"
node index.js
