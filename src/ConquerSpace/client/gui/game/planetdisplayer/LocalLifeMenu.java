/*
 * Conquer Space - Conquer Space!
 * Copyright (C) 2019 EhWhoAmI
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package ConquerSpace.client.gui.game.planetdisplayer;

import static ConquerSpace.ConquerSpace.LOCALE_MESSAGES;
import ConquerSpace.common.game.life.LifeTrait;
import ConquerSpace.common.game.life.LocalLife;
import ConquerSpace.common.game.universe.bodies.Planet;
import com.alee.extended.layout.HorizontalFlowLayout;
import com.alee.extended.layout.VerticalFlowLayout;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 *
 * @author EhWhoAmI
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
    
    public LocalLifeMenu(Planet p) {
        this.p = p;
        localLifeListModel = new DefaultListModel<>();
        localLifeList = new JList<>(localLifeListModel);
        localLifeList.addListSelectionListener(l -> {
            if(localLifeList.getSelectedValue() != null) {
                LocalLife ll = localLifeList.getSelectedValue();
                lifeName.setText(ll.getSpecies().getName());
                lifeBiomass.setText(LOCALE_MESSAGES.getMessage("game.planet.locallife.biomass", (double) ll.getBiomass()));
                reproductionRate.setText(LOCALE_MESSAGES.getMessage("game.planet.locallife.reproduction", ll.getSpecies().getBaseBreedingRate()));
                
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

        setLayout(new HorizontalFlowLayout());
        add(new JScrollPane(localLifeList));
        add(localLifeInfo);
    }

    public void update() {
        for (LocalLife life : p.localLife) {
            localLifeListModel.addElement(life);
        }
    }
}
