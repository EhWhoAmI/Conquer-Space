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
import ConquerSpace.common.game.population.RacePreferredClimateType;
import ConquerSpace.server.generators.UniverseGenerationConfig;
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

    private JLabel universeSizeLabel;
    private JComboBox<UniverseGenerationConfig.UniverseSize> universeSizeBox;
    private JLabel universeTypeLabel;
    private JComboBox<UniverseGenerationConfig.UniverseShape> universeTypeComboBox;
    private JLabel universeHistoryLabel;
    private JComboBox<UniverseGenerationConfig.UniverseAge> universeHistoryComboBox;
    private JLabel planetCommonalityLabel;
    private JComboBox<UniverseGenerationConfig.PlanetRarity> planetCommonalityComboBox;
    private JLabel civilizationCountLabel;
    private JComboBox<UniverseGenerationConfig.CivilizationCount> civilizationCountComboBox;
    private JLabel seedLabel;
    private JTextField seedText;

    private JLabel civNameLabel;
    private JTextField civNameTextField;
    private JLabel civColorLabel;
    private JButton civColorChooserButton;
    private JLabel civSymbolLabel;
    private JSpinner civSymbolSpinner;
    private JLabel civHomePlanetNameLabel;
    private JTextField civHomePlanetName;
    private JLabel civTempResistanceLabel;
    private JComboBox<RacePreferredClimateType> civTempResistanceComboBox;
    private JLabel speciesNameLabel;
    private JTextField speciesNameField;
    private JLabel currencyNameLabel;
    private JTextField currencyNameTextField;
    private JLabel currencySymbolLabel;
    private JTextField currencySymbolTextField;

    private Color civColor = Color.CYAN;

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
        civTempResistanceComboBox = new JComboBox<>(RacePreferredClimateType.values());

        speciesNameLabel = new JLabel(LOCALE_MESSAGES.getMessage("universe.civ.config.name.species"));
        speciesNameField = new JTextField("Human");

        currencyNameLabel = new JLabel(LOCALE_MESSAGES.getMessage("universe.civ.config.name.currency"));
        currencyNameTextField = new JTextField("Dollar");

        currencySymbolLabel = new JLabel(LOCALE_MESSAGES.getMessage("universe.civ.config.symbol.currency"));
        currencySymbolTextField = new JTextField("$");

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

    public UniverseGenerationConfig.UniverseSize getUniverseSize() {
        return (UniverseGenerationConfig.UniverseSize) universeSizeBox.getSelectedItem();
    }

    public UniverseGenerationConfig.UniverseShape getUniverseShape() {
        return (UniverseGenerationConfig.UniverseShape) universeTypeComboBox.getSelectedItem();
    }

    public UniverseGenerationConfig.UniverseAge getUniverseAge() {
        return (UniverseGenerationConfig.UniverseAge) universeHistoryComboBox.getSelectedItem();
    }

    public UniverseGenerationConfig.CivilizationCount getCivCount() {
        return (UniverseGenerationConfig.CivilizationCount) civilizationCountComboBox.getSelectedItem();
    }

    public UniverseGenerationConfig.PlanetRarity getPlanetRarity() {
        return (UniverseGenerationConfig.PlanetRarity) planetCommonalityComboBox.getSelectedItem();
    }

    public long getSeed() {
        long seed;
        try {
            seed = Long.parseLong(seedText.getText()); // This will pass a nfe.
        } catch (NumberFormatException nfe) {
            seed = seedText.getText().hashCode();
        }
        return seed;
    }

    public String getCivSymbol() {
        return (String) civSymbolSpinner.getValue();
    }

    public String getCivName() {
        return civNameTextField.getText();
    }

    public RacePreferredClimateType getCivPreferredClimate() {
        return (RacePreferredClimateType) civTempResistanceComboBox.getSelectedItem();
    }

    public String getHomePlanet() {
        return civHomePlanetName.getText();
    }

    public String getSpeciesName() {
        return speciesNameField.getText();
    }

    public String getCivCurrencyName() {
        return currencyNameTextField.getText();
    }

    public String getCivCurrencySymbol() {
        return currencySymbolTextField.getText();
    }
}
