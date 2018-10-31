package ConquerSpace.game.universe.spaceObjects;

import ConquerSpace.game.UniversePath;

/**
 * A star.
 *
 * @author Zyun
 */
public final class Star extends SpaceObject {

    public int type;
    public int starSize;
    private int parentStarSystem;
    private int parentSector;

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
     * Set parent sector
     * @param parentSector Parent sector id
     */
    void setParentSector(int parentSector) {
        this.parentSector = parentSector;
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

    /**
     * Get parent sector id
     * @return parent sector id
     */
    public int getParentSector() {
        return parentSector;
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

    public int getType() {
        return type;
    }

    public int getStarSize() {
        return starSize;
    }
}
