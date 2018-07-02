package ConquerSpace.game.universe.civilization.controllers.PlayerController;

import ConquerSpace.game.actions.Alert;
import java.awt.Dimension;
import javax.swing.DefaultListModel;
import javax.swing.JInternalFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;

/**
 *
 * @author Zyun
 */
public class AlertDisplayer extends JInternalFrame {

    private JList<Alert> alertList;
    private DefaultListModel<Alert> model;

    private static AlertDisplayer instance;

    private AlertDisplayer() {
        //Form array
        model = new DefaultListModel<>();
        alertList = new JList<>(model);
        JScrollPane pane = new JScrollPane(alertList);

        add(pane);
        setResizable(true);

        setSize(new Dimension(20, 300));
        setVisible(true);
    }

    public void addAlert(Alert a) {
        //Insert in front
        model.add(0, a);
        //Later, sort, etc, etc...
        //Do that
    }

    public static AlertDisplayer getInstance() {
        if (instance == null) {
            instance = new AlertDisplayer();
        } else {
            instance.setVisible(true);
        }
        return instance;
    }
}
