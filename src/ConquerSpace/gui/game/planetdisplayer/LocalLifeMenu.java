package ConquerSpace.gui.game.planetdisplayer;

import ConquerSpace.game.life.LifeTrait;
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

    private DefaultListModel<LocalLife> localLifeListModel;
    private JList<LocalLife> localLifeList;

    private JPanel localLifeInfo;
    private JLabel lifeName;
    private JLabel lifeBiomass;
    private JLabel reproductionRate;
    
    private DefaultListModel<LifeTrait> lifeTraitListModel;
    private JList<LifeTrait> lifeTraitList;
    public LocalLifeMenu(Planet p, Civilization c) {
        this.p = p;
        localLifeListModel = new DefaultListModel<>();
        localLifeList = new JList<>(localLifeListModel);
        localLifeList.addListSelectionListener(l -> {
            if(localLifeList.getSelectedValue() != null) {
                LocalLife ll = localLifeList.getSelectedValue();
                lifeName.setText(ll.getSpecies().getName());
                lifeBiomass.setText(ll.getBiomass() + " tons of biomass");
                reproductionRate.setText(ll.getSpecies().getBaseBreedingRate() + " next month");
                
                lifeTraitListModel.clear();
                for(LifeTrait lt : ll.getSpecies().lifeTraits) {
                    lifeTraitListModel.addElement(lt);
                }
            }
        });
        update();

        localLifeInfo = new JPanel();
        localLifeInfo.setLayout(new VerticalFlowLayout());
        lifeName = new JLabel();
        lifeBiomass = new JLabel();
        reproductionRate = new JLabel();
        
        lifeTraitListModel = new DefaultListModel<>();
        lifeTraitList = new JList<>(lifeTraitListModel);
        
        localLifeInfo.add(lifeName);
        localLifeInfo.add(lifeBiomass);
        localLifeInfo.add(reproductionRate);
        localLifeInfo.add(new JScrollPane(lifeTraitList));

        add(new JScrollPane(localLifeList));
        add(localLifeInfo);
    }

    public void update() {
        for (LocalLife life : p.localLife) {
            localLifeListModel.addElement(life);
        }
    }
}
