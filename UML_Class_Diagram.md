# Castle Defense Game - UML Class Diagram

## Class Relationships and Architecture

### Main Application Layer
```
CastleDefense (Application)
├── GameEngine
├── GameUI
└── Stage (JavaFX)
```

### Game Engine Layer
```
GameEngine
├── GameMap
├── List<Enemy>
├── List<DefenseTower>
├── List<SpeedBump>
├── List<Bomb>
├── List<AntiAircraftDefense>
└── GameStateListener (Interface)
```

### Game Objects Layer
```
Enemy (Abstract)
├── Soldier
├── Tank
└── Plane

DefenseTower
├── TowerType (Enum)
│   ├── FAST
│   └── POWERFUL

SpeedBump
Bomb
AntiAircraftDefense
```

### Map and UI Layer
```
GameMap
├── TileType (Enum)
│   ├── GRASS
│   ├── PATH
│   ├── WATER
│   ├── SAND
│   ├── CONSTRUCTION_SITE
│   └── BOMB_SITE
└── List<Point> (enemyPath)

GameUI
├── Canvas
├── GraphicsContext
├── InfoPanel (VBox)
└── ControlPanel (HBox)
```

## Design Patterns Used

### 1. Observer Pattern
- **GameEngine** implements the subject
- **GameUI** implements the observer
- **GameStateListener** interface defines the contract
- **Purpose**: Update UI when game state changes

### 2. Strategy Pattern
- **Enemy** abstract class with different concrete strategies
- **Soldier**, **Tank**, **Plane** implement different behaviors
- **Purpose**: Different enemy types with different movement and attack patterns

### 3. Factory Pattern
- **GameEngine.createEnemyForRound()** creates enemies based on round
- **Purpose**: Centralized enemy creation logic

### 4. Singleton-like Pattern
- **GameEngine** acts as the central game controller
- **Purpose**: Single point of control for game state

## Class Responsibilities

### CastleDefense
- **Responsibility**: Main application entry point
- **Dependencies**: JavaFX Application, GameEngine, GameUI
- **Methods**: start(), showStartMenu(), showGameSetup(), startGame()

### GameEngine
- **Responsibility**: Core game logic and state management
- **Dependencies**: GameMap, Enemy classes, Defense classes
- **Methods**: update(), spawnEnemies(), placeTower(), checkGameEndConditions()

### GameMap
- **Responsibility**: Map layout and placement validation
- **Dependencies**: Point, TileType enum
- **Methods**: canPlaceTower(), canPlaceSpeedBump(), getEnemyPath()

### Enemy (Abstract)
- **Responsibility**: Base enemy behavior
- **Dependencies**: Point, GameMap
- **Methods**: update(), moveAlongPath(), takeDamage(), attack()

### DefenseTower
- **Responsibility**: Tower defense logic
- **Dependencies**: TowerType enum, Enemy
- **Methods**: update(), attack(), takeDamage()

### GameUI
- **Responsibility**: User interface and rendering
- **Dependencies**: JavaFX components, GameEngine
- **Methods**: render(), drawMap(), drawEnemies(), handleMouseClick()

## Relationships Explanation

### Inheritance Relationships
1. **Enemy → Soldier/Tank/Plane**: Different enemy types with specialized behaviors
2. **Application → CastleDefense**: JavaFX application structure

### Composition Relationships
1. **GameEngine contains GameMap**: Game engine manages the map
2. **GameEngine contains Lists of game objects**: Manages all game entities
3. **GameUI contains Canvas and UI components**: Manages the visual interface

### Association Relationships
1. **GameEngine ↔ GameUI**: Two-way communication through listener pattern
2. **Enemy ↔ GameMap**: Enemies follow paths defined by the map
3. **DefenseTower ↔ Enemy**: Towers target and attack enemies

### Dependency Relationships
1. **CastleDefense depends on GameEngine**: Creates and manages game engine
2. **GameUI depends on GameEngine**: Gets game state for rendering
3. **All game objects depend on GameEngine**: For state updates and coordination

## Key Design Principles

### 1. Single Responsibility Principle
- Each class has a single, well-defined responsibility
- GameEngine handles logic, GameUI handles rendering, GameMap handles layout

### 2. Open/Closed Principle
- Easy to add new enemy types by extending Enemy class
- Easy to add new defense types by creating new classes
- Game engine is open for extension, closed for modification

### 3. Dependency Inversion
- High-level modules (GameEngine) don't depend on low-level modules (specific enemy types)
- Both depend on abstractions (Enemy interface)

### 4. Encapsulation
- Game state is encapsulated in GameEngine
- UI logic is separated from game logic
- Each class manages its own internal state

## Extensibility Points

### Adding New Enemy Types
1. Extend the Enemy abstract class
2. Implement required abstract methods
3. Add creation logic in GameEngine.createEnemyForRound()

### Adding New Defense Types
1. Create new defense class
2. Add placement logic in GameEngine
3. Add UI controls in GameUI
4. Add rendering logic in GameUI

### Adding New Maps
1. Extend GameMap class or create map configuration system
2. Add map selection in CastleDefense
3. Update GameEngine to use different map layouts

## Performance Considerations

### Memory Management
- Use CopyOnWriteArrayList for thread-safe collections
- Remove dead enemies and expired structures
- Use object pooling for frequently created objects

### Rendering Optimization
- Only render visible game objects
- Use efficient drawing operations
- Minimize object creation in render loop

### Game Loop Optimization
- Use AnimationTimer for smooth 60 FPS updates
- Separate update logic from rendering
- Use delta time for frame-rate independent movement 