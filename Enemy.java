import java.awt.Point;
import java.util.List;

public abstract class Enemy {
    protected double x, y;
    protected double speed;
    protected int health;
    protected int maxHealth;
    protected int damage;
    protected boolean isDead;
    protected boolean hasReachedCastle;
    protected int currentPathIndex;
    protected List<Point> path;
    protected boolean isAirUnit;
    
    public Enemy(double x, double y, double speed, int health, int damage, boolean isAirUnit) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.health = health;
        this.maxHealth = health;
        this.damage = damage;
        this.isDead = false;
        this.hasReachedCastle = false;
        this.currentPathIndex = 0;
        this.isAirUnit = isAirUnit;
    }
    
    public void update(long deltaTime) {
        if (isDead || hasReachedCastle) return;
        
        // Move along path
        moveAlongPath(deltaTime);
        
        // Check if reached castle
        if (currentPathIndex >= path.size() - 1) {
            hasReachedCastle = true;
        }
    }
    
    protected void moveAlongPath(long deltaTime) {
        if (currentPathIndex >= path.size() - 1) return;
        
        Point targetPoint = path.get(currentPathIndex + 1);
        double targetX = targetPoint.x * GameEngine.TILE_SIZE + GameEngine.TILE_SIZE / 2.0;
        double targetY = targetPoint.y * GameEngine.TILE_SIZE + GameEngine.TILE_SIZE / 2.0;
        
        double dx = targetX - x;
        double dy = targetY - y;
        double distance = Math.sqrt(dx * dx + dy * dy);
        
        if (distance < 5) {
            // Reached current path point, move to next
            currentPathIndex++;
            x = targetX;
            y = targetY;
        } else {
            // Move towards target
            double moveDistance = speed * deltaTime / 1_000_000_000.0; // Convert nanoseconds to seconds
            double moveRatio = Math.min(moveDistance / distance, 1.0);
            
            x += dx * moveRatio;
            y += dy * moveRatio;
        }
    }
    
    public void setPath(List<Point> path) {
        this.path = path;
    }
    
    public void takeDamage(int damage) {
        health -= damage;
        if (health <= 0) {
            health = 0;
            isDead = true;
        }
    }
    
    public double getDistanceTo(Enemy other) {
        double dx = x - other.x;
        double dy = y - other.y;
        return Math.sqrt(dx * dx + dy * dy);
    }
    
    public double getDistanceTo(double targetX, double targetY) {
        double dx = x - targetX;
        double dy = y - targetY;
        return Math.sqrt(dx * dx + dy * dy);
    }
    
    // Getters
    public double getX() { return x; }
    public double getY() { return y; }
    public double getSpeed() { return speed; }
    public int getHealth() { return health; }
    public int getMaxHealth() { return maxHealth; }
    public int getDamage() { return damage; }
    public boolean isDead() { return isDead; }
    public boolean hasReachedCastle() { return hasReachedCastle; }
    public boolean isAirUnit() { return isAirUnit; }
    public int getCurrentPathIndex() { return currentPathIndex; }
    
    // Abstract methods
    public abstract String getType();
    public abstract void attack(DefenseTower tower);
}

class Soldier extends Enemy {
    public Soldier(double x, double y) {
        super(x, y, 50.0, 30, 10, false); // speed: 50 pixels/second, health: 30, damage: 10
    }
    
    @Override
    public String getType() {
        return "Soldier";
    }
    
    @Override
    public void attack(DefenseTower tower) {
        // Soldiers don't attack towers, they just try to reach the castle
    }
}

class Tank extends Enemy {
    public Tank(double x, double y) {
        super(x, y, 30.0, 100, 25, false); // speed: 30 pixels/second, health: 100, damage: 25
    }
    
    @Override
    public String getType() {
        return "Tank";
    }
    
    @Override
    public void attack(DefenseTower tower) {
        // Tanks can attack towers while moving
        double distance = getDistanceTo(tower.getX(), tower.getY());
        if (distance <= 60) { // Attack range
            tower.takeDamage(damage);
        }
    }
}

class Plane extends Enemy {
    public Plane(double x, double y) {
        super(x, y, 80.0, 50, 40, true); // speed: 80 pixels/second, health: 50, damage: 40
    }
    
    @Override
    public String getType() {
        return "Plane";
    }
    
    @Override
    public void attack(DefenseTower tower) {
        // Planes can attack towers from above
        double distance = getDistanceTo(tower.getX(), tower.getY());
        if (distance <= 80) { // Attack range
            tower.takeDamage(damage);
        }
    }
} 