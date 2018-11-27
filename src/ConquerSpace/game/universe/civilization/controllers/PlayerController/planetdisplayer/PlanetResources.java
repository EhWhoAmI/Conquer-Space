package ConquerSpace.game.universe.civilization.controllers.PlayerController.planetdisplayer;

import ConquerSpace.game.universe.civilization.controllers.LimitedPlanet;
import ConquerSpace.game.universe.civilization.controllers.LimitedPlanetSector;
import ConquerSpace.game.universe.resources.Resource;
import ConquerSpace.game.universe.spaceObjects.Planet;
import ConquerSpace.game.universe.spaceObjects.pSectors.PlanetSector;
import com.alee.extended.layout.VerticalFlowLayout;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

/**
 *
 * @author Zyun
 */
public class PlanetResources extends JPanel {

    JList<String> resourceList;

    public PlanetResources(LimitedPlanet p) {
        setLayout(new VerticalFlowLayout(5, 3));
        DefaultListModel<String> dataModel = new DefaultListModel<>();
        for (LimitedPlanetSector planetSector : p.getPlanetSectors()) {
            for (Resource s : planetSector.getResources()) {
                if (!dataModel.contains(s.name)) {
                    dataModel.addElement(s.name);
                }
            }
        }
        resourceList = new JList<>(dataModel);
        JScrollPane resourceScrollPane = new JScrollPane(resourceList);
        resourceScrollPane.setBorder(new TitledBorder("Resources"));
        //resourceScrollPane.setSize(100, resourceList.getHeight());
        add(resourceScrollPane);
    }

}
