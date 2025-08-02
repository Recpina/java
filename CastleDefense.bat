@echo off
title Castle Defense Game
echo.
echo  ╔══════════════════════════════════════╗
echo  ║          CASTLE DEFENSE GAME         ║
echo  ║            Starting...               ║
echo  ╚══════════════════════════════════════╝
echo.

REM Try running with JavaFX modules first
java --module-path "%JAVA_HOME%\lib" --add-modules javafx.controls,javafx.fxml -jar CastleDefense.jar

REM If that fails, try simple JAR execution
if %errorlevel% neq 0 (
    echo Trying alternative method...
    java -jar CastleDefense.jar
)

REM If still failing, try with classpath
if %errorlevel% neq 0 (
    echo Trying with classpath...
    java -cp CastleDefense.jar CastleDefense
)

REM Final error message if nothing works
if %errorlevel% neq 0 (
    echo.
    echo ═══════════════════════════════════════
    echo   ERROR: Could not start the game!
    echo ═══════════════════════════════════════
    echo.
    echo Solutions:
    echo 1. Make sure Java is installed
    echo 2. Download Java from: java.com
    echo 3. Try double-clicking CastleDefense.jar
    echo.
    pause
    exit /b 1
)

echo.
echo Game finished. Thanks for playing!
pause