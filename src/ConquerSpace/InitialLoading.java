package ConquerSpace;

import ConquerSpace.util.CQSPLogger;
import ConquerSpace.util.ExceptionHandling;
import java.awt.GridLayout;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import org.apache.logging.log4j.Level;
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
                LOGGER.trace("Verifying file " + fileName);
                File f = new File(System.getProperty("user.dir") + "/" + fileName);
                //Next we have to determine the importance of the file. -- TODO
                //File exists or not, and warn noone. XD
                if (!f.exists()) {
                    LOGGER.warn("Can't find file " + fileName + ". not fatal.");
                    filesMissing ++;
                }
                else
                    LOGGER.trace("File " + fileName + " exists");
                progressBar.setValue((int) fileIndex/files);
            }
            if (filesMissing == 0)
                LOGGER.info("No files missing.");
            else {
                LOGGER.warn(filesMissing + " file(s) missing.");
                int toexit = JOptionPane.showConfirmDialog(null, "You have " + filesMissing + " files missing. Make sure they are there. \nSomething will go wrong if you don't fix it. \nWe need all the files that we have. Exit?" , "Files missing", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);
                if(toexit == JOptionPane.YES_OPTION) {
                    //Then exit
                    System.exit(0);
                }
                //or else leave the user to his or her own troubles. HEHEHE...
            }
        } catch (FileNotFoundException ex) {
            //Cannot fine FILELIST
            LOGGER.error("File not found Error: ", ex);
            ExceptionHandling.ExceptionMessageBox("File FILELIST not found. Please find it online or somewhere!", ex);
            System.exit(1);
        }
        long endTime = System.currentTimeMillis();
        
        //Log how long that took
        LOGGER.info("Took " + (endTime - startTime) + "ms to load.");
    }
}