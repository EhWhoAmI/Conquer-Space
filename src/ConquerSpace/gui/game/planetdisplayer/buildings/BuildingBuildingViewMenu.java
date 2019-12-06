package ConquerSpace.gui.game.planetdisplayer.buildings;

import ConquerSpace.game.buildings.BuildingBuilding;
import com.alee.extended.layout.VerticalFlowLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author zyunl
 */
public class BuildingBuildingViewMenu extends JPanel{
    private JLabel timeLeft;
    private JLabel peopleWorking;
    public BuildingBuildingViewMenu(BuildingBuilding building) {
        setLayout(new VerticalFlowLayout());
        //Get the building
        add(new JLabel("Constructing a " + building.getToBuild().toString()));
        timeLeft = new JLabel("Construction progress: " + building.getLength() + ", estimated to be x months");
        peopleWorking = new JLabel("Constructors: " + building.jobs.size());
        add(timeLeft);
        add(peopleWorking);
    }
}
