import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.Random;

public class GameEngine {
    // Game constants
    public static final int MAP_WIDTH = 800;
    public static final int MAP_HEIGHT = 600;
    public static final int TILE_SIZE = 40;
    public static final int INITIAL_COINS = 100;
    public static final int COIN_GENERATION_RATE = 1; // coins per second
    public static final int MAX_CASTLE_HEALTH = 10;
    public static final double ENEMY_PASS_THRESHOLD = 0.1; // 10%
    
    // Game state
    private String difficulty;
    private String mapName;
    private AtomicInteger coins;
    private AtomicInteger castleHealth;
    private int currentRound;
    private int totalEnemies;
    private int enemiesPassed;
    private boolean gameRunning;
    private boolean gameWon;
    private boolean gameLost;
    
    // Game objects
    private GameMap gameMap;
    private List<Enemy> enemies;
    private List<DefenseTower> towers;
    private List<SpeedBump> speedBumps;
    private List<Bomb> bombs;
    private List<AntiAircraftDefense> antiAircraftDefenses;
    
    // Game timer
    private AnimationTimer gameTimer;
    private long lastUpdateTime;
    private long lastCoinGenerationTime;
    
    // Random generator
    private Random random;
    
    // Listeners
    private List<GameStateListener> listeners;
    
    public GameEngine(String difficulty, String mapName) {
        this.difficulty = difficulty;
        this.mapName = mapName;
        this.coins = new AtomicInteger(INITIAL_COINS);
        this.castleHealth = new AtomicInteger(MAX_CASTLE_HEALTH);
        this.currentRound = 1;
        this.totalEnemies = 0;
        this.enemiesPassed = 0;
        this.gameRunning = false;
        this.gameWon = false;
        this.gameLost = false;
        
        this.enemies = new CopyOnWriteArrayList<>();
        this.towers = new CopyOnWriteArrayList<>();
        this.speedBumps = new CopyOnWriteArrayList<>();
        this.bombs = new CopyOnWriteArrayList<>();
        this.antiAircraftDefenses = new CopyOnWriteArrayList<>();
        
        this.random = new Random();
        this.listeners = new ArrayList<>();
        
        initializeGame();
    }
    
    private void initializeGame() {
        // Initialize game map
        gameMap = new GameMap(mapName);
        
        // Setup game timer
        gameTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (gameRunning) {
                    update(now);
                }
            }
        };
    }
    
    public void startGame() {
        gameRunning = true;
        lastUpdateTime = System.nanoTime();
        lastCoinGenerationTime = System.currentTimeMillis();
        gameTimer.start();
        
        // Start enemy spawning
        spawnEnemies();
    }
    
    public void pauseGame() {
        gameRunning = false;
    }
    
    public void resumeGame() {
        gameRunning = true;
    }
    
    public void stopGame() {
        gameRunning = false;
        gameTimer.stop();
    }
    
    private void update(long currentTime) {
        long deltaTime = currentTime - lastUpdateTime;
        lastUpdateTime = currentTime;
        
        // Generate coins
        long currentMillis = System.currentTimeMillis();
        if (currentMillis - lastCoinGenerationTime >= 1000) { // Every second
            generateCoins();
            lastCoinGenerationTime = currentMillis;
        }
        
        // Update all game objects
        updateEnemies(deltaTime);
        updateTowers(deltaTime);
        updateSpeedBumps(deltaTime);
        updateBombs(deltaTime);
        updateAntiAircraftDefenses(deltaTime);
        
        // Check win/lose conditions
        checkGameEndConditions();
        
        // Notify listeners
        notifyGameStateChanged();
    }
    
    private void generateCoins() {
        int currentCoins = coins.get();
        coins.set(currentCoins + COIN_GENERATION_RATE);
    }
    
    private void updateEnemies(long deltaTime) {
        List<Enemy> enemiesToRemove = new ArrayList<>();
        
        for (Enemy enemy : enemies) {
            enemy.update(deltaTime);
            
            // Check if enemy reached the castle
            if (enemy.hasReachedCastle()) {
                enemiesToRemove.add(enemy);
                enemiesPassed++;
                castleHealth.decrementAndGet();
            }
            
            // Check if enemy is dead
            if (enemy.isDead()) {
                enemiesToRemove.add(enemy);
            }
        }
        
        enemies.removeAll(enemiesToRemove);
    }
    
    private void updateTowers(long deltaTime) {
        for (DefenseTower tower : towers) {
            tower.update(deltaTime);
            
            // Find targets for tower
            Enemy target = findTargetForTower(tower);
            if (target != null) {
                tower.attack(target);
            }
        }
    }
    
    private void updateSpeedBumps(long deltaTime) {
        List<SpeedBump> speedBumpsToRemove = new ArrayList<>();
        
        for (SpeedBump speedBump : speedBumps) {
            speedBump.update(deltaTime);
            
            if (speedBump.isExpired()) {
                speedBumpsToRemove.add(speedBump);
            }
        }
        
        speedBumps.removeAll(speedBumpsToRemove);
    }
    
    private void updateBombs(long deltaTime) {
        List<Bomb> bombsToRemove = new ArrayList<>();
        
        for (Bomb bomb : bombs) {
            bomb.update(deltaTime);
            
            if (bomb.isExploded()) {
                // Damage enemies in range
                damageEnemiesInRange(bomb);
                bombsToRemove.add(bomb);
            }
        }
        
        bombs.removeAll(bombsToRemove);
    }
    
    private void updateAntiAircraftDefenses(long deltaTime) {
        for (AntiAircraftDefense defense : antiAircraftDefenses) {
            defense.update(deltaTime);
            
            // Find air targets
            Enemy airTarget = findAirTargetForDefense(defense);
            if (airTarget != null) {
                defense.attack(airTarget);
            }
        }
    }
    
    private Enemy findTargetForTower(DefenseTower tower) {
        Enemy bestTarget = null;
        double bestDistance = Double.MAX_VALUE;
        
        for (Enemy enemy : enemies) {
            if (enemy.isAirUnit()) continue; // Towers can't target air units
            
            double distance = tower.getDistanceTo(enemy);
            if (distance <= tower.getRange() && distance < bestDistance) {
                bestTarget = enemy;
                bestDistance = distance;
            }
        }
        
        return bestTarget;
    }
    
    private Enemy findAirTargetForDefense(AntiAircraftDefense defense) {
        Enemy bestTarget = null;
        double bestDistance = Double.MAX_VALUE;
        
        for (Enemy enemy : enemies) {
            if (!enemy.isAirUnit()) continue; // Anti-aircraft can only target air units
            
            double distance = defense.getDistanceTo(enemy);
            if (distance <= defense.getRange() && distance < bestDistance) {
                bestTarget = enemy;
                bestDistance = distance;
            }
        }
        
        return bestTarget;
    }
    
    private void damageEnemiesInRange(Bomb bomb) {
        for (Enemy enemy : enemies) {
            double distance = bomb.getDistanceTo(enemy.getX(), enemy.getY());
            if (distance <= bomb.getExplosionRadius()) {
                enemy.takeDamage(bomb.getDamage());
            }
        }
    }
    
    private void spawnEnemies() {
        int enemyCount = getEnemyCountForRound();
        totalEnemies += enemyCount;
        
        for (int i = 0; i < enemyCount; i++) {
            // Delay enemy spawning
            final int enemyIndex = i;
            Platform.runLater(() -> {
                spawnEnemy(enemyIndex * 1000); // 1 second delay between enemies
            });
        }
    }
    
    private void spawnEnemy(int delay) {
        // Create enemy based on round and difficulty
        Enemy enemy = createEnemyForRound();
        // Set the enemy path
        enemy.setPath(gameMap.getEnemyPath());
        enemies.add(enemy);
    }
    
    private Enemy createEnemyForRound() {
        // Simple enemy creation logic - can be expanded
        double x = 0;
        double y = MAP_HEIGHT / 2;
        
        if (currentRound <= 3) {
            return new Soldier(x, y);
        } else if (currentRound <= 6) {
            return random.nextBoolean() ? new Soldier(x, y) : new Tank(x, y);
        } else {
            int enemyType = random.nextInt(3);
            switch (enemyType) {
                case 0: return new Soldier(x, y);
                case 1: return new Tank(x, y);
                case 2: return new Plane(x, y);
                default: return new Soldier(x, y);
            }
        }
    }
    
    private int getEnemyCountForRound() {
        int baseCount = 5;
        int difficultyMultiplier = getDifficultyMultiplier();
        return baseCount + (currentRound - 1) * 2 * difficultyMultiplier;
    }
    
    private int getDifficultyMultiplier() {
        switch (difficulty) {
            case "EASY": return 1;
            case "MEDIUM": return 2;
            case "HARD": return 3;
            default: return 2;
        }
    }
    
    private void checkGameEndConditions() {
        // Check if all enemies are destroyed
        if (enemies.isEmpty() && totalEnemies > 0) {
            gameWon = true;
            gameRunning = false;
            return;
        }
        
        // Check if castle is destroyed
        if (castleHealth.get() <= 0) {
            gameLost = true;
            gameRunning = false;
            return;
        }
        
        // Check if too many enemies passed
        double passRatio = (double) enemiesPassed / totalEnemies;
        if (passRatio >= ENEMY_PASS_THRESHOLD) {
            gameLost = true;
            gameRunning = false;
        }
    }
    
    // Public methods for game actions
    public boolean placeTower(double x, double y, DefenseTower.TowerType type) {
        int cost = type.getCost();
        if (coins.get() >= cost && gameMap.canPlaceTower(x, y)) {
            DefenseTower tower = new DefenseTower(x, y, type);
            towers.add(tower);
            coins.addAndGet(-cost);
            return true;
        }
        return false;
    }
    
    public boolean placeSpeedBump(double x, double y) {
        int cost = 50; // Speed bump cost
        if (coins.get() >= cost && gameMap.canPlaceSpeedBump(x, y)) {
            SpeedBump speedBump = new SpeedBump(x, y);
            speedBumps.add(speedBump);
            coins.addAndGet(-cost);
            return true;
        }
        return false;
    }
    
    public boolean placeBomb(double x, double y) {
        int cost = 75; // Bomb cost
        if (coins.get() >= cost && gameMap.canPlaceBomb(x, y)) {
            Bomb bomb = new Bomb(x, y);
            bombs.add(bomb);
            coins.addAndGet(-cost);
            return true;
        }
        return false;
    }
    
    public boolean placeAntiAircraftDefense(double x, double y, boolean isExpensive) {
        int cost = isExpensive ? 150 : 100;
        if (coins.get() >= cost && gameMap.canPlaceAntiAircraft(x, y)) {
            AntiAircraftDefense defense = new AntiAircraftDefense(x, y, isExpensive);
            antiAircraftDefenses.add(defense);
            coins.addAndGet(-cost);
            return true;
        }
        return false;
    }
    
    // Getters
    public int getCoins() { return coins.get(); }
    public int getCastleHealth() { return castleHealth.get(); }
    public int getCurrentRound() { return currentRound; }
    public int getTotalEnemies() { return totalEnemies; }
    public int getEnemiesPassed() { return enemiesPassed; }
    public int getRemainingEnemies() { return enemies.size(); }
    public boolean isGameRunning() { return gameRunning; }
    public boolean isGameWon() { return gameWon; }
    public boolean isGameLost() { return gameLost; }
    public GameMap getGameMap() { return gameMap; }
    public List<Enemy> getEnemies() { return enemies; }
    public List<DefenseTower> getTowers() { return towers; }
    public List<SpeedBump> getSpeedBumps() { return speedBumps; }
    public List<Bomb> getBombs() { return bombs; }
    public List<AntiAircraftDefense> getAntiAircraftDefenses() { return antiAircraftDefenses; }
    
    // Listener management
    public void addGameStateListener(GameStateListener listener) {
        listeners.add(listener);
    }
    
    public void removeGameStateListener(GameStateListener listener) {
        listeners.remove(listener);
    }
    
    private void notifyGameStateChanged() {
        for (GameStateListener listener : listeners) {
            listener.onGameStateChanged();
        }
    }
    
    public interface GameStateListener {
        void onGameStateChanged();
    }
} 