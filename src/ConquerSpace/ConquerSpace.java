/*
 * Conquer Space - Conquer Space!
 * Copyright (C) 2019 EhWhoAmI
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package ConquerSpace;

import ConquerSpace.game.GameController;
import ConquerSpace.game.GameLoader;
import ConquerSpace.game.civilization.CivilizationConfig;
import ConquerSpace.game.universe.UniverseConfig;
import ConquerSpace.game.universe.bodies.Universe;
import ConquerSpace.game.universe.generators.DefaultUniverseGenerator;
import ConquerSpace.gui.music.MusicPlayer;
import ConquerSpace.gui.start.Loading;
import ConquerSpace.gui.start.MainMenu;
import ConquerSpace.i18n.Messages;
import ConquerSpace.tools.ToolsSelectionMenu;
import ConquerSpace.util.Checksum;
import ConquerSpace.util.ExceptionHandling;
import ConquerSpace.util.Version;
import ConquerSpace.util.logging.CQSPLogger;
import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
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
 * @author EhWhoAmI
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
            LOGGER.info("Asset file not found, no build number.", ex);
        } catch (IOException ex) {
            LOGGER.info("IO exception, no build number.", ex);
        }
    }

    /**
     * The version of the game.
     */
    public static final Version VERSION = new Version(0, 0, 3, "indev", BUILD_NUMBER);

    /**
     * Localization.
     */
    public static Messages localeMessages;

    public static String codeChecksum = null;
    public static String assetChecksum = null;

    public static boolean DEBUG = false;

    public static final Properties defaultProperties = new Properties();

    /**
     * Main class.
     *
     * @param args Command line arguments. Does nothing so far.
     */
    public static void main(String[] args) throws InterruptedException {
        if (args.length >= 1 && args[0].equals("-t")) {
            new ToolsSelectionMenu();
        } else {
            if (args.length >= 1 && args[0].equals("-debug")) {
                DEBUG = true;
            }
            
            CQSPLogger.initLoggers();
            LOGGER.info("Run started: " + new Date().toString());
            LOGGER.info("Version " + VERSION.toString());

            //Generate hash to verify the version
            //For error messages
            if (!DEBUG) {
                generateChecksum();
            }

            setDefaultOptions();
            configureSettings();

            //Set catch all exceptions in game thread
            Toolkit.getDefaultToolkit().getSystemEventQueue().push(new EventQueueProxy());

            initLookAndFeel();

            GameController.musicPlayer = new MusicPlayer();
            if (Globals.settings.getProperty("music").equals("no")) {
                GameController.musicPlayer.stopMusic();
            } else {
                GameController.musicPlayer.playMusic();
            }
            try {
                GameController.musicPlayer.setVolume(Float.parseFloat(Globals.settings.getProperty("music.volume")));
            } catch (NumberFormatException nfe) {
                GameController.musicPlayer.setVolume(1);
            } catch (IllegalArgumentException iae) {
                GameController.musicPlayer.setVolume(1);
            }

            //New Game Menu
            try {

                //Add debug menu
                if (DEBUG) {
                    //Debug game loader
                    setDebugUniverseGenerator();
                } else {
                    MainMenu menu = new MainMenu();
                    menu.setVisible(true);

                    //While the menu not loaded...
                    while (!menu.isLoadedUniverse()) {
                        Thread.sleep(100);
                    }
                    menu = null;
                }
                //Show loading screen
                Loading load = new Loading();
                loadUniverse();
                load.setVisible(false);

                runGame();
            } catch (Exception e) {
                //Catch exceptions...
                ExceptionHandling.ExceptionMessageBox("Exception: " + e.getClass() + ", " + e.getMessage(), e);
            }
        }
    }

    /**
     * Kept for purely historical reasons.
     */
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

    public static void initLookAndFeel() {
        try {
            //Set look and feel
            //Get from settings...
            if (Globals.settings.getProperty("laf").equals("default")) {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } else {
                Properties lafProperties = new Properties();
                File lafPropertyFile = new File(System.getProperty("user.dir") + "/assets/lookandfeels.properties");
                FileInputStream fis;
                try {
                    fis = new FileInputStream(lafPropertyFile);
                    lafProperties.load(fis);
                } catch (FileNotFoundException ex) {
                } catch (IOException ex) {
                }
                UIManager.setLookAndFeel(lafProperties.getProperty(Globals.settings.getProperty("laf")));
            }
        } catch (ClassNotFoundException ex) {
            LOGGER.warn("", ex);
        } catch (InstantiationException ex) {
            LOGGER.warn("", ex);
        } catch (IllegalAccessException ex) {
            LOGGER.warn("", ex);
        } catch (UnsupportedLookAndFeelException ex) {
            LOGGER.warn("", ex);
        }
    }

    public static void loadUniverse() {
        //Load universe
        long loadingStart = System.currentTimeMillis();
        GameLoader.load();
        Universe universe = Globals.generator.generate();
        Globals.universe = universe;
        long loadingEnd = System.currentTimeMillis();
        LOGGER.info("Took " + (loadingEnd - loadingStart) + " ms to generate universe, or about " + ((loadingEnd - loadingStart) / 1000d) + " seconds");
    }

    public static void runGame() {
        //Start game
        new GameController();
    }

    public static void configureSettings() {
        //Init settings, and read from file if possible
        Globals.settings = new Properties();
        //Check for the existance of the settings file
        File settingsFile = new File(System.getProperty("user.dir") + "/settings.properties");
        if (settingsFile.exists()) {
            try {

                FileInputStream fis = new FileInputStream(settingsFile);
                Globals.settings.load(fis);
            } catch (IOException ex) {
                LOGGER.warn("Cannot load settings. Using default", ex);
                createNewSettings(settingsFile);
            }
        } else {
            createNewSettings(settingsFile);
        }
        //do settings
        configureGame();

        try {
            //Write
            Globals.settings.store(new FileOutputStream(settingsFile), "Created by Conquer Space version " + VERSION.toString());
        } catch (IOException ex) {
        }
    }

    public static void createNewSettings(File settingsFile) {
        for (String key : defaultProperties.stringPropertyNames()) {
            Globals.settings.put(key, defaultProperties.get(key));
        }

        try {
            if (!settingsFile.getParentFile().exists()) {
                settingsFile.getParentFile().mkdir();
            }
            settingsFile.createNewFile();
            //Add default settings

            //Default settings
            //Write
            Globals.settings.store(new FileOutputStream(settingsFile), "Created by Conquer Space version " + VERSION.toString());
        } catch (IOException ex) {
            LOGGER.warn("Unable to create settings file!", ex);
        }
    }

    public static void configureGame() {
        //Check if all settings exist
        for (String key : defaultProperties.stringPropertyNames()) {
            Globals.settings.putIfAbsent(key, defaultProperties.getProperty(key));
        }

        //Get settings
        String locale = Globals.settings.getProperty("locale");
        String[] locales = locale.split("-");
        localeMessages = new Messages(new Locale(locales[0], locales[1]));

        //get version
        String version = Globals.settings.getProperty("version");
        //Do something
        Version v = new Version(version);
        //Check if not equal
        //if(v.)
        //Music
    }

    public static void generateChecksum() {
        //Get current file
        File codeFile = new File(ConquerSpace.class
                .getProtectionDomain().getCodeSource()
                .getLocation().getPath());
        File assetFolder = new File("assets/data");
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                long start = System.currentTimeMillis();
                try {
                    codeChecksum = Checksum.hashFolder(codeFile);
                    LOGGER.info("Done with code checksum");
                    assetChecksum = Checksum.hashFolder(assetFolder);
                    LOGGER.info("Done with asset checksum");
                } catch (NoSuchAlgorithmException ex) {
                    LOGGER.warn("", ex);
                } catch (IOException ex) {
                    LOGGER.warn("", ex);
                }
                LOGGER.info("Code checksum: " + codeChecksum);
                LOGGER.info("Asset checksum: " + assetChecksum);
                long end = System.currentTimeMillis();
                LOGGER.info("Time needed to calculate checksum: " + (end - start));
            }
        };
        Thread checksumThread = new Thread(runnable);
        checksumThread.setName("checksum");
        checksumThread.start();
    }

    public static void initalizeCommandLineArgs() {

    }

    static void setDefaultOptions() {
        //The default and required options that exist for backward compatability
        //Default settings
        defaultProperties.setProperty("locale", "en-US");

        //Version
        defaultProperties.setProperty("version", VERSION.toString());
        defaultProperties.setProperty("debug", "no");

        defaultProperties.setProperty("music", "yes");
        defaultProperties.setProperty("music.volume", "1");

        defaultProperties.setProperty("laf", "default");

    }

    static void setDebugUniverseGenerator() {
        UniverseConfig config = new UniverseConfig();

        config.setUniverseSize("Medium");
        config.setUniverseShape("Irregular");
        config.setUniverseAge("Medium");
        config.setCivilizationCount("Common");
        config.setPlanetCommonality("Common");

        //XD
        config.setSeed(42);

        //Set the player Civ options
        CivilizationConfig civilizationConfig = new CivilizationConfig();
        civilizationConfig.setCivColor(Color.CYAN);
        civilizationConfig.setCivSymbol("A");
        civilizationConfig.setCivilizationName("Humans");
        civilizationConfig.setCivilizationPreferredClimate("Varied");
        civilizationConfig.setHomePlanetName("Earth");
        civilizationConfig.setSpeciesName("Earthlings");
        civilizationConfig.setCivCurrencyName("Money");
        civilizationConfig.setCivCurrencySymbol("M");
        config.setCivilizationConfig(civilizationConfig);

        //Create generator
        DefaultUniverseGenerator gen = new DefaultUniverseGenerator(config, civilizationConfig, 42);
        Globals.generator = gen;
    }

    /**
     * For processing exceptions and things like that.
     */
    static class EventQueueProxy extends EventQueue {

        protected void dispatchEvent(AWTEvent newEvent) {
            try {
                super.dispatchEvent(newEvent);
            } catch (Throwable t) {
                ExceptionHandling.ExceptionMessageBox("Exception!", t);
            }
        }
    }
}
