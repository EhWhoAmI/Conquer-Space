package ConquerSpace.game.universe.civilization.vision;

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
     * Does not know distance of planet from star. You know, science, so you can tell
     * from the change in luminosity, but you cannot tell other stuff.
     */
    public static final int KNOWS_INTERIOR = 2;
    /**
     * Knows what is inside, planets and all.
     * Knows distance from the planet from star.
     */
    public static final int KNOWS_DETAILS = 3;
    /**
     * Knows whatever is going on, ships and stuff...
     */
    public static final int KNOWS_ALL = 4;
}
