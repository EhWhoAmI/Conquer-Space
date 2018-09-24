package ConquerSpace.game.universe.civilization.vision;

/**
 * A point where vision comes from, where you can start vision.
 *
 * @author Zyun
 */
public interface VisionPoint {

    /**
     * Range in light years.
     *
     * @return range.
     */
    public int getRange();
    public int getCivilization();
    
    public static final int GAMMA = 0;
    public static final int XRAY = 1;
    public static final int ULTRAVIOLET = 2;
    public static final int VISIBLE = 3;
    public static final int INFRARED = 4;
    public static final int TERAHERTZ = 5;
    public static final int MICROWAVE = 6;
    public static final int RADIO_WAVE = 7;
}
