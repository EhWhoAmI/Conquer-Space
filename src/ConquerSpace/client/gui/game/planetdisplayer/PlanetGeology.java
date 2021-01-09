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
import ConquerSpace.common.GameState;
import ConquerSpace.common.ObjectReference;
import ConquerSpace.common.game.resources.StoreableReference;
import ConquerSpace.common.game.resources.Stratum;
import ConquerSpace.common.game.universe.bodies.Planet;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.LayoutStyle;

/**
 *
 * @author EhWhoAmI
 */
public class PlanetGeology extends JPanel {

    private Planet planet;
    private DefaultListModel<Stratum> stratumListModel;
    private DefaultListModel<String> resourceListModel;

    private final GameState gameState;

    private JLabel depthLabel;
    private JLabel jLabel3;
    private JLabel jLabel4;
    private JLabel jLabel6;
    private JPanel jPanel2;
    private JScrollPane jScrollPane2;
    private JScrollPane jScrollPane3;
    private JLabel radiusLabel;
    private JList<String> resourceList;
    private JList<Stratum> strataList;

    /**
     * Creates new form PlanetGeology
     *
     * @param gameState
     * @param p
     */
    public PlanetGeology(GameState gameState, Planet p) {
        this.gameState = gameState;
        this.planet = p;
        stratumListModel = new DefaultListModel<>();
        for (ObjectReference stratumId : planet.strata) {
            Stratum stratum = gameState.getObject(stratumId, Stratum.class);

            stratumListModel.addElement(stratum);
        }
        resourceListModel = new DefaultListModel<>();
        initComponents();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        GridBagConstraints gridBagConstraints;

        jScrollPane2 = new JScrollPane();
        strataList = new JList<>();
        jPanel2 = new JPanel();
        jLabel3 = new JLabel();
        jLabel4 = new JLabel();
        jScrollPane3 = new JScrollPane();
        resourceList = new JList<>();
        jLabel6 = new JLabel();
        depthLabel = new JLabel();
        radiusLabel = new JLabel();

        setLayout(new GridBagLayout());

        strataList.setModel(stratumListModel);
        strataList.setName(""); // NOI18N
        strataList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                strataListMouseClicked();
            }

            public void mouseReleased(MouseEvent evt) {
                strataListMouseReleased();
            }
        });
        strataList.addListSelectionListener(l -> {
            Stratum stratum = strataList.getSelectedValue();
            itemSelected();
        });
        jScrollPane2.setViewportView(strataList);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(jScrollPane2, gridBagConstraints);

        jLabel3.setText(LOCALE_MESSAGES.getMessage("game.planet.geology.depth"));

        jLabel4.setText(LOCALE_MESSAGES.getMessage("game.planet.geology.radius"));

        resourceList.setModel(resourceListModel);
        jScrollPane3.setViewportView(resourceList);

        jLabel6.setText(LOCALE_MESSAGES.getMessage("game.planet.geology.resources"));

        GroupLayout jPanel2Layout = new GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addComponent(jLabel3)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(depthLabel))
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addComponent(jLabel4)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(radiusLabel))
                                        .addComponent(jLabel6))
                                .addContainerGap(110, Short.MAX_VALUE))
                        .addComponent(jScrollPane3, GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel3)
                                        .addComponent(depthLabel))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel4)
                                        .addComponent(radiusLabel))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 87, Short.MAX_VALUE)
                                .addComponent(jLabel6)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane3, GroupLayout.PREFERRED_SIZE, 159, GroupLayout.PREFERRED_SIZE))
        );

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(jPanel2, gridBagConstraints);
    }

    private void strataListMouseClicked() {
        itemSelected();
    }

    private void strataListMouseReleased() {
        itemSelected();
    }

    private void itemSelected() {
        Stratum stratum = strataList.getSelectedValue();
        if (stratum != null) {
            radiusLabel.setText(stratum.getRadius() + " km");
            depthLabel.setText(stratum.getDepth() + " km");

            //Set the stuff
            resourceListModel.clear();
            for (Map.Entry<StoreableReference, Integer> en : stratum.minerals.entrySet()) {
                StoreableReference key = en.getKey();
                Integer val = en.getValue();

                resourceListModel.addElement(gameState.getGood(key).getName() + " " + val);
            }

        }
    }
}
