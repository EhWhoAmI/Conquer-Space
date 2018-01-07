package ConquerSpace.game.universe;

/**
 *
 * @author Zyun
 */
public class Star {
    private int type;
    private int starSize;
    private int id;

    public Star(int type, int starSize, int id) {
        this.type = type;
        this.starSize = starSize;
        this.id = id;
    }

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
