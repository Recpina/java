import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.animation.AnimationTimer;

public class GameUI {
    private GameEngine gameEngine;
    private BorderPane root;
    private Canvas gameCanvas;
    private GraphicsContext gc;
    private VBox infoPanel;
    private Label coinsLabel;
    private Label healthLabel;
    private Label roundLabel;
    private Label enemiesLabel;
    private Label timerLabel;
    
    private Button fastTowerBtn;
    private Button powerfulTowerBtn;
    private Button speedBumpBtn;
    private Button bombBtn;
    private Button antiAirCheapBtn;
    private Button antiAirExpensiveBtn;
    
    private DefenseTower.TowerType selectedTowerType;
    private boolean placingTower = false;
    private boolean placingSpeedBump = false;
    private boolean placingBomb = false;
    private boolean placingAntiAir = false;
    private boolean isExpensiveAntiAir = false;
    
    private AnimationTimer renderTimer;
    private long gameStartTime;
    
    public GameUI(GameEngine gameEngine) {
        this.gameEngine = gameEngine;
        this.gameStartTime = System.currentTimeMillis();
        
        initializeUI();
        setupEventHandlers();
        setupRenderTimer();
        
        // Register as listener
        gameEngine.addGameStateListener(this::updateUI);
    }
    
    private void initializeUI() {
        root = new BorderPane();
        root.setStyle("-fx-background-color: #2c3e50;");
        
        // Create game canvas
        gameCanvas = new Canvas(GameEngine.MAP_WIDTH, GameEngine.MAP_HEIGHT);
        gc = gameCanvas.getGraphicsContext2D();
        
        // Create info panel
        createInfoPanel();
        
        // Create control panel
        createControlPanel();
        
        // Layout
        root.setCenter(gameCanvas);
        root.setRight(infoPanel);
        root.setBottom(createControlPanel());
    }
    
    private void createInfoPanel() {
        infoPanel = new VBox(10);
        infoPanel.setPadding(new Insets(10));
        infoPanel.setStyle("-fx-background-color: #34495e; -fx-min-width: 200px;");
        
        Label titleLabel = new Label("GAME INFO");
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: white;");
        
        coinsLabel = new Label("Coins: 100");
        coinsLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
        
        healthLabel = new Label("Health: 10/10");
        healthLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
        
        roundLabel = new Label("Round: 1");
        roundLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
        
        enemiesLabel = new Label("Enemies: 0/0");
        enemiesLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
        
        timerLabel = new Label("Time: 00:00");
        timerLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
        
        infoPanel.getChildren().addAll(titleLabel, coinsLabel, healthLabel, roundLabel, enemiesLabel, timerLabel);
    }
    
    private HBox createControlPanel() {
        HBox controlPanel = new HBox(10);
        controlPanel.setPadding(new Insets(10));
        controlPanel.setStyle("-fx-background-color: #34495e;");
        controlPanel.setAlignment(Pos.CENTER);
        
        // Tower buttons
        fastTowerBtn = new Button("Fast Tower ($80)");
        fastTowerBtn.setStyle("-fx-font-size: 12px; -fx-min-width: 100px;");
        fastTowerBtn.setOnAction(e -> selectFastTower());
        
        powerfulTowerBtn = new Button("Powerful Tower ($120)");
        powerfulTowerBtn.setStyle("-fx-font-size: 12px; -fx-min-width: 100px;");
        powerfulTowerBtn.setOnAction(e -> selectPowerfulTower());
        
        speedBumpBtn = new Button("Speed Bump ($50)");
        speedBumpBtn.setStyle("-fx-font-size: 12px; -fx-min-width: 100px;");
        speedBumpBtn.setOnAction(e -> selectSpeedBump());
        
        bombBtn = new Button("Bomb ($75)");
        bombBtn.setStyle("-fx-font-size: 12px; -fx-min-width: 100px;");
        bombBtn.setOnAction(e -> selectBomb());
        
        antiAirCheapBtn = new Button("Anti-Air Cheap ($100)");
        antiAirCheapBtn.setStyle("-fx-font-size: 12px; -fx-min-width: 100px;");
        antiAirCheapBtn.setOnAction(e -> selectAntiAir(false));
        
        antiAirExpensiveBtn = new Button("Anti-Air Expensive ($150)");
        antiAirExpensiveBtn.setStyle("-fx-font-size: 12px; -fx-min-width: 100px;");
        antiAirExpensiveBtn.setOnAction(e -> selectAntiAir(true));
        
        controlPanel.getChildren().addAll(
            fastTowerBtn, powerfulTowerBtn, speedBumpBtn, 
            bombBtn, antiAirCheapBtn, antiAirExpensiveBtn
        );
        
        return controlPanel;
    }
    
    private void setupEventHandlers() {
        gameCanvas.setOnMouseClicked(this::handleMouseClick);
        gameCanvas.setOnMouseMoved(this::handleMouseMoved);
    }
    
    private void setupRenderTimer() {
        renderTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                render();
            }
        };
        renderTimer.start();
    }
    
    private void render() {
        // Clear canvas
        gc.setFill(Color.LIGHTGREEN);
        gc.fillRect(0, 0, GameEngine.MAP_WIDTH, GameEngine.MAP_HEIGHT);
        
        // Draw map
        drawMap();
        
        // Draw game objects
        drawTowers();
        drawEnemies();
        drawSpeedBumps();
        drawBombs();
        drawAntiAircraftDefenses();
        
        // Draw placement preview
        if (placingTower || placingSpeedBump || placingBomb || placingAntiAir) {
            drawPlacementPreview();
        }
    }
    
    private void drawMap() {
        GameMap map = gameEngine.getGameMap();
        
        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                GameMap.TileType tileType = map.getTileType(x, y);
                double drawX = x * GameEngine.TILE_SIZE;
                double drawY = y * GameEngine.TILE_SIZE;
                
                switch (tileType) {
                    case GRASS:
                        gc.setFill(Color.LIGHTGREEN);
                        break;
                    case PATH:
                        gc.setFill(Color.SADDLEBROWN);
                        break;
                    case WATER:
                        gc.setFill(Color.LIGHTBLUE);
                        break;
                    case SAND:
                        gc.setFill(Color.BURLYWOOD);
                        break;
                    case CONSTRUCTION_SITE:
                        gc.setFill(Color.LIGHTGRAY);
                        gc.setStroke(Color.BLACK);
                        gc.strokeRect(drawX, drawY, GameEngine.TILE_SIZE, GameEngine.TILE_SIZE);
                        break;
                    case BOMB_SITE:
                        gc.setFill(Color.ORANGE);
                        break;
                }
                
                gc.fillRect(drawX, drawY, GameEngine.TILE_SIZE, GameEngine.TILE_SIZE);
            }
        }
    }
    
    private void drawTowers() {
        for (DefenseTower tower : gameEngine.getTowers()) {
            if (tower.isDead()) continue;
            
            double x = tower.getX();
            double y = tower.getY();
            
            // Draw tower base
            if (tower.getType() == DefenseTower.TowerType.FAST) {
                gc.setFill(Color.GREEN);
            } else {
                gc.setFill(Color.RED);
            }
            
            gc.fillOval(x - 15, y - 15, 30, 30);
            
            // Draw health bar
            drawHealthBar(x, y - 25, tower.getHealthPercentage());
        }
    }
    
    private void drawEnemies() {
        for (Enemy enemy : gameEngine.getEnemies()) {
            if (enemy.isDead()) continue;
            
            double x = enemy.getX();
            double y = enemy.getY();
            
            // Draw enemy based on type
            if (enemy instanceof Soldier) {
                gc.setFill(Color.GRAY);
                gc.fillOval(x - 8, y - 8, 16, 16);
            } else if (enemy instanceof Tank) {
                gc.setFill(Color.DARKGRAY);
                gc.fillRect(x - 12, y - 12, 24, 24);
            } else if (enemy instanceof Plane) {
                gc.setFill(Color.LIGHTGRAY);
                gc.fillPolygon(
                    new double[]{x, x - 10, x + 10},
                    new double[]{y - 8, y + 8, y + 8},
                    3
                );
            }
            
            // Draw health bar
            drawHealthBar(x, y - 15, (double) enemy.getHealth() / enemy.getMaxHealth());
        }
    }
    
    private void drawSpeedBumps() {
        for (SpeedBump speedBump : gameEngine.getSpeedBumps()) {
            if (speedBump.isExpired()) continue;
            
            double x = speedBump.getX();
            double y = speedBump.getY();
            
            gc.setFill(Color.YELLOW);
            gc.fillRect(x - 10, y - 5, 20, 10);
        }
    }
    
    private void drawBombs() {
        for (Bomb bomb : gameEngine.getBombs()) {
            if (bomb.isExploded()) continue;
            
            double x = bomb.getX();
            double y = bomb.getY();
            
            gc.setFill(Color.BLACK);
            gc.fillOval(x - 8, y - 8, 16, 16);
        }
    }
    
    private void drawAntiAircraftDefenses() {
        for (AntiAircraftDefense defense : gameEngine.getAntiAircraftDefenses()) {
            double x = defense.getX();
            double y = defense.getY();
            
            gc.setFill(Color.PURPLE);
            gc.fillRect(x - 12, y - 12, 24, 24);
        }
    }
    
    private void drawHealthBar(double x, double y, double healthPercentage) {
        double barWidth = 30;
        double barHeight = 4;
        
        // Background
        gc.setFill(Color.RED);
        gc.fillRect(x - barWidth/2, y, barWidth, barHeight);
        
        // Health
        gc.setFill(Color.GREEN);
        gc.fillRect(x - barWidth/2, y, barWidth * healthPercentage, barHeight);
    }
    
    private void drawPlacementPreview() {
        // This would show a preview of where the item would be placed
        // For now, we'll just show a simple indicator
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(2);
        gc.strokeOval(400, 300, 20, 20); // Center of screen for now
    }
    
    private void handleMouseClick(MouseEvent event) {
        double x = event.getX();
        double y = event.getY();
        
        if (placingTower) {
            if (gameEngine.placeTower(x, y, selectedTowerType)) {
                placingTower = false;
                selectedTowerType = null;
            }
        } else if (placingSpeedBump) {
            if (gameEngine.placeSpeedBump(x, y)) {
                placingSpeedBump = false;
            }
        } else if (placingBomb) {
            if (gameEngine.placeBomb(x, y)) {
                placingBomb = false;
            }
        } else if (placingAntiAir) {
            if (gameEngine.placeAntiAircraftDefense(x, y, isExpensiveAntiAir)) {
                placingAntiAir = false;
                isExpensiveAntiAir = false;
            }
        }
    }
    
    private void handleMouseMoved(MouseEvent event) {
        // Update cursor position for placement preview
    }
    
    private void selectFastTower() {
        placingTower = true;
        selectedTowerType = DefenseTower.TowerType.FAST;
        placingSpeedBump = placingBomb = placingAntiAir = false;
    }
    
    private void selectPowerfulTower() {
        placingTower = true;
        selectedTowerType = DefenseTower.TowerType.POWERFUL;
        placingSpeedBump = placingBomb = placingAntiAir = false;
    }
    
    private void selectSpeedBump() {
        placingSpeedBump = true;
        placingTower = placingBomb = placingAntiAir = false;
    }
    
    private void selectBomb() {
        placingBomb = true;
        placingTower = placingSpeedBump = placingAntiAir = false;
    }
    
    private void selectAntiAir(boolean expensive) {
        placingAntiAir = true;
        isExpensiveAntiAir = expensive;
        placingTower = placingSpeedBump = placingBomb = false;
    }
    
    private void updateUI() {
        // Update labels
        coinsLabel.setText("Coins: " + gameEngine.getCoins());
        healthLabel.setText("Health: " + gameEngine.getCastleHealth() + "/" + GameEngine.MAX_CASTLE_HEALTH);
        roundLabel.setText("Round: " + gameEngine.getCurrentRound());
        enemiesLabel.setText("Enemies: " + gameEngine.getRemainingEnemies() + "/" + gameEngine.getTotalEnemies());
        
        long elapsedTime = System.currentTimeMillis() - gameStartTime;
        long seconds = elapsedTime / 1000;
        long minutes = seconds / 60;
        seconds = seconds % 60;
        timerLabel.setText(String.format("Time: %02d:%02d", minutes, seconds));
        
        // Check game end conditions
        if (gameEngine.isGameWon()) {
            showGameEndDialog("Victory!", "Congratulations! You have successfully defended your castle!");
        } else if (gameEngine.isGameLost()) {
            showGameEndDialog("Defeat!", "Your castle has been overrun! Better luck next time.");
        }
    }
    
    private void showGameEndDialog(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    public BorderPane getRoot() {
        return root;
    }
} 