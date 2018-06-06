package ConquerSpace.game.universe.civilizations;

/**
 *
 * @author Zyun
 */
public class VisionTypes {
    /**
     * Doesn't know it is even there.
     */
    public static final int UNDISCOVERED = 0;
    /**
     * Knows it exists, but nothing else.
     */
    public static final int EXISTS = 1;
    /**
     * Knows what is inside, planets and all, but not resources and stuff like that.
     */
    public static final int KNOWS_INTERIOR = 2;
    /**
     * Knows what is inside, planets and all.
     */
    public static final int KNOWS_DETAILS = 3;
    /**
     * Knows whatever is going on, ships and stuff...
     */
    public static final int KNOWS_ALL = 4;
}
