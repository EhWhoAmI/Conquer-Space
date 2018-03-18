package ConquerSpace.start.gui;

import ConquerSpace.util.CQSPLogger;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import nu.xom.Attribute;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;
import nu.xom.ParsingException;
import org.apache.logging.log4j.Logger;

/**
 * The content of the manuals.
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
     * @return ManualContent instance
     */
    public static ManualContent getInstance(String file) {

        if (instance == null) {
            instance = new ManualContent();
        }
        try {
            //Parse text so that images show
            Builder b = new Builder();
            Document d = b.build(file, null);
            Element e = d.getRootElement();
            //Get all img tags.
            Elements imgs = e.getChildElements("img");
            for (int i = 0; i < imgs.size(); i++) {
                Element imgE = imgs.get(i);
                Attribute attri = imgE.getAttribute("src");
                attri.setValue("file://" + System.getProperty("user.dir") + File.separator + "assets" + File.separator + "manuals" + File.separator + attri.getValue());
            }
            file = d.toXML();
        } catch (ParsingException ex) {
            LOGGER.warn("Parsing exception:" + ex.toString(), ex);
        } catch (IOException ex) {
            LOGGER.warn("IO Exception:" + ex.getMessage(), ex);
        }
        instance.setText(file);
        return instance;

    }

    private ManualContent() {
        setSize(500, 400);
        text = new JTextPane();
        text.setContentType("text/html");
        text.setEditable(false);

        JScrollPane pane = new JScrollPane(text);

        add(pane);
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
