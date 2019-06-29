package ConquerSpace.game.universe.spaceObjects.terrain;

import java.awt.Color;
import java.util.HashMap;

/**
 *
 * @author zyunl
 */
public class TerrainColoring {

    public static final int NUMBER_OF_COLORS = 3;

    public static HashMap<Float, Color> getTerrainColoring(int i) {
        HashMap<Float, Color> colors = new HashMap<>();

        switch (i) {
            case 0:
                colors.put(-1f, new Color(69, 24, 4));
                colors.put(-0.25f, new Color(193, 68, 14));
                colors.put(0.25f, new Color(231, 125, 17));
                colors.put(0.75f, new Color(253, 166, 0));
                colors.put(0.9f, new Color(240, 231, 231));
                break;
            case 1:
                colors.put(-1f, new Color(80, 78, 81));
                colors.put(0f, new Color(151, 151, 159));
                colors.put(0.5f, new Color(206, 204, 209));
                colors.put(1f, new Color(181, 167, 167));
                break;
            case 2:
                colors.put(-1f, new Color(49, 48, 46));
                colors.put(0.25f, new Color(148, 144, 141));
                colors.put(0.5f, new Color(218, 217, 215));
                colors.put(0.7f, new Color(240, 240, 240));
                colors.put(1f, new Color(195, 194, 190));
        }
        return colors;
    }
}
