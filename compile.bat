@echo off
setlocal enabledelayedexpansion
echo ========================================================
echo   RAAHVO - Compiling Java Console Application
echo ========================================================

cd /d "%~dp0"
if not exist bin mkdir bin

if exist "C:\Program Files\Java\jdk-26.0.1\bin\javac.exe" (
    set "JAVAC_CMD=C:\Program Files\Java\jdk-26.0.1\bin\javac.exe"
) else if defined JAVA_HOME (
    set "JAVAC_CMD=%JAVA_HOME%\bin\javac.exe"
) else (
    set "JAVAC_CMD=javac"
)

echo Using compiler: !JAVAC_CMD!

dir /s /b src\*.java > sources_list.txt
"!JAVAC_CMD!" -d bin @sources_list.txt
set "COMPILE_STATUS=%ERRORLEVEL%"
del sources_list.txt

if !COMPILE_STATUS! EQU 0 (
    echo.
    echo ========================================================
    echo  [SUCCESS] All 30 Evaluation 1 classes compiled cleanly into bin/
    echo ========================================================
    echo You can now launch the application by running: run.bat
) else (
    echo.
    echo ========================================================
    echo  [ERROR] Compilation failed. Please check errors above.
    echo ========================================================
)

pause
