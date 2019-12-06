package ConquerSpace.gui.game.planetdisplayer.buildings;

import ConquerSpace.game.buildings.CityDistrict;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author zyunl
 */
public class PopulationStorageViewMenu extends JPanel{
    public PopulationStorageViewMenu(CityDistrict district) {
        add(new JLabel(district.getCity().getName()));
    }
}    
