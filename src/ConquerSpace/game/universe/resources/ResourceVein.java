package ConquerSpace.game.universe.resources;

/**
 *
 * @author zyunl
 */
public class ResourceVein {

    private Resource resourceType;
    private int resourceAmount;
    private float difficulty;
    private int x;
    private int y;
    private int radius;

    public ResourceVein(Resource resourceType, int resourceAmount) {
        this.resourceType = resourceType;
        this.resourceAmount = resourceAmount;
    }

    public Resource getResourceType() {
        return resourceType;
    }

    public float getDifficulty() {
        return difficulty;
    }

    public int getRadius() {
        return radius;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getResourceAmount() {
        return resourceAmount;
    }

    public void setResourceAmount(int resourceAmount) {
        this.resourceAmount = resourceAmount;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public void setResourceType(Resource resourceType) {
        this.resourceType = resourceType;
    }
    
    public void removeResources(int amount) {
        resourceAmount-=amount;
    }
}
