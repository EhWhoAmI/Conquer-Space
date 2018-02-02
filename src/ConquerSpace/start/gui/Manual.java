package ConquerSpace.start.gui;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.DefaultListModel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;

import ConquerSpace.util.CQSPLogger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.apache.logging.log4j.Logger;

/**
 * Shows the selection of manuals.
 * @author Zyun
 */
public class Manual extends JFrame implements ListSelectionListener {
    private static final long serialVersionUID = -2589166556749207075L;
    private static Manual current;
    private static final Logger LOGGER = CQSPLogger.getLogger(Manual.class.getName());
    private Properties prop;
    private JList<String> list;

    private Manual() {
        try {
            setSize(300, 200);

            setTitle("Manual");
            DefaultListModel<String> model = new DefaultListModel<>();
            // Open property file
            prop = new Properties();

            prop.load(new FileInputStream(System.getProperty("user.dir") + "/assets/manuals/manlist.properties"));

            for (Object val : prop.values()) {
                model.addElement((String) val);
            }
            
            LOGGER.info("Manual entries: " + model.getSize());
            list = new JList<>(model);
            list.addListSelectionListener(this);
            add(list);
            this.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent arg0) {
                    current.dispose();
                    current = null;
                    LOGGER.info("Closed manual window.");
                }
            });
        } catch (IOException e) {
            LOGGER.warn("Cannot find file", e);
        }
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    /**
     * Creates a singleton of a manual list.
     * @return an instance of manual.
     */
    public static Manual getInstance() {
        if (current == null) {
            current = new Manual();
        }
        return current;
    }

    @Override
    public void valueChanged(ListSelectionEvent arg0) {
        ManualContent value = null;
        Enumeration<Object> keys = prop.keys();
        while (keys.hasMoreElements()) {
            String str = (String) keys.nextElement();
            if (prop.getProperty(str).equals(list.getSelectedValue())) {
                try {
                    value = ManualContent.getInstance(
                            new String(Files.readAllBytes(Paths.get(
                                    System.getProperty("user.dir") + "/assets/manuals/" + str)), StandardCharsets.UTF_8));
                    LOGGER.info("Loading manual " + str);
                    break;
                } catch (IOException ex) {
                    LOGGER.error("Error", ex);
                }
            }
        }

        value.setVisible(true);
    }
}
