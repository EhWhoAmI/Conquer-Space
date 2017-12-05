package ConquerSpace;

import java.awt.GridLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import java.util.logging.Logger;

import ConquerSpace.util.CQSPLogger;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.logging.Level;

/**
 *
 * @author Zyun
 */
public class InitialLoading extends JFrame {

    private static final Logger LOGGER = CQSPLogger.getLogger(InitialLoading.class.getName());
    private JProgressBar progressBar;
    private JLabel text;

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

    public void run() {
        try {
            //Load the file check.
            Scanner fileScanner = new Scanner(new File(System.getProperty("user.dir") + "/assets/FILELIST"));
            int files = 0;
            while(fileScanner.hasNextLine()) {
                files++;
                fileScanner.nextLine();
            }
            LOGGER.info("Files: " + files);
            //Reset Scanner
            fileScanner = new Scanner(new File(System.getProperty("user.dir") + "/assets/FILELIST"));
            int fileIndex = 0;
            LOGGER.info("Scanner status: " + fileScanner.hasNext());
            while (fileScanner.hasNextLine()) {
                
                fileIndex ++;
                String fileName = fileScanner.nextLine();
                LOGGER.info("Doing file " + fileName);
                File f = new File(System.getProperty("user.dir") + "/" + fileName);
                if (!f.exists())
                    throw new FileNotFoundException("File " + fileName + " not found");
                progressBar.setValue((int) fileIndex/files);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(InitialLoading.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
