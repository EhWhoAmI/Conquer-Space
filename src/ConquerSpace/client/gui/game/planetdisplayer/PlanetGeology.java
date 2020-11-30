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
import ConquerSpace.common.game.resources.StorableReference;
import ConquerSpace.common.game.resources.Stratum;
import ConquerSpace.common.game.universe.bodies.Planet;
import java.awt.event.MouseEvent;
import java.util.Map;
import javax.swing.DefaultListModel;

/**
 *
 * @author EhWhoAmI
 */
public class PlanetGeology extends javax.swing.JPanel {

    private Planet planet;
    private DefaultListModel<Stratum> stratumListModel;
    private DefaultListModel<String> resourceListModel;

    private final GameState gameState;

    /**
     * Creates new form PlanetGeology
     * @param gameState
     * @param p
     */
    public PlanetGeology(GameState gameState, Planet p) {
        this.gameState = gameState;
        this.planet = p;
        stratumListModel = new DefaultListModel<>();
        for (ObjectReference stratumId : p.strata) {
            Stratum stratum = gameState.getObject(stratumId, Stratum.class);

            stratumListModel.addElement(stratum);
        }
        resourceListModel = new DefaultListModel<>();
        initComponents();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jScrollPane2 = new javax.swing.JScrollPane();
        strataList = new javax.swing.JList<>();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        resourceList = new javax.swing.JList<>();
        jLabel6 = new javax.swing.JLabel();
        depthLabel = new javax.swing.JLabel();
        radiusLabel = new javax.swing.JLabel();

        setLayout(new java.awt.GridBagLayout());

        strataList.setModel(stratumListModel);
        strataList.setName(""); // NOI18N
        strataList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                strataListMouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                strataListMouseReleased(evt);
            }
        });
        strataList.addListSelectionListener(l -> {
            Stratum stratum = strataList.getSelectedValue();
            itemSelected();
        });
        jScrollPane2.setViewportView(strataList);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(jScrollPane2, gridBagConstraints);

        jLabel3.setText(LOCALE_MESSAGES.getMessage("game.planet.geology.depth"));

        jLabel4.setText(LOCALE_MESSAGES.getMessage("game.planet.geology.radius"));

        resourceList.setModel(resourceListModel);
        jScrollPane3.setViewportView(resourceList);

        jLabel6.setText(LOCALE_MESSAGES.getMessage("game.planet.geology.resources"));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(depthLabel))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(radiusLabel))
                    .addComponent(jLabel6))
                .addContainerGap(110, Short.MAX_VALUE))
            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(depthLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(radiusLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 87, Short.MAX_VALUE)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(jPanel2, gridBagConstraints);
    }

    private void strataListMouseClicked(MouseEvent evt) {
        itemSelected();
    }

    private void strataListMouseReleased(MouseEvent evt) {
        itemSelected();
    }

    private void itemSelected() {
        Stratum stratum = strataList.getSelectedValue();
        if (stratum != null) {
            radiusLabel.setText(stratum.getRadius() + " km");
            depthLabel.setText(stratum.getDepth() + " km");

            //Set the stuff
            resourceListModel.clear();
            for (Map.Entry<StorableReference, Integer> en : stratum.minerals.entrySet()) {
                StorableReference key = en.getKey();
                Integer val = en.getValue();

                resourceListModel.addElement(gameState.getGood(key).getName() + " " + val);
            }

        }
    }
    
    private javax.swing.JLabel depthLabel;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel radiusLabel;
    private javax.swing.JList<String> resourceList;
    private javax.swing.JList<Stratum> strataList;
}
