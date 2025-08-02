# How to Run Your Castle Defense Game

Congratulations! Your Java Castle Defense game is ready to run. The code has been compiled successfully.

## Quick Start (Linux)

Since JavaFX has been installed and the code compiled, you can run the game with:

```bash
java --module-path /usr/share/openjfx/lib --add-modules javafx.controls,javafx.fxml CastleDefense
```

Or simply use the provided script:
```bash
./run_game.sh
```

## What You Need to Know

### Your Game
- **Name**: Castle Defense Game
- **Type**: Tower Defense Strategy Game
- **Technology**: Java with JavaFX (graphical interface)
- **Files**: 7 Java source files + compiled .class files

### Game Features
- Defend your castle against waves of enemies
- Place different types of towers (Fast, Powerful)
- Use defensive structures (Speed Bumps, Bombs, Anti-Aircraft)
- Manage resources (coins generated over time)
- Progressive difficulty with multiple enemy types

## Running on Different Systems

### Linux (Current Environment)
```bash
# Make sure JavaFX is installed
sudo apt install openjfx

# Compile the game
javac --module-path /usr/share/openjfx/lib --add-modules javafx.controls,javafx.fxml *.java

# Run the game
java --module-path /usr/share/openjfx/lib --add-modules javafx.controls,javafx.fxml CastleDefense
```

### Windows
```cmd
# Install Java 8-11 (includes JavaFX) or Java 17+ with separate JavaFX
# Download from: https://www.oracle.com/java/technologies/downloads/

# For Java 8-11:
javac *.java
java CastleDefense

# For Java 17+ (download JavaFX from https://openjfx.io/):
javac --module-path "path\to\javafx\lib" --add-modules javafx.controls,javafx.fxml *.java
java --module-path "path\to\javafx\lib" --add-modules javafx.controls,javafx.fxml CastleDefense
```

### macOS
```bash
# Install Java via Homebrew
brew install openjdk
brew install openjfx

# Compile and run
javac --module-path /usr/local/share/openjfx/lib --add-modules javafx.controls,javafx.fxml *.java
java --module-path /usr/local/share/openjfx/lib --add-modules javafx.controls,javafx.fxml CastleDefense
```

## Troubleshooting

### Common Issues

1. **"Module javafx.controls not found"**
   - Solution: Install JavaFX separately for Java 11+
   - Linux: `sudo apt install openjfx`
   - Download from: https://openjfx.io/

2. **"No display available"** (Remote environments)
   - The game requires a graphical display
   - Copy files to a local machine with a desktop environment

3. **"Java not found"**
   - Install Java Development Kit (JDK)
   - Ensure `java` and `javac` are in your PATH

4. **Compilation errors**
   - Make sure all .java files are in the same directory
   - Check that you have the correct Java version (8+)

### Running in IDEs

If you prefer using an IDE:

1. **IntelliJ IDEA**:
   - Open the folder as a project
   - Add JavaFX to module dependencies
   - Run the `CastleDefense` class

2. **Eclipse**:
   - Import as existing project
   - Add JavaFX libraries to build path
   - Run `CastleDefense.java`

3. **VS Code**:
   - Install Java Extension Pack
   - Configure JavaFX in settings
   - Run from the editor

## Game Controls

- **Mouse**: Click to place towers and structures
- **Tower Buttons**: Select tower type before placing
- **Resource Management**: Watch your coins (generated automatically)
- **Strategy**: Block enemy paths and defend your castle

## File Structure

Your game consists of:
- `CastleDefense.java` - Main game launcher
- `GameEngine.java` - Core game logic
- `GameUI.java` - User interface
- `GameMap.java` - Map and placement logic
- `Enemy.java` - Enemy types and behavior
- `DefenseTower.java` - Tower systems
- `DefenseStructures.java` - Bombs, speed bumps, etc.

Enjoy your game! 🏰⚔️