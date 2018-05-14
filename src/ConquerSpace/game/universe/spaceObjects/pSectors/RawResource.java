package ConquerSpace.game.universe.spaceObjects.pSectors;

import ConquerSpace.Globals;
import ConquerSpace.game.universe.resources.RawResourceTypes;
import ConquerSpace.game.universe.resources.Resource;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.ListModel;
import javax.swing.border.TitledBorder;

/**
 * Resource that is not being collected
 * @author Zyun
 */
public class RawResource extends PlanetSector{
    public ArrayList<Resource> resources;

    //Raw Resource will ALWAYS have no owner
    public RawResource(int id) {
        super(id);
        resources = new ArrayList<>();
    }
    
    public void addResource(int type, int amount) {
        resources.add(new Resource(type, amount));
    }

    @Override
    public JPanel getInfoPanel() {
        JPanel root = new JPanel();
        JTabbedPane tabbedPane = new JTabbedPane();
        
        JPanel infoPanel = new JPanel();
        JLabel title = new JLabel("Raw Resource");
        JLabel idLabel = new JLabel("ID: " + this.getId());
        JLabel ownerLabel = new JLabel("Owner: No owner");
        JLabel resourceTitle = new JLabel("Resources");
        
        DefaultListModel<String> resourceList = new DefaultListModel<>();

        for(Resource res : resources) {
            switch (res.getType()) {
                case RawResourceTypes.GAS:
                    resourceList.addElement("Gas");
                    break;
                case RawResourceTypes.ROCK:
                    resourceList.addElement("Rock");
                    break;
                case RawResourceTypes.METAL:
                    resourceList.addElement("Metal");
                    break;
                default:
                    break;
            }
        }
        
        JList<String> resources = new JList<>(resourceList);
        resources.setBorder(new TitledBorder("Resources"));
        JScrollPane resourceScrollPane = new JScrollPane(resources);
        
        infoPanel.add(idLabel);
        infoPanel.add(ownerLabel);
        infoPanel.add(resourceScrollPane);
        infoPanel.setLayout(new GridLayout(3,1));
        tabbedPane.add("Info", infoPanel);
        root.add(title);
        root.add(tabbedPane);
        return root;
    }
}
