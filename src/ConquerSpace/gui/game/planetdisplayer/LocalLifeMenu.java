package ConquerSpace.gui.game.planetdisplayer;

import ConquerSpace.game.life.LocalLife;
import ConquerSpace.game.universe.civilization.Civilization;
import ConquerSpace.game.universe.spaceObjects.Planet;
import com.alee.extended.layout.VerticalFlowLayout;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 *
 * @author zyunl
 */
public class LocalLifeMenu extends JPanel {

    private Planet p;

    private DefaultListModel<LocalLife> localLifeDefaultListModel;
    private JList<LocalLife> localLifeList;

    private JPanel localLifeInfo;
    private JLabel lifeName;
    private JLabel lifeBiomass;

    public LocalLifeMenu(Planet p, Civilization c) {
        this.p = p;
        localLifeDefaultListModel = new DefaultListModel<>();
        localLifeList = new JList<>(localLifeDefaultListModel);
        localLifeList.addListSelectionListener(l -> {
            if(localLifeList.getSelectedValue() != null) {
                LocalLife ll = localLifeList.getSelectedValue();
                lifeName.setText(ll.getName());
                lifeBiomass.setText(ll.getBiomass() + " tons of biomass");
            }
        });
        update();

        localLifeInfo = new JPanel();
        localLifeInfo.setLayout(new VerticalFlowLayout());
        lifeName = new JLabel();
        lifeBiomass = new JLabel();
        
        localLifeInfo.add(lifeName);
        localLifeInfo.add(lifeBiomass);

        add(new JScrollPane(localLifeList));
        add(localLifeInfo);
    }

    public void update() {
        for (LocalLife life : p.localLife) {
            localLifeDefaultListModel.addElement(life);
        }
    }
}
