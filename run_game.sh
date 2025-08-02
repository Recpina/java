#!/bin/bash

echo "Castle Defense Game - Compilation and Execution"
echo "==============================================="

echo
echo "Compiling Java files..."
javac --module-path /usr/share/openjfx/lib --add-modules javafx.controls,javafx.fxml *.java

if [ $? -ne 0 ]; then
    echo
    echo "ERROR: Compilation failed!"
    echo "Please check that you have Java and JavaFX installed and all files are present."
    read -p "Press Enter to continue..."
    exit 1
fi

echo
echo "Compilation successful!"
echo
echo "Starting Castle Defense game..."
echo

java --module-path /usr/share/openjfx/lib --add-modules javafx.controls,javafx.fxml CastleDefense

if [ $? -ne 0 ]; then
    echo
    echo "ERROR: Game execution failed!"
    echo "This might be due to JavaFX not being properly configured."
    echo
    echo "Make sure JavaFX is installed: sudo apt install openjfx"
    read -p "Press Enter to continue..."
    exit 1
fi

echo
echo "Game finished."
read -p "Press Enter to continue..." 