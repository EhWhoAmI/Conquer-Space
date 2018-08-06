package ConquerSpace.game.universe.spaceObjects.pSectors;

import ConquerSpace.game.universe.resources.Resource;
import com.alee.extended.layout.VerticalFlowLayout;
import java.awt.Dimension;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.border.TitledBorder;

/**
 * Resource that is not being collected
 * @author Zyun
 */
public class RawResource extends PlanetSector{
    //Raw Resource will ALWAYS have no owner
    public RawResource() {
    }

    @Override
    public JPanel getInfoPanel() {
        JPanel root = new JPanel(new VerticalFlowLayout(5, 3));
        JTabbedPane tabbedPane = new JTabbedPane();
        
        JPanel infoPanel = new JPanel();
        JLabel title = new JLabel("Raw Resource");
        JLabel idLabel = new JLabel("ID: " + this.getId());
        JLabel amountLabel = new JLabel("Amount of resources: " + ((resources.size() > 0) ? resources.size():"none"));
        //JLabel resourceTitle = new JLabel("Resources");
        
        DefaultListModel<String> resourceList = new DefaultListModel<>();

        for(Resource res : resources) {
            resourceList.addElement(res.name);
        }
        
        JList<String> resourcesList = new JList<>(resourceList);
        JScrollPane resourceScrollPane = new JScrollPane(resourcesList);
        resourceScrollPane.setBorder(new TitledBorder("Resources"));
        
        infoPanel.add(idLabel);
        infoPanel.add(amountLabel);
        infoPanel.add(resourceScrollPane);
        
        infoPanel.setLayout(new VerticalFlowLayout(5, 3));
        tabbedPane.add("Info", infoPanel);
        root.add(title);
        root.add(tabbedPane);
        return root;
    }
}
