package ConquerSpace.start.gui;

import ConquerSpace.util.CQSPLogger;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.Logger;

/**
 * Window of options.
 * @author Zyun
 */
public class OptionsWindow extends JFrame{
    
    //Logger
    private static final Logger LOGGER = CQSPLogger.getLogger(OptionsWindow.class.getName());
    
    private static OptionsWindow instance;
    
    //Components
    private JPanel logsPanel;
    private JLabel deleteLogsLabel;
    private JButton deleteLogsButton;
    
    private OptionsWindow (){
        setTitle("Options");
        logsPanel = new JPanel();
        logsPanel.setBorder(BorderFactory.createTitledBorder(new LineBorder(Color.GRAY), "Logs"));
        
        deleteLogsLabel = new JLabel("Delete Logs");
        deleteLogsButton = new JButton("Delete");
        deleteLogsButton.addActionListener((e) -> {
            //Get Log files
            File logDir = new File(System.getProperty("user.dir") + "/logs");
            if (!FileUtils.deleteQuietly(logDir)){
                LOGGER.info("Delete dir unsucessful");
            }
        });
        logsPanel.add(deleteLogsLabel);
        logsPanel.add(deleteLogsButton);
        
        add(logsPanel);
        pack();
        setVisible(true);
    }
    
    public static OptionsWindow getInstance(){
        if (instance == null) {
            instance = new OptionsWindow();
        }
        return (instance);
    }
}
