package ConquerSpace;

import ConquerSpace.game.GameController;
import ConquerSpace.gui.music.MusicPlayer;
import ConquerSpace.gui.start.MainMenu;
import ConquerSpace.i18n.Messages;
import ConquerSpace.util.CQSPLogger;
import ConquerSpace.util.ExceptionHandling;
import ConquerSpace.util.Version;
import java.awt.AWTEvent;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.awt.SplashScreen;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;
import java.util.Scanner;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.apache.logging.log4j.Logger;

/*
  ______   ______   .__   __.   ______      __    __   _______ .______              _______..______      ___       ______  _______ 
 /      | /  __  \  |  \ |  |  /  __  \    |  |  |  | |   ____||   _  \            /       ||   _  \    /   \     /      ||   ____|
|  ,----'|  |  |  | |   \|  | |  |  |  |   |  |  |  | |  |__   |  |_)  |          |   (----`|  |_)  |  /  ^  \   |  ,----'|  |__   
|  |     |  |  |  | |  . `  | |  |  |  |   |  |  |  | |   __|  |      /            \   \    |   ___/  /  /_\  \  |  |     |   __|  
|  `----.|  `--'  | |  |\   | |  `--'  '--.|  `--'  | |  |____ |  |\  \----.   .----)   |   |  |     /  _____  \ |  `----.|  |____ 
 \______| \______/  |__| \__|  \_____\_____\\______/  |_______|| _| `._____|   |_______/    | _|    /__/     \__\ \______||_______|
                                                                                                                                   
 */
/**
 * Conquer Space main class. Where everything starts.
 *
 * @author Zyun
 */
public class ConquerSpace {

    /**
     * Logger for the class.
     */
    private static final Logger LOGGER = CQSPLogger.getLogger(ConquerSpace.class.getName());

    /**
     * Build number for debugging.
     */
    public static int BUILD_NUMBER = 0;

    static {
        Properties buildno = new Properties();
        try {
            buildno.load(new FileInputStream(System.getProperty("user.dir") + "/assets/BUILDNO"));
            BUILD_NUMBER = Integer.parseInt(buildno.getProperty("build.number"));
        } catch (FileNotFoundException ex) {
            LOGGER.info("Asset file not found. No Problem.", ex);
        } catch (IOException ex) {
            LOGGER.info("Io exception. No Problem.", ex);
        }

    }

    /**
     * The version of the game.
     */
    public static final Version VERSION = new Version(0, 0, 2, "indev-snapshot-2-b" + BUILD_NUMBER);

    /**
     * Localization.
     */
    public static Messages localeMessages;

    /**
     * Main class.
     *
     * @param args Command line arguments. Does nothing so far.
     */
    public static void main(String[] args) {
        CQSPLogger.initLoggers();
        LOGGER.info("Run started: " + new Date().toString());
        LOGGER.info("Version " + VERSION.toString());

        //Init settings, and read from file if possible
        Globals.settings = new Properties();
        //Check for the existance of the settings file
        File settingsFile = new File(System.getProperty("user.dir") + "/settings.properties");
        if (settingsFile.exists()) {
            try {
                //Read from file.
                FileInputStream fis = new FileInputStream(settingsFile);
                Globals.settings.load(fis);

                //Get settings
                String locale = Globals.settings.getProperty("locale");
                String[] locales = locale.split("-");
                localeMessages = new Messages(new Locale(locales[0], locales[1]));

                //get version
                String version = Globals.settings.getProperty("version");
                if (!(((version.split("-"))[0]).equals(VERSION.toString().split("-")[0]))) {
                    //Then different version, update. How, idk.
                }

            } catch (IOException ex) {
                LOGGER.warn("Cannot load settings. Using default", ex);
            }
        } else {
            try {
                if (!settingsFile.getParentFile().exists()) {
                    settingsFile.getParentFile().mkdir();
                }
                settingsFile.createNewFile();
                //Add default settings

                //Default settings
                Globals.settings.setProperty("locale", "en-US");
                localeMessages = new Messages(new Locale("en", "US"));

                //Version
                Globals.settings.setProperty("version", VERSION.toString());
                Globals.settings.setProperty("debug", "no");

                Globals.settings.setProperty("music", "yes");

                Globals.settings.store(new FileOutputStream(settingsFile), "Created by Conquer Space version " + VERSION.toString());
            } catch (IOException ex) {
                LOGGER.warn("Unable to create settings file!", ex);
            }
        }
        //Set catch all exceptions
        Toolkit.getDefaultToolkit().getSystemEventQueue().push(new EventQueueProxy());

        try {
            //Set look and feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException ex) {
            LOGGER.warn("", ex);
        } catch (InstantiationException ex) {
            LOGGER.warn("", ex);
        } catch (IllegalAccessException ex) {
            LOGGER.warn("", ex);
        } catch (UnsupportedLookAndFeelException ex) {
            LOGGER.warn("", ex);
        }
        GameController.musicPlayer = new MusicPlayer();
        if (Globals.settings.getProperty("music").equals("no")) {
            GameController.musicPlayer.setToPlay(false);
        }
        // Load all the files.
        loadFiles();

        //New Game Menu
        MainMenu menu = new MainMenu();
        menu.setVisible(true);
    }

    static class EventQueueProxy extends EventQueue {

        protected void dispatchEvent(AWTEvent newEvent) {
            try {
                super.dispatchEvent(newEvent);
            } catch (Throwable t) {
                ExceptionHandling.ExceptionMessageBox("Exception!", t);
                //Also print stack trace if debug
                if (true) {
                    t.printStackTrace();
                }
            }
        }
    }

    public static void loadFiles() {
        long startTime = System.currentTimeMillis();
        try {
            //Load the file check.
            Scanner fileScanner = new Scanner(new File(System.getProperty("user.dir") + "/assets/FILELIST"));
            int files = 0;
            while (fileScanner.hasNextLine()) {
                files++;
                fileScanner.nextLine();
            }
            LOGGER.info("Asset Files: " + files);
            //Reset Scanner
            fileScanner = new Scanner(new File(System.getProperty("user.dir") + "/assets/FILELIST"));
            int fileIndex = 0;
            int filesMissing = 0;
            while (fileScanner.hasNextLine()) {
                fileIndex++;

                String fileName = fileScanner.nextLine();

                LOGGER.trace("Verifying file " + fileName);

                //Check for it
                File f = new File(System.getProperty("user.dir") + "/" + fileName);
                //Next we have to determine the importance of the file. -- TODO
                //File exists or not, and warn noone. XD
                if (!f.exists()) {
                    LOGGER.warn("Can't find file " + fileName + ". not fatal.");
                    filesMissing++;
                } else {
                    LOGGER.trace("File " + fileName + " exists");
                }
            }
            if (filesMissing == 0) {
                LOGGER.info("No files missing.");
            } else {
                LOGGER.warn(filesMissing + " file(s) missing.");
                int toexit = JOptionPane.showConfirmDialog(null, "You have "
                        + filesMissing + " files missing. Make sure they are there"
                        + ". \nSomething will go wrong if you don't fix"
                        + "it. \nWe need all the files that we have. "
                        + "Exit?", "Files missing",
                        JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);
                if (toexit == JOptionPane.YES_OPTION) {
                    //Then exit
                    System.exit(0);
                }
                //or else leave the user to his or her own troubles. HEHEHE...
            }
        } catch (FileNotFoundException ex) {
            //Cannot fine FILELIST
            LOGGER.error("File not found Error: ", ex);
            ExceptionHandling.ExceptionMessageBox("File FILELIST not found. Plea"
                    + "se find it online or somewhere! We cannot check for any f"
                    + "iles we need.", ex);
            System.exit(1);
        }

        long endTime = System.currentTimeMillis();

        //Log how long that took
        LOGGER.info("Took " + (endTime - startTime) + "ms to load.");
    }
}
