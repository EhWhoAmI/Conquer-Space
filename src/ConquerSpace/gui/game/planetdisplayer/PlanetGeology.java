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
package ConquerSpace.gui.game.planetdisplayer;

import ConquerSpace.game.universe.resources.Good;
import ConquerSpace.game.universe.resources.Stratum;
import ConquerSpace.game.universe.bodies.Planet;
import java.util.Map;
import javax.swing.DefaultListModel;

/**
 *
 * @author EhWhoAmI
 */
public class PlanetGeology extends javax.swing.JPanel {

    private Planet p;
    private DefaultListModel<Stratum> stratumListModel;
    private DefaultListModel<String> resourceListModel;

    /**
     * Creates new form PlanetGeology
     */
    public PlanetGeology(Planet p) {
        this.p = p;
        stratumListModel = new DefaultListModel<>();
        for (Stratum stratum : p.strata) {
            stratumListModel.addElement(stratum);
        }
        resourceListModel = new DefaultListModel<>();
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
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

        jLabel3.setText("Depth");

        jLabel4.setText("Radius");

        resourceList.setModel(resourceListModel);
        jScrollPane3.setViewportView(resourceList);

        jLabel6.setText("Resources");

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
                .addContainerGap(123, Short.MAX_VALUE))
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
    }// </editor-fold>//GEN-END:initComponents

    private void strataListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_strataListMouseClicked
        itemSelected();
    }//GEN-LAST:event_strataListMouseClicked

    private void strataListMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_strataListMouseReleased
        itemSelected();
    }//GEN-LAST:event_strataListMouseReleased

    private void itemSelected() {
        Stratum stratum = strataList.getSelectedValue();
        if (stratum != null) {
            radiusLabel.setText(stratum.getRadius() + " km");
            depthLabel.setText(stratum.getDepth() + " km");
            
            //Set the stuff
            resourceListModel.clear();
            for (Map.Entry<Good, Integer> en : stratum.minerals.entrySet()) {
                Good key = en.getKey();
                Integer val = en.getValue();
                
                resourceListModel.addElement(key.getName() + " " + val);
            }
           
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
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
    // End of variables declaration//GEN-END:variables
}
