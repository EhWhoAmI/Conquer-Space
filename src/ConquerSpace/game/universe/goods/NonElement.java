package ConquerSpace.game.universe.goods;

import java.util.HashMap;

/**
 *
 * @author zyunl
 */
public class NonElement extends Good{
    public HashMap<Good, Integer> recipie;

    public NonElement(String name, int id, double volume, double mass) {
        super(name, id, volume, mass);
        recipie = new HashMap<>();
    }
}
