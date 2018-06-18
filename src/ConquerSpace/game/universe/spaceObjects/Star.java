package ConquerSpace.game.universe.spaceObjects;

/**
 * A star.
 *
 * @author Zyun
 */
public class Star extends SpaceObject {

    public int type;
    public int starSize;
    private int parentStarSystem;
    private int parentSector;

    int id;

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

}
