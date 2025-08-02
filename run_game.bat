@echo off
echo Castle Defense Game - Compilation and Execution
echo ===============================================

echo.
echo Compiling Java files...
javac *.java

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo ERROR: Compilation failed!
    echo Please check that you have Java installed and all files are present.
    pause
    exit /b 1
)

echo.
echo Compilation successful!
echo.
echo Starting Castle Defense game...
echo.

java CastleDefense

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo ERROR: Game execution failed!
    echo This might be due to JavaFX not being available.
    echo.
    echo For Java 11+: You need to download JavaFX separately
    echo Visit: https://openjfx.io/
    echo.
    echo For Java 8-10: JavaFX should be included by default
    pause
    exit /b 1
)

echo.
echo Game finished.
pause 