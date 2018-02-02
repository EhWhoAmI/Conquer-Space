package ConquerSpace.game.universe.spaceObjects;

import ConquerSpace.game.GameObject;
/**
 * A star.
 * @author Zyun
 */
public class Star extends GameObject{
    private int type;
    private int starSize;
    private int id;

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
}
