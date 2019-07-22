package ConquerSpace.gui.start;

import ConquerSpace.Globals;
import ConquerSpace.game.GameController;
import ConquerSpace.util.CQSPLogger;
import com.alee.extended.layout.VerticalFlowLayout;
import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.border.LineBorder;
import org.apache.logging.log4j.Logger;

/**
 * Window of options.
 *
 * @author Zyun
 */
public class OptionsWindow extends JFrame {

    //Logger
    private static final Logger LOGGER = CQSPLogger.getLogger(OptionsWindow.class.getName());

    private static OptionsWindow instance;

    //Components
    private JPanel logsPanel;
    private JLabel deleteLogsLabel;
    private JButton deleteLogsButton;
    private JPanel musicPanel;
    private JToggleButton musicOnButton;

    private OptionsWindow() {
        setTitle("Options");
        setLayout(new VerticalFlowLayout());
        logsPanel = new JPanel();
        logsPanel.setBorder(BorderFactory.createTitledBorder(new LineBorder(Color.GRAY), "Logs"));

        deleteLogsLabel = new JLabel("Delete Logs");
        deleteLogsButton = new JButton("Delete");
        deleteLogsButton.addActionListener((e) -> {
            //Get Log files
            File logDir = new File(System.getProperty("user.dir") + "/logs");
            for (File f : logDir.listFiles()) {
                try {
                    f.delete();
                } catch (Exception ex) {

                }
            }
        });
        logsPanel.add(deleteLogsLabel);
        logsPanel.add(deleteLogsButton);

        musicPanel = new JPanel();
        musicPanel.setBorder(BorderFactory.createTitledBorder(new LineBorder(Color.GRAY), "Music"));
        musicOnButton = new JToggleButton("Music");
        musicOnButton.addActionListener(a -> {
            if (Globals.settings.get("music").equals("no")) {
                musicOnButton.setSelected(true);
                Globals.settings.setProperty("music", "yes");
                GameController.musicPlayer.setToPlay(true);
            }
            if (Globals.settings.get("music").equals("yes")) {
                musicOnButton.setSelected(false);
                Globals.settings.setProperty("music", "no");
                GameController.musicPlayer.setToPlay(false);
            }
            //Write
            File settingsFile = new File(System.getProperty("user.dir") + "/settings.properties");
            if (settingsFile.exists()) {
                FileOutputStream fis = null;
                try {
                    //Read from file.
                    fis = new FileOutputStream(settingsFile);
                    PrintWriter pw = new PrintWriter(fis);
                    Globals.settings.list(pw);
                    pw.close();
                } catch (FileNotFoundException ex) {
                } finally {
                    try {
                        fis.close();
                    } catch (IOException ex) {
                    }
                }
            }
        });

        if (Globals.settings.get("music").equals("no")) {
            musicOnButton.setSelected(false);
        }

        musicPanel.add(musicOnButton);
        add(logsPanel);
        add(musicPanel);
        pack();
        setVisible(true);
    }

    public static OptionsWindow getInstance() {
        if (instance == null) {
            instance = new OptionsWindow();
        }
        return (instance);
    }
}
