package ConquerSpace.start.gui;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.DefaultListModel;
import java.util.logging.Logger;
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
import java.util.logging.Level;

/**
 *
 * @author hoong
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
            list = new JList<>(model);
            list.addListSelectionListener(this);
            add(list);
            this.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent arg0) {
                    current.dispose();
                    current = null;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    /**
     *
     * @return
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
                    value = ManualContent.getInstance(new String(Files.readAllBytes(Paths.get(System.getProperty("user.dir") + "/assets/manuals/" + str)), StandardCharsets.UTF_8));
                    break;
                } catch (IOException ex) {
                    Logger.getLogger(Manual.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        value.setVisible(true);
    }
}
