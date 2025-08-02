@echo off
echo.
echo Castle Defense Game Launcher
echo =============================
echo.
echo Starting your Castle Defense game...
echo.

java CastleDefense

if %errorlevel% neq 0 (
    echo.
    echo ERROR: Could not start the game!
    echo.
    echo Possible solutions:
    echo 1. Make sure Java is installed
    echo 2. Try: java -cp . CastleDefense
    echo 3. Use IntelliJ IDEA to run the game
    echo.
    pause
) else (
    echo.
    echo Game finished successfully!
    pause
)