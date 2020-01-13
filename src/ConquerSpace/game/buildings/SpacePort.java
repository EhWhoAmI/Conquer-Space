package ConquerSpace.game.buildings;

import ConquerSpace.game.universe.ships.launch.LaunchSystem;
import ConquerSpace.game.universe.ships.launch.SpacePortLaunchPad;
import java.awt.Color;
import java.util.ArrayList;

/**
 *
 * @author zyunl
 */
public class SpacePort extends Building{
    public ArrayList<SpacePortLaunchPad> launchPads = new ArrayList<>();
    private LaunchSystem system;

    public SpacePort(LaunchSystem system, int amount) {
        this.system = system;
        launchPads = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            launchPads.add(new SpacePortLaunchPad(system));
        }
    }
    
    
    @Override
    public Color getColor() {
        return Color.MAGENTA;
    }
    
     @Override
    public String getType() {
        return "Space Port";
    }
}
