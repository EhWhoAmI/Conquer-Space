package ConquerSpace.game.universe.spaceObjects;

import ConquerSpace.game.universe.UniversePath;

/**
 * A star.
 *
 * @author Zyun
 */
public class Star extends SpaceObject {

    public int type;
    /**
     * Radius of star in kilometers.
     * Largest star can be about 1.7k solar radii, where the sun is about 695,700km.
     * Neutron stars can be tiny, you know.
     */
    public int starSize;
    private int parentStarSystem;

    int id;
    
    private int ownerID = -1;

    /**
     * @see StarTypes
     * @param type type of star
     * @param starSize size of star
     * @param id id
     */
    public Star(int type, int starSize, int id) {
        this.type = type;
        this.starSize = starSize;
        this.id = id;
    }

    /**
     * Get the readable string of this star
     * @return this star to readable string
     */
    public String toReadableString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Star: " + id + "(type=");
        switch (type) {
            case StarTypes.RED:
                builder.append("red");
                break;
            case StarTypes.BLUE:
                builder.append("blue");
                break;
            case StarTypes.BROWN:
                builder.append("brown");
                break;
            case StarTypes.YELLOW:
                builder.append("yellow");
        }
        builder.append("Size=" + starSize + ")\n");
        return (builder.toString());
    }

    /**
     * Set parent star system
     * @param parentStarSystem parent star system id.
     */
    void setParentStarSystem(int parentStarSystem) {
        this.parentStarSystem = parentStarSystem;
    }

    /**
     * Get parent star system id
     * @return parent star system id
     */
    public int getParentStarSystem() {
        return parentStarSystem;
    }

    public int getId() {
        return id;
    }
    
    public UniversePath getUniversePath() {
        return (new UniversePath(parentStarSystem, id, true));
    }
    
    public int getOwnerID(){
        return ownerID;
    }
    
    public void setOwnerID(int id) {
        this.ownerID = id;
    }
}
