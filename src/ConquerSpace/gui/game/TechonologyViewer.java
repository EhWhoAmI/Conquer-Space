package ConquerSpace.gui.game;

import ConquerSpace.game.tech.Technologies;
import ConquerSpace.game.tech.Technology;
import ConquerSpace.game.universe.civilization.Civilization;
import com.alee.extended.layout.VerticalFlowLayout;
import java.awt.BorderLayout;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;

/**
 *
 * @author Zyun
 */
public class TechonologyViewer extends JPanel {

    private JList<Technology> researchedList;
    private Civilization c;
    private DefaultListModel<Technology> model;

    public TechonologyViewer(Civilization civ) {
        //this.universe = u;
        setLayout(new BorderLayout());
        c = civ;
        model = new DefaultListModel<>();
        for (Technology t : c.civTechs.keySet()) {
            if (c.civTechs.get(t) == Technologies.RESEARCHED) {
                model.addElement(t);
            }
        }
        researchedList = new JList<>(model);
        researchedList.addListSelectionListener((e) -> {
            //Technology selected = researchedList.getSelectedValue();
            /*JOptionPane.showInternalMessageDialog(this.getDesktopPane(), 
                    new TechonologyPanel(selected), 
                    ("Techonology " + researchedList.getSelectedValue().getName()),
                    JOptionPane.DEFAULT_OPTION);*/
        });
        add(researchedList, BorderLayout.CENTER);

        setSize(100, 100);
        setVisible(true);
    }

    public void update() {
        for (Technology t : c.civTechs.keySet()) {
            if (c.civTechs.get(t) == Technologies.RESEARCHED && !model.contains(t)) {
                model.addElement(t);
            }
        }
    }
    
    private class TechonologyPanel extends JPanel {

        JLabel name;
        JLabel techLevel;

        public TechonologyPanel(Technology t) {
            setLayout(new VerticalFlowLayout());
            name = new JLabel("Name: " + t.getName());
            techLevel = new JLabel("Tech Level: " + t.getLevel());

            add(name);
            add(techLevel);
        }

    }
}
