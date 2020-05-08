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

import ConquerSpace.game.actions.Actions;
import ConquerSpace.game.buildings.District;
import ConquerSpace.game.buildings.SpacePort;
import ConquerSpace.game.universe.Vector;
import ConquerSpace.game.civilization.Civilization;
import ConquerSpace.game.ships.Ship;
import ConquerSpace.game.ships.ShipClass;
import ConquerSpace.game.ships.satellites.Satellite;
import ConquerSpace.game.ships.satellites.Satellites;
import ConquerSpace.game.universe.bodies.Planet;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import org.json.JSONObject;

/**
 *
 * @author EhWhoAmI
 */
public class SpacePortMenuSheet extends javax.swing.JPanel {

    private Civilization c;
    private Planet p;

    private SatelliteListModel satelliteListModel;
    private SpaceShipListModel spaceShipListModel;
    
    /**
     * Creates new form SpacePortMenuSheet
     */
    public SpacePortMenuSheet(Planet p, Civilization c) {
        this.p = p;
        this.c = c;
        satelliteListModel = new SatelliteListModel();
        spaceShipListModel = new SpaceShipListModel();
        initComponents();
        parentTabs.setEnabledAt(1, false);
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

        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        spaceportCount = new javax.swing.JLabel();
        parentTabs = new javax.swing.JTabbedPane();
        jPanel4 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        qlSatorShipTabs = new javax.swing.JTabbedPane();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        qlsatelliteList = new javax.swing.JList<>();
        jPanel2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        qlSatNameLabel = new javax.swing.JLabel();
        qlSatMassLabel = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        qlSpaceshipList = new javax.swing.JList<>();
        jPanel9 = new javax.swing.JPanel();
        qlSpaceshipNameLabel = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        qlSpaceshipMass = new javax.swing.JLabel();
        launchButton = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();

        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                formMouseEntered(evt);
            }
        });
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });
        addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                formPropertyChange(evt);
            }
        });
        setLayout(new java.awt.GridBagLayout());

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)), "Planetary Space Travel Capabilities"));
        jPanel1.setLayout(new com.alee.extended.layout.VerticalFlowLayout());

        jPanel3.setLayout(new com.alee.extended.layout.HorizontalFlowLayout());

        jLabel1.setText("Spaceports:");
        jPanel3.add(jLabel1);
        jPanel3.add(spaceportCount);

        jPanel1.add(jPanel3);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(jPanel1, gridBagConstraints);

        jPanel4.setLayout(new java.awt.GridBagLayout());

        jLabel3.setText("What to Launch?");
        jPanel4.add(jLabel3, new java.awt.GridBagConstraints());

        qlSatorShipTabs.setTabPlacement(javax.swing.JTabbedPane.LEFT);

        java.awt.GridBagLayout jPanel6Layout = new java.awt.GridBagLayout();
        jPanel6Layout.columnWidths = new int[] {0, 5, 0};
        jPanel6Layout.rowHeights = new int[] {0};
        jPanel6.setLayout(jPanel6Layout);

        qlsatelliteList.setModel(satelliteListModel);
        qlsatelliteList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                qlsatelliteListMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(qlsatelliteList);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.ipadx = 150;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        jPanel6.add(jScrollPane1, gridBagConstraints);

        jLabel4.setText("Name:");

        jLabel5.setText("Mass:");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(qlSatMassLabel)
                    .addComponent(qlSatNameLabel))
                .addGap(0, 865, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(qlSatNameLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(qlSatMassLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 96, Short.MAX_VALUE))
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel6.add(jPanel2, gridBagConstraints);

        qlSatorShipTabs.addTab("Satellite", jPanel6);

        jPanel8.setLayout(new java.awt.GridBagLayout());

        qlSpaceshipList.setModel(spaceShipListModel);
        qlSpaceshipList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                qlSpaceshipListMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(qlSpaceshipList);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        jPanel8.add(jScrollPane2, gridBagConstraints);

        jLabel8.setText("Mass");

        jLabel6.setText("Class Name:");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addComponent(jLabel8)
                .addGap(18, 18, 18)
                .addComponent(qlSpaceshipMass)
                .addGap(0, 1028, Short.MAX_VALUE))
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(qlSpaceshipNameLabel)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(qlSpaceshipNameLabel)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(qlSpaceshipMass))
                .addGap(0, 96, Short.MAX_VALUE))
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel8.add(jPanel9, gridBagConstraints);

        qlSatorShipTabs.addTab("Spaceship", jPanel8);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel4.add(qlSatorShipTabs, gridBagConstraints);

        launchButton.setText("Launch!");
        launchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                launchButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel4.add(launchButton, gridBagConstraints);

        parentTabs.addTab("Quick Launch", jPanel4);

        jPanel5.setLayout(null);

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 48)); // NOI18N
        jLabel10.setText("UNDER CONSTRUCTION...");
        jPanel5.add(jLabel10);
        jLabel10.setBounds(0, 0, 565, 58);

        parentTabs.addTab("Launch from specific launchpad", jPanel5);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(parentTabs, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void launchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_launchButtonActionPerformed
        //First get automation
        int toAutomateOrNot = parentTabs.getSelectedIndex();
        if (toAutomateOrNot == 0) {
            int tab = qlSatorShipTabs.getSelectedIndex();
            if (tab == 0) {
                //Do satellites
                //Automate
                int selection = qlsatelliteList.getSelectedIndex();
                //Create satellite
                JSONObject selectedObject = c.satelliteTemplates.get(selection);
                Satellite sat = Satellites.parseSatellite(selectedObject, c.multipliers, c.values);
                //Check if it orbits a planet
                sat.setOwner(c.getID());
                Actions.launchSatellite(sat, p, 100, c);
                JOptionPane.showInternalMessageDialog(getParent(), "Launched satellite");
            } else if (tab == 1) {
                int selection = qlSpaceshipList.getSelectedIndex();
                //Create satellite
                ShipClass selectedObject = c.shipClasses.get(selection);
                
                Ship ship = new Ship(selectedObject,
                        0, 0, new Vector(0, 0),
                        p.getUniversePath());
                ship.setEstimatedThrust(selectedObject.getEstimatedThrust());
                Actions.launchShip(ship, p, c);
                //Check if it orbits a planet
                JOptionPane.showInternalMessageDialog(getParent(), "Launched Spaceship");
            }
        } else {
            //Other thing
        }
    }//GEN-LAST:event_launchButtonActionPerformed

    private void qlsatelliteListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_qlsatelliteListMouseClicked
        //Change the info
        if (!qlsatelliteList.isSelectionEmpty()) {
            int selection = qlsatelliteList.getSelectedIndex();
            //Create satellite
            JSONObject selectedObject = c.satelliteTemplates.get(selection);
            qlSatNameLabel.setText(selectedObject.getString("name"));
            qlSatMassLabel.setText(selectedObject.getInt("mass") + "");
        }
    }//GEN-LAST:event_qlsatelliteListMouseClicked

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
        updateComponent();
    }//GEN-LAST:event_formComponentShown

    private void formPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_formPropertyChange
        updateComponent();
    }//GEN-LAST:event_formPropertyChange

    private void formMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseEntered
        updateComponent();
    }//GEN-LAST:event_formMouseEntered

    private void qlSpaceshipListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_qlSpaceshipListMouseClicked
        if (!qlSpaceshipList.isSelectionEmpty()) {
            int selection = qlSpaceshipList.getSelectedIndex();
            //Create satellite
            ShipClass selectedObject = c.shipClasses.get(selection);
            qlSpaceshipNameLabel.setText(selectedObject.getName());
            qlSpaceshipMass.setText(selectedObject.getMass() + "");
        }
    }//GEN-LAST:event_qlSpaceshipListMouseClicked

    private class SatelliteListModel extends DefaultListModel<String> {

        @Override
        public String getElementAt(int index) {
            return c.satelliteTemplates.get(index).getString("name");
        }

        @Override
        public int getSize() {
            return c.satelliteTemplates.size();
        }

        public void fireEvent() {
            fireIntervalAdded(this, c.launchVehicles.size(), c.launchVehicles.size());
        }
    }
    
    private class SpaceShipListModel extends DefaultListModel<String> {

        @Override
        public String getElementAt(int index) {
            return c.shipClasses.get(index).getName();
        }

        @Override
        public int getSize() {
            return c.shipClasses.size();
        }

        public void fireEvent() {
            fireIntervalAdded(this, c.launchVehicles.size(), c.launchVehicles.size());
        }
    }

    private void updateComponent() {
        satelliteListModel.fireEvent();
        
        //Get the amount of launch pads
        int launchPadCount = 0;
        for(District b : p.buildings.values()) {
            if(b instanceof SpacePort) {
                SpacePort port = (SpacePort) b;
                launchPadCount += port.launchPads.size();
            }
        }
        spaceportCount.setText("" + launchPadCount);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton launchButton;
    private javax.swing.JTabbedPane parentTabs;
    private javax.swing.JLabel qlSatMassLabel;
    private javax.swing.JLabel qlSatNameLabel;
    private javax.swing.JTabbedPane qlSatorShipTabs;
    private javax.swing.JList<String> qlSpaceshipList;
    private javax.swing.JLabel qlSpaceshipMass;
    private javax.swing.JLabel qlSpaceshipNameLabel;
    private javax.swing.JList<String> qlsatelliteList;
    private javax.swing.JLabel spaceportCount;
    // End of variables declaration//GEN-END:variables
}
