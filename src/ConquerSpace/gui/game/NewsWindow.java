package ConquerSpace.gui.game;

import ConquerSpace.game.events.Event;
import ConquerSpace.game.universe.civilization.Civilization;
import javax.swing.DefaultListModel;
import javax.swing.JInternalFrame;
import javax.swing.JList;

/**
 *
 * @author zyunl
 */
public class NewsWindow extends JInternalFrame {

    private Civilization civ;
    private DefaultListModel<Event> eventListModel;
    private JList<Event> eventList;

    public NewsWindow(Civilization c) {
        this.civ = c;
        eventListModel = new DefaultListModel<>();
        eventList = new JList<>(eventListModel);
        add(eventList);
        pack();
        setClosable(true);
        setResizable(true);
        setVisible(true);
    }

    public void update() {
        eventListModel.clear();  
        for (Event e : civ.events) {
            eventListModel.addElement(e);
        }
    }
    //private class EventListModel extends 
}
