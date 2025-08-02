import java.util.Random;

public class DefenseTower {
    private double x, y;
    private TowerType type;
    private int health;
    private int maxHealth;
    private double range;
    private double fireRate; // attacks per second
    private int damage;
    private long lastAttackTime;
    private boolean isDead;
    private Random random;
    
    public enum TowerType {
        FAST(80, 50, 15, 100),      // Fast tower: high fire rate, low health
        POWERFUL(120, 30, 25, 150); // Powerful tower: low fire rate, high health
        
        private final int cost;
        private final double fireRate;
        private final int damage;
        private final int health;
        
        TowerType(int cost, double fireRate, int damage, int health) {
            this.cost = cost;
            this.fireRate = fireRate;
            this.damage = damage;
            this.health = health;
        }
        
        public int getCost() { return cost; }
        public double getFireRate() { return fireRate; }
        public int getDamage() { return damage; }
        public int getHealth() { return health; }
    }
    
    public DefenseTower(double x, double y, TowerType type) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.maxHealth = type.getHealth();
        this.health = maxHealth;
        this.range = 120; // Range in pixels
        this.fireRate = type.getFireRate();
        this.damage = type.getDamage();
        this.lastAttackTime = 0;
        this.isDead = false;
        this.random = new Random();
    }
    
    public void update(long deltaTime) {
        if (isDead) return;
        
        // Check if tower is destroyed
        if (health <= 0) {
            health = 0;
            isDead = true;
        }
    }
    
    public void attack(Enemy target) {
        if (isDead) return;
        
        long currentTime = System.nanoTime();
        long timeSinceLastAttack = currentTime - lastAttackTime;
        double attackInterval = 1_000_000_000.0 / fireRate; // Convert to nanoseconds
        
        if (timeSinceLastAttack >= attackInterval) {
            // Perform attack
            target.takeDamage(damage);
            lastAttackTime = currentTime;
        }
    }
    
    public void takeDamage(int damage) {
        health -= damage;
        if (health <= 0) {
            health = 0;
            isDead = true;
        }
    }
    
    public double getDistanceTo(Enemy enemy) {
        double dx = x - enemy.getX();
        double dy = y - enemy.getY();
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
    public TowerType getType() { return type; }
    public int getHealth() { return health; }
    public int getMaxHealth() { return maxHealth; }
    public double getRange() { return range; }
    public double getFireRate() { return fireRate; }
    public int getDamage() { return damage; }
    public boolean isDead() { return isDead; }
    
    // Health percentage for UI
    public double getHealthPercentage() {
        return (double) health / maxHealth;
    }
} 