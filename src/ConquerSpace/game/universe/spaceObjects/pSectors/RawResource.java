package ConquerSpace.game.universe.spaceObjects.pSectors;

/**
 * Resource that is not being collected
 * @author Zyun
 */
public class RawResource extends PlanetSector{
    private int resources;
    private int resourceType;

    public RawResource(int id, int resources, int resourceType) {
        super(id);
        this.resources = resources;
        this.resourceType = resourceType;
    }

    public int getResourceType() {
        return resourceType;
    }

    public int getResources() {
        return resources;
    }

    @Override
    public String toReadableString() {
        return ("<Raw resource: type = " + resourceType + " resources = " + resources + ">"); 
    }
    
    
}
