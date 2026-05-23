@echo off
where mvn >nul 2>nul
if %ERRORLEVEL% EQU 0 (
  mvn %*
  exit /b %ERRORLEVEL%
)
echo Maven is not installed or not on PATH. Install Maven, or open this project in IntelliJ IDEA and run it from there.
exit /b 127
