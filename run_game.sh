#!/bin/bash

echo "Castle Defense Game - Compilation and Execution"
echo "==============================================="

echo
echo "Compiling Java files..."
javac *.java

if [ $? -ne 0 ]; then
    echo
    echo "ERROR: Compilation failed!"
    echo "Please check that you have Java installed and all files are present."
    read -p "Press Enter to continue..."
    exit 1
fi

echo
echo "Compilation successful!"
echo
echo "Starting Castle Defense game..."
echo

java CastleDefense

if [ $? -ne 0 ]; then
    echo
    echo "ERROR: Game execution failed!"
    echo "This might be due to JavaFX not being available."
    echo
    echo "For Java 11+: You need to download JavaFX separately"
    echo "Visit: https://openjfx.io/"
    echo
    echo "For Java 8-10: JavaFX should be included by default"
    read -p "Press Enter to continue..."
    exit 1
fi

echo
echo "Game finished."
read -p "Press Enter to continue..." 