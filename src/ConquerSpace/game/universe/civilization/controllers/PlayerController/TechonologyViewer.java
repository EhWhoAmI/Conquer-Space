package ConquerSpace.game.universe.civilization.controllers.PlayerController;

import ConquerSpace.game.tech.Technologies;
import ConquerSpace.game.tech.Technology;
import ConquerSpace.game.universe.civilization.Civilization;
import ConquerSpace.game.universe.spaceObjects.Universe;
import com.alee.extended.layout.VerticalFlowLayout;
import javax.swing.DefaultListModel;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author Zyun
 */
public class TechonologyViewer extends JInternalFrame{
    private JList<Technology> researchedList;
    private Universe universe;
    private Civilization c;
    public TechonologyViewer(Universe u, Civilization civ) {
        this.universe = u;
        c = civ;
        setTitle("Researched Techonologies");
        DefaultListModel<Technology> model = new DefaultListModel<>();
        for(Technology t : c.civTechs.keySet()) {
            if(c.civTechs.get(t) == Technologies.RESEARCHED) {
                model.addElement(t);
            }
        }
        researchedList = new JList<>(model);
        researchedList.addListSelectionListener((e) -> {
           Technology selected = researchedList.getSelectedValue();
            JOptionPane.showInternalMessageDialog(this.getDesktopPane(), 
                    new TechonologyPanel(selected), 
                    ("Techonology " + researchedList.getSelectedValue().getName()),
                    JOptionPane.DEFAULT_OPTION);
        });
        add(researchedList);
        
        setClosable(true);
        setMaximizable(true);
        setResizable(true);
        setSize(100, 100);
        setVisible(true);
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