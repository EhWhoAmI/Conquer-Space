package ConquerSpace.game.universe.spaceObjects.planet;

/**
 * Resource that is not being collected
 * @author Zyun
 */
public class RawResource extends PlanetSector{
    private int resources;
    private int resourceType;

    public RawResource(int resources, int resourceType) {
        this.resources = resources;
        this.resourceType = resourceType;
    }

    public int getResourceType() {
        return resourceType;
    }

    public int getResources() {
        return resources;
    }
    
    
}
