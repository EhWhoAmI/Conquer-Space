package ConquerSpace.game.universe;

/**
 *
 * @author Zyun
 */
public class Star {
    private StarTypes type;
    private int starSize;
    private int id;

    public Star(StarTypes type, int starSize, int id) {
        this.type = type;
        this.starSize = starSize;
        this.id = id;
    }
}
