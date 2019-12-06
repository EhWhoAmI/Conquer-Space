package ConquerSpace.gui.game.planetdisplayer;

import ConquerSpace.game.buildings.Building;
import ConquerSpace.game.buildings.BuildingBuilding;
import ConquerSpace.game.buildings.CityDistrict;
import ConquerSpace.gui.game.planetdisplayer.buildings.BuildingBuildingViewMenu;
import ConquerSpace.gui.game.planetdisplayer.buildings.PopulationStorageViewMenu;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author zyunl
 */
public class BuildingInfoContainer extends JPanel {

    public BuildingInfoContainer(Building building) {
        //Get the building and set the thingy
        if (building instanceof BuildingBuilding) {
            BuildingBuildingViewMenu menu = new BuildingBuildingViewMenu((BuildingBuilding) building);
            add(menu);
        } else if (building instanceof CityDistrict) {
            PopulationStorageViewMenu menu = new PopulationStorageViewMenu((CityDistrict) building);
            add(menu);
        } else {
            add(new JLabel(building.toString()));
        }
    }
}
