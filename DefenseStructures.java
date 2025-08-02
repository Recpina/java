import java.util.Random;

public class SpeedBump {
    private double x, y;
    private long duration; // Duration in nanoseconds
    private long startTime;
    private boolean isExpired;
    private double speedReductionFactor = 0.5; // 50% speed reduction
    
    public SpeedBump(double x, double y) {
        this.x = x;
        this.y = y;
        this.duration = 10_000_000_000L; // 10 seconds in nanoseconds
        this.startTime = System.nanoTime();
        this.isExpired = false;
    }
    
    public void update(long deltaTime) {
        long currentTime = System.nanoTime();
        if (currentTime - startTime >= duration) {
            isExpired = true;
        }
    }
    
    public boolean isExpired() {
        return isExpired;
    }
    
    public double getSpeedReductionFactor() {
        return speedReductionFactor;
    }
    
    public double getX() { return x; }
    public double getY() { return y; }
    
    public double getDistanceTo(double targetX, double targetY) {
        double dx = x - targetX;
        double dy = y - targetY;
        return Math.sqrt(dx * dx + dy * dy);
    }
}

class Bomb {
    private double x, y;
    private int damage;
    private double explosionRadius;
    private boolean isExploded;
    private long explosionDelay; // Time before explosion
    private long startTime;
    
    public Bomb(double x, double y) {
        this.x = x;
        this.y = y;
        this.damage = 50;
        this.explosionRadius = 80; // pixels
        this.isExploded = false;
        this.explosionDelay = 2_000_000_000L; // 2 seconds in nanoseconds
        this.startTime = System.nanoTime();
    }
    
    public void update(long deltaTime) {
        long currentTime = System.nanoTime();
        if (currentTime - startTime >= explosionDelay && !isExploded) {
            isExploded = true;
        }
    }
    
    public boolean isExploded() {
        return isExploded;
    }
    
    public int getDamage() {
        return damage;
    }
    
    public double getExplosionRadius() {
        return explosionRadius;
    }
    
    public double getX() { return x; }
    public double getY() { return y; }
    
    public double getDistanceTo(double targetX, double targetY) {
        double dx = x - targetX;
        double dy = y - targetY;
        return Math.sqrt(dx * dx + dy * dy);
    }
}

class AntiAircraftDefense {
    private double x, y;
    private boolean isExpensive;
    private double hitChance;
    private double range;
    private double fireRate;
    private int damage;
    private long lastAttackTime;
    private Random random;
    
    public AntiAircraftDefense(double x, double y, boolean isExpensive) {
        this.x = x;
        this.y = y;
        this.isExpensive = isExpensive;
        this.hitChance = isExpensive ? 0.8 : 0.6; // 80% vs 60% hit chance
        this.range = 200; // pixels
        this.fireRate = 2.0; // attacks per second
        this.damage = 30;
        this.lastAttackTime = 0;
        this.random = new Random();
    }
    
    public void update(long deltaTime) {
        // Anti-aircraft defense doesn't need special update logic
    }
    
    public void attack(Enemy target) {
        if (!target.isAirUnit()) return; // Can only attack air units
        
        long currentTime = System.nanoTime();
        long timeSinceLastAttack = currentTime - lastAttackTime;
        double attackInterval = 1_000_000_000.0 / fireRate; // Convert to nanoseconds
        
        if (timeSinceLastAttack >= attackInterval) {
            // Check hit chance
            if (random.nextDouble() <= hitChance) {
                target.takeDamage(damage);
            }
            lastAttackTime = currentTime;
        }
    }
    
    public double getDistanceTo(Enemy enemy) {
        double dx = x - enemy.getX();
        double dy = y - enemy.getY();
        return Math.sqrt(dx * dx + dy * dy);
    }
    
    public double getRange() {
        return range;
    }
    
    public boolean isExpensive() {
        return isExpensive;
    }
    
    public double getHitChance() {
        return hitChance;
    }
    
    public double getX() { return x; }
    public double getY() { return y; }
} 