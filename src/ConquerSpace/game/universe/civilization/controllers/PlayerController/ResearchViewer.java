package ConquerSpace.game.universe.civilization.controllers.PlayerController;

import ConquerSpace.game.tech.Techonologies;
import ConquerSpace.game.tech.Techonology;
import ConquerSpace.game.universe.civilizations.Civilization;
import java.awt.GridLayout;
import javax.swing.DefaultListModel;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author Zyun
 */
public class ResearchViewer extends JInternalFrame implements ListSelectionListener{
    private JList<Techonology> tech;
    private JPanel rightPanel;
    private JLabel techName;
    public ResearchViewer(Civilization c) {
        setLayout(new GridLayout(1, 2));
        //Get the list of researched tech
        DefaultListModel<Techonology> list = new DefaultListModel<>();
        //list.addElement(element);
        for (Techonology t : c.civTechs.keySet()) {
            if(c.civTechs.get(t) == Techonologies.REVEALED) {
                list.addElement(t);
            }
        }
        tech = new JList<>(list);
        tech.addListSelectionListener(this);
        
        rightPanel = new JPanel();
        
        techName = new JLabel("");
        rightPanel.add(techName);
        add(tech);
        add(rightPanel);
        
        setVisible(true);
        setClosable(true);
        setResizable(true);
        pack();
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        Techonology selected = tech.getSelectedValue();
        techName.setText(selected.getName());
    }
    
    
}
