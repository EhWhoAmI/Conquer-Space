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
package ConquerSpace.client.gui.start;

import static ConquerSpace.ConquerSpace.DEBUG;
import static ConquerSpace.ConquerSpace.LOCALE_MESSAGES;
import ConquerSpace.common.game.population.RacePreferredClimateTpe;
import ConquerSpace.common.game.universe.generators.UniverseGenerationConfig;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerListModel;
import javax.swing.border.LineBorder;

/**
 *
 */
public class UniverseConfigPanel extends JPanel implements ActionListener {

    JLabel universeSizeLabel;
    JComboBox<UniverseGenerationConfig.UniverseSize> universeSizeBox;
    JLabel universeTypeLabel;
    JComboBox<UniverseGenerationConfig.UniverseShape> universeTypeComboBox;
    JLabel universeHistoryLabel;
    JComboBox<UniverseGenerationConfig.UniverseAge> universeHistoryComboBox;
    JLabel planetCommonalityLabel;
    JComboBox<UniverseGenerationConfig.PlanetRarity> planetCommonalityComboBox;
    JLabel civilizationCountLabel;
    JComboBox<UniverseGenerationConfig.CivilizationCount> civilizationCountComboBox;
    JLabel seedLabel;
    JTextField seedText;

    JLabel civNameLabel;
    JTextField civNameTextField;
    JLabel civColorLabel;
    JButton civColorChooserButton;
    JLabel civSymbolLabel;
    JSpinner civSymbolSpinner;
    JLabel civHomePlanetNameLabel;
    JTextField civHomePlanetName;
    JLabel civTempResistanceLabel;
    JComboBox<RacePreferredClimateTpe> civTempResistanceComboBox;
    JLabel speciesNameLabel;
    JTextField speciesNameField;
    JLabel currencyNameLabel;
    JTextField currencyNameTextField;
    JLabel currencySymbolLabel;
    JTextField currencySymbolTextField;

    Color civColor = Color.CYAN;

    public UniverseConfigPanel() {
        setLayout(new GridLayout(2, 1, 10, 10));
        //Add components
        JPanel lsidePan = new JPanel();
        lsidePan.setBorder(BorderFactory.createTitledBorder(new LineBorder(Color.GRAY), "Universe Options"));
        lsidePan.setLayout(new GridLayout(3, 4, 10, 10));

        universeSizeLabel = new JLabel(LOCALE_MESSAGES.getMessage("universe.config.size"));
        universeSizeLabel.setToolTipText(LOCALE_MESSAGES.getMessage("universe.config.size.tooltip"));
        universeSizeBox = new JComboBox<>(UniverseGenerationConfig.UniverseSize.values());

        universeTypeLabel = new JLabel(LOCALE_MESSAGES.getMessage("universe.config.shape"));
        universeTypeLabel.setToolTipText(LOCALE_MESSAGES.getMessage("universe.config.tooltip.useless") + LOCALE_MESSAGES.getMessage("universe.config.shape"));
        universeTypeComboBox = new JComboBox<>(UniverseGenerationConfig.UniverseShape.values());

        //Doesnt make a difference for now...
        universeHistoryLabel = new JLabel(LOCALE_MESSAGES.getMessage("universe.config.age"));
        universeHistoryLabel.setToolTipText(LOCALE_MESSAGES.getMessage("universe.config.tooltip.useless") + LOCALE_MESSAGES.getMessage("universe.config.age.tooltip"));
        universeHistoryComboBox = new JComboBox<>(UniverseGenerationConfig.UniverseAge.values());

        planetCommonalityLabel = new JLabel(LOCALE_MESSAGES.getMessage("universe.config.planets"));
        planetCommonalityLabel.setToolTipText(LOCALE_MESSAGES.getMessage("universe.config.tooltip.useless") + LOCALE_MESSAGES.getMessage("universe.config.planets.tooltip"));
        planetCommonalityComboBox = new JComboBox<>(UniverseGenerationConfig.PlanetRarity.values());

        civilizationCountLabel = new JLabel(LOCALE_MESSAGES.getMessage("universe.config.civilization.count"));
        civilizationCountLabel.setToolTipText(LOCALE_MESSAGES.getMessage("universe.config.tooltip.useless") + LOCALE_MESSAGES.getMessage("universe.config.civilization.count.tooltip"));
        civilizationCountComboBox = new JComboBox<>(UniverseGenerationConfig.CivilizationCount.values());

        seedLabel = new JLabel(LOCALE_MESSAGES.getMessage("universe.config.seed"));
        seedLabel.setToolTipText(LOCALE_MESSAGES.getMessage("universe.config.seed.tooltip"));
        seedText = new JTextField();
        if (DEBUG) {
            seedText.setText("test");
        } else {
            seedText.setText(Long.toString(System.currentTimeMillis()));
        }

        JPanel rsidePan = new JPanel();
        rsidePan.setBorder(BorderFactory.createTitledBorder(new LineBorder(Color.GRAY), "Universe Options"));
        rsidePan.setLayout(new GridLayout(4, 4, 10, 10));

        civNameLabel = new JLabel(LOCALE_MESSAGES.getMessage("universe.civ.config.name"));
        civNameTextField = new JTextField("Humans");

        civSymbolLabel = new JLabel(LOCALE_MESSAGES.getMessage("universe.civ.config.symbol"));
        //symbol list (Alphabet)
        String[] list = new String[26];
        for (int i = 0; i < list.length; i++) {
            list[i] = String.valueOf((char) (i + 65));
        }
        SpinnerListModel mod = new SpinnerListModel(list);
        civSymbolSpinner = new JSpinner(mod);

        civColorLabel = new JLabel(LOCALE_MESSAGES.getMessage("universe.civ.config.color"));
        civColorChooserButton = new JButton(LOCALE_MESSAGES.getMessage("universe.civ.config.color.choose"));
        civColorChooserButton.setBackground(civColor);
        civColorChooserButton.addActionListener(this);

        civHomePlanetNameLabel = new JLabel(LOCALE_MESSAGES.getMessage("universe.civ.config.name.planet"));
        civHomePlanetName = new JTextField("Earth");

        civTempResistanceLabel = new JLabel(LOCALE_MESSAGES.getMessage("universe.civ.config.climate"));
        civTempResistanceComboBox = new JComboBox<>(RacePreferredClimateTpe.values());

        speciesNameLabel = new JLabel(LOCALE_MESSAGES.getMessage("universe.civ.config.name.species"));
        speciesNameField = new JTextField("Earthlings");

        currencyNameLabel = new JLabel(LOCALE_MESSAGES.getMessage("universe.civ.config.name.currency"));
        currencyNameTextField = new JTextField("Money");

        currencySymbolLabel = new JLabel(LOCALE_MESSAGES.getMessage("universe.civ.config.symbol.currency"));
        currencySymbolTextField = new JTextField("M");

        lsidePan.add(universeSizeLabel);
        lsidePan.add(universeSizeBox);
        lsidePan.add(universeTypeLabel);
        lsidePan.add(universeTypeComboBox);
        lsidePan.add(universeHistoryLabel);
        lsidePan.add(universeHistoryComboBox);
        lsidePan.add(planetCommonalityLabel);
        lsidePan.add(planetCommonalityComboBox);
        lsidePan.add(civilizationCountLabel);
        lsidePan.add(civilizationCountComboBox);
        lsidePan.add(seedLabel);
        lsidePan.add(seedText);

        rsidePan.add(civNameLabel);
        rsidePan.add(civNameTextField);
        rsidePan.add(civSymbolLabel);
        rsidePan.add(civSymbolSpinner);
        rsidePan.add(civColorLabel);

        rsidePan.add(civColorChooserButton);
        rsidePan.add(civHomePlanetNameLabel);
        rsidePan.add(civHomePlanetName);
        rsidePan.add(civTempResistanceLabel);
        rsidePan.add(civTempResistanceComboBox);
        rsidePan.add(speciesNameLabel);
        rsidePan.add(speciesNameField);
        rsidePan.add(currencyNameLabel);
        rsidePan.add(currencyNameTextField);
        rsidePan.add(currencySymbolLabel);
        rsidePan.add(currencySymbolTextField);

        add(lsidePan);
        add(rsidePan);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == civColorChooserButton) {
            //Show the civ color chooser
            civColor = JColorChooser.showDialog(null, LOCALE_MESSAGES.getMessage("Choose Civilization Color"), civColor);
            civColorChooserButton.setBackground(civColor);
        }
    }

}
