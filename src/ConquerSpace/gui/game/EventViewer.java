package ConquerSpace.gui.game;

import ConquerSpace.game.events.Event;
import com.alee.extended.layout.VerticalFlowLayout;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 *
 * @author zyunl
 */
public class EventViewer extends JPanel{
    private JList<Event> eventList;
    private DefaultListModel<Event> eventListModel;
    
    public EventViewer() {
        setLayout(new VerticalFlowLayout());
        eventListModel = new DefaultListModel<>();
        eventList = new JList<>(eventListModel);
        
        JScrollPane scrollPane = new JScrollPane(eventList);
        add(scrollPane);
    }
    
    public void passEvent(Event e) {
        eventListModel.addElement(e);
    }
}
