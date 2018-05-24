package ConquerSpace.game.universe.civilization.controllers.PlayerController;

import ConquerSpace.game.universe.spaceObjects.Planet;
import java.awt.CardLayout;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.text.NumberFormat;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.NumberFormatter;

/**
 *
 * @author Zyun
 */
public class BuildPlanetSectorMenu extends JFrame {

    private JPanel topWrapper;
    private JLabel planetBuildTypeTitle;
    private JComboBox<String> planetBuildType;
    private JLabel costLabel;
    private JButton build;
    private JPanel bottom;
    private CardLayout cardLayout;

    public BuildPlanetSectorMenu(Planet p, int id) {
        setTitle("Build on planet " + p.getName());
        setLayout(new GridLayout(2, 1));
        topWrapper = new JPanel();
        topWrapper.setLayout(new GridLayout(2, 2, 5, 5));
        planetBuildTypeTitle = new JLabel("Build a ");
        planetBuildType = new JComboBox<>();
        planetBuildType.addItem("Residental area");
        planetBuildType.addItem("Industral area");
        planetBuildType.addItem("Military building");
        planetBuildType.addActionListener((e) -> {
            JComboBox<String> box = (JComboBox) e.getSource();
            box.getSelectedItem();
            cardLayout.show(bottom, "Pop");
        });

        costLabel = new JLabel("Cost : " + 0);
        build = new JButton("Build!");

        topWrapper.add(planetBuildTypeTitle);
        topWrapper.add(planetBuildType);
        topWrapper.add(costLabel);
        topWrapper.add(build);
        add(topWrapper);

        bottom = new JPanel();
        cardLayout = new CardLayout();
        bottom.setLayout(cardLayout);
        bottom.add(new BuildPopulationStorage(), "Pop");

        add(bottom);
        pack();
        setVisible(true);
    }

    //Various menus for building stats
    private class BuildPopulationStorage extends JPanel {

        private JLabel amount;
        private JFormattedTextField maxPopulation;

        public BuildPopulationStorage() {
            setLayout(new GridLayout(1, 2));
            amount = new JLabel("Max Population");
            NumberFormat format = NumberFormat.getInstance();
            NumberFormatter formatter = new NumberFormatter(format);
            formatter.setValueClass(Integer.class);
            formatter.setMinimum(-1);
            formatter.setMaximum(Integer.MAX_VALUE);
            formatter.setAllowsInvalid(false);
            formatter.setCommitsOnValidEdit(true);

            maxPopulation = new JFormattedTextField(formatter);
            add(amount);
            add(maxPopulation);
        }

    }
}
