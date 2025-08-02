import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;
import javafx.scene.control.ComboBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

public class CastleDefense extends Application {
    
    private GameEngine gameEngine;
    private GameUI gameUI;
    private Stage primaryStage;
    
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Castle Defense");
        
        // Show start menu
        showStartMenu();
        
        primaryStage.show();
    }
    
    private void showStartMenu() {
        BorderPane menuLayout = new BorderPane();
        menuLayout.setStyle("-fx-background-color: #2c3e50;");
        
        VBox menuContent = new VBox(20);
        menuContent.setAlignment(Pos.CENTER);
        menuContent.setPadding(new Insets(50));
        
        Label titleLabel = new Label("CASTLE DEFENSE");
        titleLabel.setStyle("-fx-font-size: 36px; -fx-font-weight: bold; -fx-text-fill: white;");
        
        Button newGameBtn = new Button("New Game");
        newGameBtn.setStyle("-fx-font-size: 18px; -fx-min-width: 200px; -fx-min-height: 40px;");
        newGameBtn.setOnAction(e -> showGameSetup());
        
        Button exitBtn = new Button("Exit");
        exitBtn.setStyle("-fx-font-size: 18px; -fx-min-width: 200px; -fx-min-height: 40px;");
        exitBtn.setOnAction(e -> System.exit(0));
        
        menuContent.getChildren().addAll(titleLabel, newGameBtn, exitBtn);
        menuLayout.setCenter(menuContent);
        
        Scene menuScene = new Scene(menuLayout, 800, 600);
        primaryStage.setScene(menuScene);
    }
    
    private void showGameSetup() {
        BorderPane setupLayout = new BorderPane();
        setupLayout.setStyle("-fx-background-color: #34495e;");
        
        VBox setupContent = new VBox(20);
        setupContent.setAlignment(Pos.CENTER);
        setupContent.setPadding(new Insets(50));
        
        Label setupLabel = new Label("Game Setup");
        setupLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: white;");
        
        // Difficulty selection
        HBox difficultyBox = new HBox(10);
        difficultyBox.setAlignment(Pos.CENTER);
        Label difficultyLabel = new Label("Difficulty:");
        difficultyLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");
        ComboBox<String> difficultyCombo = new ComboBox<>();
        difficultyCombo.getItems().addAll("EASY", "MEDIUM", "HARD");
        difficultyCombo.setValue("MEDIUM");
        difficultyBox.getChildren().addAll(difficultyLabel, difficultyCombo);
        
        // Map selection
        HBox mapBox = new HBox(10);
        mapBox.setAlignment(Pos.CENTER);
        Label mapLabel = new Label("Map:");
        mapLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");
        ComboBox<String> mapCombo = new ComboBox<>();
        mapCombo.getItems().addAll("Map 1", "Map 2", "Map 3");
        mapCombo.setValue("Map 1");
        mapBox.getChildren().addAll(mapLabel, mapCombo);
        
        Button startGameBtn = new Button("Start Game");
        startGameBtn.setStyle("-fx-font-size: 18px; -fx-min-width: 200px; -fx-min-height: 40px;");
        startGameBtn.setOnAction(e -> startGame(difficultyCombo.getValue(), mapCombo.getValue()));
        
        Button backBtn = new Button("Back to Menu");
        backBtn.setStyle("-fx-font-size: 16px; -fx-min-width: 150px; -fx-min-height: 30px;");
        backBtn.setOnAction(e -> showStartMenu());
        
        setupContent.getChildren().addAll(setupLabel, difficultyBox, mapBox, startGameBtn, backBtn);
        setupLayout.setCenter(setupContent);
        
        Scene setupScene = new Scene(setupLayout, 800, 600);
        primaryStage.setScene(setupScene);
    }
    
    private void startGame(String difficulty, String mapName) {
        // Initialize game engine
        gameEngine = new GameEngine(difficulty, mapName);
        
        // Initialize game UI
        gameUI = new GameUI(gameEngine);
        
        // Create game scene
        Scene gameScene = new Scene(gameUI.getRoot(), 1200, 800);
        primaryStage.setScene(gameScene);
        
        // Start the game
        gameEngine.startGame();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
} 