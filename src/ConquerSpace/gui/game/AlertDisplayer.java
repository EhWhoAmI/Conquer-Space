package ConquerSpace.gui.game;

import ConquerSpace.game.actions.Alert;
import ConquerSpace.gui.GUI;
import ConquerSpace.util.Singleton;
import java.awt.Dimension;
import javax.swing.DefaultListModel;
import javax.swing.JInternalFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;

/**
 *
 * @author Zyun
 */
@Singleton
public class AlertDisplayer extends JInternalFrame implements GUI {

    private JList<Alert> alertList;
    private DefaultListModel<Alert> model;

    private static AlertDisplayer instance;

    private AlertDisplayer() {
        setTitle("Alerts");

        init();

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

    private void initWindow() {
        setResizable(true);

        setSize(new Dimension(100, 200));
        setVisible(true);
        setClosable(true);
    }

    @Override
    public void init() {
        //Form array
        model = new DefaultListModel<>();
        alertList = new JList<>(model);
        JScrollPane pane = new JScrollPane(alertList);

        add(pane);
        initWindow();
    }

    @Override
    public void refresh() {
    }

    @Override
    public void clean() {
        model.clear();
    }

    @Override
    public void reload() {
        clean();
        init();
    }
}
