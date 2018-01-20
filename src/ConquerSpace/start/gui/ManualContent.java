package ConquerSpace.start.gui;

import ConquerSpace.util.CQSPLogger;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JTextPane;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Zyun
 */
public class ManualContent extends JFrame {
    private static final Logger LOGGER = CQSPLogger.getLogger(ManualContent.class.getName());
    private static ManualContent instance;
    private JTextPane text;

    /**
     *
     * @param file the manual file to load.
     * @return
     */
    public static ManualContent getInstance(String file) {
        if (instance == null) {
            instance = new ManualContent();
        }
        instance.setText(file);
        return instance;
    }

    private ManualContent() {
        setSize(500, 400);
        text = new JTextPane();
        text.setContentType("text/html");
        text.setEditable(false);
        add(text);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent arg0) {
                instance.dispose();
                instance = null;
                LOGGER.info("Closed manual content window");
            }
        });
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void setText(String content) {
        if (text != null) {
            text.setText(content);
        }
    }
}
