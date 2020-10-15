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

import ConquerSpace.client.ClientOptions;
import ConquerSpace.client.gui.music.MusicPlayer;
import ConquerSpace.client.gui.start.Loading;
import ConquerSpace.client.gui.start.MainMenu;
import ConquerSpace.client.i18n.Messages;
import ConquerSpace.common.GameLoader;
import ConquerSpace.common.GameState;
import ConquerSpace.common.game.population.RacePreferredClimateTpe;
import ConquerSpace.common.save.SaveGame;
import ConquerSpace.common.util.Checksum;
import ConquerSpace.common.util.ExceptionHandling;
import ConquerSpace.common.util.Version;
import ConquerSpace.common.util.logging.CQSPLogger;
import ConquerSpace.server.GameController;
import ConquerSpace.server.generators.CivilizationConfig;
import ConquerSpace.server.generators.DefaultUniverseGenerator;
import ConquerSpace.server.generators.UniverseGenerationConfig;
import ConquerSpace.server.generators.UniverseGenerator;
import ConquerSpace.tools.ToolsSelectionMenu;
import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.lang3.LocaleUtils;
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
public final class ConquerSpace {

    /**
     * Logger for the class.
     */
    private static final Logger LOGGER = CQSPLogger.getLogger(ConquerSpace.class.getName());

    public static String BUILD_TIME = "";

    public static String BUILD_REVISION = "";

    /**
     * The version of the game.
     */
    public static final Version VERSION = new Version(0, 0, 3, "indev");

    /**
     * Localization.
     */
    public static Messages LOCALE_MESSAGES;
    public static Locale locale = null;
    public static final Locale DEFAULT_LOCALE = new Locale("en", "us");

    public static String codeChecksum = null;
    public static String assetChecksum = null;

    public static boolean DEBUG = false,
            TOOLS = false,
            HEADLESS = false,
            TRANSLATE_TEST = false;

    public static final String USER_DIR = System.getProperty("user.dir");

    public static final File SETTINGS_FILE = new File(USER_DIR + "/settings.properties");

    public static GameState gameState;
    /**
     * This is the settings of the game.
     */
    public static ClientOptions settings;

    public static UniverseGenerator generator;

    /**
     * Main class.
     *
     * @param args Command line arguments. Does nothing so far.
     */
    public static void main(String[] args) throws InterruptedException {
        initalizeCommandLineArgs(args);
        if (TOOLS) {
            new ToolsSelectionMenu(null);
        } else {
            CQSPLogger.initLoggers();
            LOGGER.info("Run started: " + new Date().toString());
            LOGGER.info("Version " + VERSION.toString());

            //Generate hash to verify the version
            //For error messages
            if (!DEBUG) {
                generateChecksum();
            }

            configureSettings();

            //Set language
            LOCALE_MESSAGES = new Messages(ConquerSpace.locale);

            //New Game Menu
            try {
                //Headless, no UI
                //Turn off music for now
                if (!HEADLESS) {
                    //Set catch all exceptions in game thread
                    Toolkit.getDefaultToolkit().getSystemEventQueue().push(new EventQueueProxy());

                    //Configure look and feel
                    initLookAndFeel();
                    configureMusic();
                }
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
                    //Remove
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
                //Clean up, however we do that
            }
        }
    }

    public static void initLookAndFeel() {
        try {
            //Set look and feel
            //Get from settings...
            if (settings.getLaf().equals("default")) {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } else {
                Properties lafProperties = new Properties();
                File lafPropertyFile = new File(USER_DIR + "/assets/lookandfeels.properties");
                try ( FileInputStream fis = new FileInputStream(lafPropertyFile);) {
                    lafProperties.load(fis);
                } catch (FileNotFoundException ex) {
                } catch (IOException ex) {
                }
                if (lafProperties.containsKey(settings.getLaf())) {
                    UIManager.setLookAndFeel(lafProperties.getProperty(settings.getLaf()));
                }
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
        gameState = new GameState(0);
        //Set save folder
        gameState.saveFile = new File(SaveGame.getSaveFolder());
        //Load universe
        long loadingStart = System.currentTimeMillis();
        GameLoader.load(gameState);
        try {
            generator.generate(gameState);
        } catch (Exception ex) {
            ExceptionHandling.ExceptionMessageBox(ex.getClass().toString() + " while loading universe!", ex);
        }
        long loadingEnd = System.currentTimeMillis();
        LOGGER.info("Took " + (loadingEnd - loadingStart) + " ms to generate universe, or about " + ((loadingEnd - loadingStart) / 1000d) + " seconds");
    }

    public static void runGame() {
        //Start game
        new GameController(gameState);
    }

    public static void configureSettings() {
        //Init settings, and read from file if possible        
        settings = new ClientOptions();
        //Check for the existance of the settings file
        if (SETTINGS_FILE.exists()) {
            settings.fromProperties(SETTINGS_FILE);
        } else {
            createNewSettings(SETTINGS_FILE);
        }

        //do settings
        configureGame();

        try {
            //Write settings to file
            settings.store(SETTINGS_FILE);
        } catch (IOException ex) {
        }
    }

    public static void createNewSettings(File settingsFile) {
        try {
            if (!settingsFile.getParentFile().exists()) {
                settingsFile.getParentFile().mkdirs();
            }
            settingsFile.createNewFile();

            //Save
            settings.store(settingsFile);
        } catch (IOException ex) {
            LOGGER.warn("Unable to create settings file!", ex);
        }
    }

    public static void configureGame() {
        //Get settings
        ConquerSpace.locale = LocaleUtils.toLocale(System.getProperty("user.language"));
        Version version = settings.getVersion();
        //Check if not equal
    }

    private static void generateChecksum() {
        //Get current file
        File codeFile = new File(ConquerSpace.class
                .getProtectionDomain().getCodeSource()
                .getLocation().getPath());
        File assetFolder = new File("assets/data");

        //Create thread to process checksums
        Runnable runnable = () -> {
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
            LOGGER.info("Time needed to calculate checksum: " + (end - start) + "ms");
        };

        Thread checksumThread = new Thread(runnable);
        checksumThread.setName("checksum");
        checksumThread.start();
    }

    //Gets manifest metadata
    private static void processManifestData() {
        Class clazz = ConquerSpace.class;
        String className = clazz.getSimpleName() + ".class";
        String classPath = clazz.getResource(className).toString();
        if (classPath.startsWith("jar")) {
            try {
                // Class not from JAR
                String manifestPath = classPath.substring(0, classPath.lastIndexOf("!") + 1)
                        + "/META-INF/MANIFEST.MF";
                Manifest manifest = new Manifest(new URL(manifestPath).openStream());
                Attributes attr = manifest.getMainAttributes();
                BUILD_TIME = attr.getValue("Build-Time");
                BUILD_REVISION = attr.getValue("Revision");
            } catch (MalformedURLException ex) {
                LOGGER.info("MalformedURLException", ex);
            } catch (IOException ex) {
                LOGGER.info("IOException", ex);
            }
        }
    }

    private static void initalizeCommandLineArgs(String[] args) {
        Options options = new Options();
        options.addOption("d", false, "Debug, default seed (42), all the menus can be skipped");
        options.addOption("t", false, "Run tool viewer");
        options.addOption("headless", false, "Headless start");

        Option translateOption = Option.builder("translate")
                .optionalArg(true)
                .numberOfArgs(1)
                .desc("Debug translations")
                .build();
        options.addOption(translateOption);

        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine commandLineArgs = parser.parse(options, args);
            DEBUG = commandLineArgs.hasOption('d');
            TOOLS = commandLineArgs.hasOption('t');
            HEADLESS = commandLineArgs.hasOption("headless");
            TRANSLATE_TEST = commandLineArgs.hasOption("translate");

            //Deal with locale
            String translateLocale = commandLineArgs.getOptionValue("translate");
            LOGGER.info("Loading Locale " + translateLocale);
            if (translateLocale != null) {
                //Set locale to test, later
                try {
                    ConquerSpace.locale = LocaleUtils.toLocale(translateLocale);
                } catch (IllegalArgumentException iae) {
                    LOGGER.warn("Invalid locale " + translateLocale + " using default: " + ConquerSpace.DEFAULT_LOCALE.toString());
                    ConquerSpace.locale = DEFAULT_LOCALE;
                }
            }

        } catch (ParseException ex) {
            LOGGER.warn(ex);
        }
    }

    static void configureMusic() {
        GameController.musicPlayer = new MusicPlayer();
        if (settings.isPlayMusic()) {
            GameController.musicPlayer.playMusic();
        } else {
            GameController.musicPlayer.stopMusic();
        }
        try {
            GameController.musicPlayer.setVolume(settings.getMusicVolume());
        } catch (NumberFormatException nfe) {
            GameController.musicPlayer.setVolume(1);
        } catch (IllegalArgumentException iae) {
            GameController.musicPlayer.setVolume(1);
        }
    }

    static void setDebugUniverseGenerator() {
        UniverseGenerationConfig config = new UniverseGenerationConfig();

        config.universeSize = UniverseGenerationConfig.UniverseSize.Medium;
        config.universeShape = UniverseGenerationConfig.UniverseShape.Irregular;
        config.universeAge = UniverseGenerationConfig.UniverseAge.Ancient;
        config.civilizationCount = UniverseGenerationConfig.CivilizationCount.Common;
        config.planetCommonality = UniverseGenerationConfig.PlanetRarity.Common;

        //XD
        config.seed = (42);

        //Set the player Civ options
        CivilizationConfig civilizationConfig = new CivilizationConfig();
        civilizationConfig.civColor = (Color.CYAN);
        civilizationConfig.civSymbol = ("A");
        civilizationConfig.civilizationName = ("Humans");
        civilizationConfig.civilizationPreferredClimate = RacePreferredClimateTpe.Varied;
        civilizationConfig.homePlanetName = ("Earth");
        civilizationConfig.speciesName = ("Earthlings");
        civilizationConfig.civCurrencyName = ("Money");
        civilizationConfig.civCurrencySymbol = ("M");
        config.civConfig = (civilizationConfig);

        //Create generator
        DefaultUniverseGenerator gen = new DefaultUniverseGenerator(config, civilizationConfig, 42);
        generator = gen;
    }

    public static void exitGame() {

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
