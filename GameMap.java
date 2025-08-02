import java.util.List;
import java.util.ArrayList;
import java.awt.Point;

public class GameMap {
    private String mapName;
    private int width;
    private int height;
    private TileType[][] tiles;
    private List<Point> enemyPath;
    private List<Point> towerPlacementSites;
    private List<Point> speedBumpPlacementSites;
    private List<Point> bombPlacementSites;
    private List<Point> antiAircraftPlacementSites;
    
    public enum TileType {
        GRASS, PATH, WATER, SAND, CONSTRUCTION_SITE, BOMB_SITE
    }
    
    public GameMap(String mapName) {
        this.mapName = mapName;
        this.width = GameEngine.MAP_WIDTH / GameEngine.TILE_SIZE;
        this.height = GameEngine.MAP_HEIGHT / GameEngine.TILE_SIZE;
        this.tiles = new TileType[width][height];
        
        initializeMap();
    }
    
    private void initializeMap() {
        // Initialize all tiles as grass
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                tiles[x][y] = TileType.GRASS;
            }
        }
        
        // Create enemy path
        createEnemyPath();
        
        // Create placement sites
        createPlacementSites();
        
        // Add some water and sand areas
        addWaterAndSand();
    }
    
    private void createEnemyPath() {
        enemyPath = new ArrayList<>();
        
        // Create a winding path from left to right
        int startY = height / 2;
        
        // Start from left edge
        for (int x = 0; x < width / 4; x++) {
            enemyPath.add(new Point(x, startY));
            tiles[x][startY] = TileType.PATH;
        }
        
        // Turn down
        for (int y = startY; y < startY + 3; y++) {
            enemyPath.add(new Point(width / 4, y));
            tiles[width / 4][y] = TileType.PATH;
        }
        
        // Go right
        for (int x = width / 4; x < width * 3 / 4; x++) {
            enemyPath.add(new Point(x, startY + 3));
            tiles[x][startY + 3] = TileType.PATH;
        }
        
        // Turn down again
        for (int y = startY + 3; y < startY + 6; y++) {
            enemyPath.add(new Point(width * 3 / 4, y));
            tiles[width * 3 / 4][y] = TileType.PATH;
        }
        
        // Go right to end
        for (int x = width * 3 / 4; x < width; x++) {
            enemyPath.add(new Point(x, startY + 6));
            tiles[x][startY + 6] = TileType.PATH;
        }
    }
    
    private void createPlacementSites() {
        towerPlacementSites = new ArrayList<>();
        speedBumpPlacementSites = new ArrayList<>();
        bombPlacementSites = new ArrayList<>();
        antiAircraftPlacementSites = new ArrayList<>();
        
        // Add tower placement sites around the path
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (tiles[x][y] == TileType.GRASS) {
                    // Check if adjacent to path
                    if (isAdjacentToPath(x, y)) {
                        towerPlacementSites.add(new Point(x, y));
                        tiles[x][y] = TileType.CONSTRUCTION_SITE;
                    }
                }
            }
        }
        
        // Add some bomb sites on the path
        for (int i = 2; i < enemyPath.size() - 2; i += 3) {
            Point pathPoint = enemyPath.get(i);
            bombPlacementSites.add(pathPoint);
            tiles[pathPoint.x][pathPoint.y] = TileType.BOMB_SITE;
        }
        
        // Add speed bump sites on the path
        for (int i = 1; i < enemyPath.size() - 1; i += 2) {
            Point pathPoint = enemyPath.get(i);
            if (tiles[pathPoint.x][pathPoint.y] == TileType.PATH) {
                speedBumpPlacementSites.add(pathPoint);
            }
        }
        
        // Add anti-aircraft placement sites (can be placed anywhere)
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (tiles[x][y] == TileType.GRASS) {
                    antiAircraftPlacementSites.add(new Point(x, y));
                }
            }
        }
    }
    
    private void addWaterAndSand() {
        // Add water on the right side
        for (int x = width * 3 / 4; x < width; x++) {
            for (int y = 0; y < height / 3; y++) {
                tiles[x][y] = TileType.WATER;
            }
        }
        
        // Add sand area
        for (int x = width * 2 / 3; x < width * 3 / 4; x++) {
            for (int y = height * 2 / 3; y < height; y++) {
                tiles[x][y] = TileType.SAND;
            }
        }
    }
    
    private boolean isAdjacentToPath(int x, int y) {
        // Check if the tile is adjacent to a path tile
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                int nx = x + dx;
                int ny = y + dy;
                if (nx >= 0 && nx < width && ny >= 0 && ny < height) {
                    if (tiles[nx][ny] == TileType.PATH) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    public boolean canPlaceTower(double x, double y) {
        int tileX = (int) (x / GameEngine.TILE_SIZE);
        int tileY = (int) (y / GameEngine.TILE_SIZE);
        
        if (tileX < 0 || tileX >= width || tileY < 0 || tileY >= height) {
            return false;
        }
        
        return tiles[tileX][tileY] == TileType.CONSTRUCTION_SITE;
    }
    
    public boolean canPlaceSpeedBump(double x, double y) {
        int tileX = (int) (x / GameEngine.TILE_SIZE);
        int tileY = (int) (y / GameEngine.TILE_SIZE);
        
        if (tileX < 0 || tileX >= width || tileY < 0 || tileY >= height) {
            return false;
        }
        
        return tiles[tileX][tileY] == TileType.PATH;
    }
    
    public boolean canPlaceBomb(double x, double y) {
        int tileX = (int) (x / GameEngine.TILE_SIZE);
        int tileY = (int) (y / GameEngine.TILE_SIZE);
        
        if (tileX < 0 || tileX >= width || tileY < 0 || tileY >= height) {
            return false;
        }
        
        return tiles[tileX][tileY] == TileType.BOMB_SITE;
    }
    
    public boolean canPlaceAntiAircraft(double x, double y) {
        int tileX = (int) (x / GameEngine.TILE_SIZE);
        int tileY = (int) (y / GameEngine.TILE_SIZE);
        
        if (tileX < 0 || tileX >= width || tileY < 0 || tileY >= height) {
            return false;
        }
        
        return tiles[tileX][tileY] == TileType.GRASS;
    }
    
    public Point getNextPathPoint(int currentIndex) {
        if (currentIndex >= 0 && currentIndex < enemyPath.size() - 1) {
            return enemyPath.get(currentIndex + 1);
        }
        return null;
    }
    
    public Point getPathStart() {
        return enemyPath.get(0);
    }
    
    public Point getPathEnd() {
        return enemyPath.get(enemyPath.size() - 1);
    }
    
    public List<Point> getEnemyPath() {
        return new ArrayList<>(enemyPath);
    }
    
    public TileType getTileType(int x, int y) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            return tiles[x][y];
        }
        return TileType.GRASS;
    }
    
    public int getWidth() {
        return width;
    }
    
    public int getHeight() {
        return height;
    }
    
    public String getMapName() {
        return mapName;
    }
} 