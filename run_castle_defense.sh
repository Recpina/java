#!/bin/bash

echo "
╔══════════════════════════════════════╗
║          CASTLE DEFENSE GAME         ║
║            Starting...               ║
╚══════════════════════════════════════╝
"

# Try running with JavaFX modules first
java --module-path /usr/share/openjfx/lib --add-modules javafx.controls,javafx.fxml -jar CastleDefense.jar

# If that fails, try simple JAR execution
if [ $? -ne 0 ]; then
    echo "Trying alternative method..."
    java -jar CastleDefense.jar
fi

# If still failing, try with classpath
if [ $? -ne 0 ]; then
    echo "Trying with classpath..."
    java -cp CastleDefense.jar CastleDefense
fi

# Final error message if nothing works
if [ $? -ne 0 ]; then
    echo "
═══════════════════════════════════════
  ERROR: Could not start the game!
═══════════════════════════════════════

Solutions:
1. Make sure Java is installed
2. Install JavaFX: sudo apt install openjfx
3. Try: java -jar CastleDefense.jar
"
    read -p "Press Enter to continue..."
    exit 1
fi

echo "
Game finished. Thanks for playing!"
read -p "Press Enter to continue..."