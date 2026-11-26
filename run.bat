@echo off
setlocal enabledelayedexpansion
cls
echo ========================================================
echo   RAAHVO - Smart Travel Booking ^& Trip Planning System
echo                  "Every Route Has a Story."
echo ========================================================

cd /d "%~dp0"

if exist "C:\Program Files\Java\jdk-26.0.1\bin\java.exe" (
    set "JAVA_CMD=C:\Program Files\Java\jdk-26.0.1\bin\java.exe"
) else if defined JAVA_HOME (
    set "JAVA_CMD=%JAVA_HOME%\bin\java.exe"
) else (
    set "JAVA_CMD=java"
)

if not exist "bin\com\raahvo\common\Main.class" (
    echo [WARNING] Binaries not found in bin/. Please run compile.bat first!
    pause
    exit /b 1
)

"!JAVA_CMD!" -cp "bin;lib/*" com.raahvo.common.Main
pause
