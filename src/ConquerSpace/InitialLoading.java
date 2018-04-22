package ConquerSpace;

import ConquerSpace.util.CQSPLogger;
import java.awt.GridLayout;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import org.apache.logging.log4j.Logger;

/**
 * Initial loading screen. Loads everything.
 * @author Zyun
 */
public class InitialLoading extends JFrame {

    //Logger
    private static final Logger LOGGER = CQSPLogger.getLogger(InitialLoading.class.getName());
    
    //Components
    private JProgressBar progressBar;
    private JLabel text;

    /**
     * Constructor to show window.
     */
    public InitialLoading() {
        setTitle("Conquer Space");
        setLayout(new GridLayout(2, 1, 10, 10));

        text = new JLabel("Loading...");
        progressBar = new JProgressBar();
        progressBar.setStringPainted(true);
        add(text);
        add(progressBar);
        pack();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    /**
     * Run function for the content of the loading.
     */
    public void run() {
        long startTime = System.currentTimeMillis();
        try {
            //Load the file check.
            Scanner fileScanner = new Scanner(new File(System.getProperty("user.dir") + "/assets/FILELIST"));
            int files = 0;
            while(fileScanner.hasNextLine()) {
                files++;
                fileScanner.nextLine();
            }
            LOGGER.info("Asset Files: " + files);
            //Reset Scanner
            fileScanner = new Scanner(new File(System.getProperty("user.dir") + "/assets/FILELIST"));
            int fileIndex = 0;
            int filesMissing = 0;
            while (fileScanner.hasNextLine()) {
                
                fileIndex ++;
                String fileName = fileScanner.nextLine();
                LOGGER.info("Verifying file " + fileName);
                File f = new File(System.getProperty("user.dir") + "/" + fileName);
                //Next we have to determine the importance of the file. -- TODO
                //File exists or not, and warn noone. XD
                if (!f.exists()) {
                    LOGGER.warn("Can't find file " + fileName + ". not fatal.");
                    filesMissing ++;
                }
                else
                    LOGGER.info("File " + fileName + " exists");
                progressBar.setValue((int) fileIndex/files);
            }
            if (filesMissing == 0)
                LOGGER.info("No files missing.");
            else
                LOGGER.warn(filesMissing + " file(s) missing.");
        } catch (FileNotFoundException ex) {
            //Cannot fine FILELIST
            LOGGER.error("Error: ", ex);
            JOptionPane.showMessageDialog(null, "Could not find file: \n" + ex.getMessage() , ex.getClass().getName(), JOptionPane.ERROR_MESSAGE);
        }
        long endTime = System.currentTimeMillis();
        
        //Log how long that took
        LOGGER.info("Took " + (endTime - startTime) + "ms to load.");
    }
}
