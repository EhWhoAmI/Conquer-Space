package ConquerSpace.game.universe.civilization.controllers.PlayerController.planetdisplayer;

import ConquerSpace.game.universe.spaceObjects.Planet;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Display sectors and stuff.
 *
 * @author Zyun
 */
public class PlanetOverview extends JPanel {

    private JLabel planetName;
    private JLabel planetPath;

    public PlanetOverview(Planet p) {
        //If name is nothing, then call it unnamed planet
        planetName = new JLabel();
        planetPath = new JLabel();

        //Init planetname
        if (p.getName() == "") {

            planetName.setText("Unnamed Planet");
        } else {
            planetName.setText(p.getName());
        }

        //Init planetPath
        StringBuilder name = new StringBuilder();
        name.append("Sector ");
        name.append("" + p.getParentSector());
        name.append(" Star System ");
        name.append("" + p.getParentStarSystem());
        planetPath.setText(name.toString());
        
        //Add components
        add(planetName);
        add(planetPath);
    }
}
