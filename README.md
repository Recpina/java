# Castle Defense Game

A strategic tower defense game built in Java using JavaFX. Defend your castle against waves of enemies by placing defensive towers, speed bumps, bombs, and anti-aircraft defenses.

## Game Features

### Core Gameplay
- **Strategic Defense**: Place towers and defensive structures to protect your castle
- **Resource Management**: Manage coins that are generated over time
- **Multiple Enemy Types**: Soldiers, Tanks, and Planes with different behaviors
- **Defensive Structures**: Fast towers, powerful towers, speed bumps, bombs, and anti-aircraft defenses
- **Progressive Difficulty**: Each round increases in difficulty with more and stronger enemies

### Game Elements

#### Defense Towers
- **Fast Towers**: High fire rate, low health, cost $80
- **Powerful Towers**: Low fire rate, high health, cost $120

#### Enemy Units
- **Soldiers**: Basic ground units that move along the path
- **Tanks**: Heavy ground units with high health that can attack towers
- **Planes**: Fast air units that can attack from above

#### Defensive Structures
- **Speed Bumps**: Slow down enemies by 50% for 10 seconds, cost $50
- **Bombs**: Explode after 2 seconds, damaging all enemies in range, cost $75
- **Anti-Aircraft Defenses**: Target air units with different hit chances
  - Cheap: 60% hit chance, cost $100
  - Expensive: 80% hit chance, cost $150

### Win/Lose Conditions
- **Victory**: Destroy all enemies before they reach the castle
- **Defeat**: 
  - Castle health reaches 0
  - 10% or more of total enemies reach the castle

## Requirements

- Java 8 or higher
- JavaFX (included with Java 8-10, separate module for Java 11+)

## Compilation and Running

### Method 1: Using javac and java (Java 8-10)

1. **Compile the game:**
   ```bash
   javac *.java
   ```

2. **Run the game:**
   ```bash
   java CastleDefense
   ```

### Method 2: Using Java 11+ with JavaFX

1. **Download JavaFX SDK** from [OpenJFX](https://openjfx.io/)

2. **Set JavaFX path** (replace with your actual JavaFX path):
   ```bash
   # Windows
   set JAVAFX_PATH=C:\path\to\javafx-sdk-11.0.2\lib
   
   # Linux/Mac
   export JAVAFX_PATH=/path/to/javafx-sdk-11.0.2/lib
   ```

3. **Compile with JavaFX modules:**
   ```bash
   javac --module-path $JAVAFX_PATH --add-modules javafx.controls,javafx.fxml *.java
   ```

4. **Run with JavaFX modules:**
   ```bash
   java --module-path $JAVAFX_PATH --add-modules javafx.controls,javafx.fxml CastleDefense
   ```

### Method 3: Using an IDE

1. **Open the project** in your preferred IDE (Eclipse, IntelliJ IDEA, NetBeans)
2. **Add JavaFX libraries** to your project dependencies
3. **Run the `CastleDefense` class**

## How to Play

1. **Start the game** and select difficulty level and map
2. **Place defensive structures** by clicking the buttons and then clicking on valid locations
3. **Manage your resources** - coins are generated automatically over time
4. **Defend your castle** - prevent enemies from reaching the right side of the map
5. **Survive waves** - each round gets progressively harder

### Controls

- **Mouse Click**: Place defensive structures
- **Tower Buttons**: Select tower type to place
- **Speed Bump Button**: Place speed bumps on the path
- **Bomb Button**: Place bombs on bomb sites
- **Anti-Air Buttons**: Place anti-aircraft defenses

### Game Interface

- **Left Panel**: Game map with enemies, towers, and defensive structures
- **Right Panel**: Game information (coins, health, round, enemies, timer)
- **Bottom Panel**: Control buttons for placing defensive structures

## Game Architecture

### Core Classes

- **`CastleDefense`**: Main application class with menu system
- **`GameEngine`**: Core game logic and state management
- **`GameMap`**: Map layout and placement validation
- **`GameUI`**: User interface and rendering
- **`Enemy`**: Base enemy class with subclasses (Soldier, Tank, Plane)
- **`DefenseTower`**: Tower defense system
- **`DefenseStructures`**: Speed bumps, bombs, and anti-aircraft defenses

### Design Patterns

- **Observer Pattern**: Game state listeners for UI updates
- **Strategy Pattern**: Different enemy types with different behaviors
- **Factory Pattern**: Enemy creation based on round and difficulty
- **Singleton Pattern**: Game engine as the central game controller

## Game Constants

- **Map Size**: 800x600 pixels
- **Tile Size**: 40 pixels
- **Initial Coins**: 100
- **Coin Generation**: 1 coin per second
- **Castle Health**: 10
- **Enemy Pass Threshold**: 10%

## Future Enhancements

- A* pathfinding algorithm for dynamic enemy routing
- Tank rotation animations with mathematical formulas
- Zoom in/out functionality
- Support army capability
- Shooting effects and animations
- Map maker functionality
- Multiple map support
- Sound effects and background music

## Troubleshooting

### Common Issues

1. **JavaFX not found**: Make sure you're using Java 8-10 or have JavaFX properly configured for Java 11+
2. **Compilation errors**: Ensure all Java files are in the same directory
3. **Runtime errors**: Check that JavaFX modules are properly included

### Performance Tips

- Close other applications to free up system resources
- Reduce game window size if experiencing lag
- Restart the game if performance degrades over time

## Credits

This game was created as a Java programming project demonstrating object-oriented design principles, game development concepts, and JavaFX usage.

## License

This project is for educational purposes. Feel free to modify and extend the code for learning and personal use. 