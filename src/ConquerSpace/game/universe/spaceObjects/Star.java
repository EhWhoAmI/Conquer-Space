package ConquerSpace.game.universe.spaceObjects;
/**
 * A star.
 * @author Zyun
 */
public class Star extends SpaceObject{
    public int type;
    public int starSize;
    public int id;
    private int parentStarSystem;
    private int parentSector;
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
     *
     * @return
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
        return(builder.toString());
    }

    void setParentSector(int parentSector) {
        this.parentSector = parentSector;
    }

    void setParentStarSystem(int parentStarSystem) {
        this.parentStarSystem = parentStarSystem;
    }

    public int getParentStarSystem() {
        return parentStarSystem;
    }

    public int getParentSector() {
        return parentSector;
    }
    
    
}
