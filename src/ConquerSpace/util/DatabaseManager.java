package ConquerSpace.util;

import ConquerSpace.ConquerSpace;
import ConquerSpace.game.GameUpdater;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

/**
 *
 * @author Zyun
 */
public class DatabaseManager {
    private static final Logger LOGGER = CQSPLogger.getLogger(GameUpdater.class.getName());

    public Connection dataBaseConnection;

    public void init() throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {
        //Init database
        //Get number of saves
        File saveDir = new File(System.getProperty("user.dir") + "/save");
        if (!(saveDir.exists() && saveDir.isDirectory())) {
            saveDir.mkdirs();
        }
        //Then view saves
        //Little regex magic here...
        FileFilter filter = new RegexFileFilter("^save\\d+$");
        Pattern lastIntPattern = Pattern.compile("[^0-9]+([0-9]+)$");
        int largest = 0;
        for (File f : saveDir.listFiles(filter)) {
            String name = f.getName();
            Matcher matcher = lastIntPattern.matcher(name);
            if (matcher.find()) {
                String someNumberStr = matcher.group(1);
                int lastNumberInt = Integer.parseInt(someNumberStr);
                if (lastNumberInt > largest) {
                    largest = lastNumberInt;
                }
            }

        }

        //Create save dir.
        File gameSaveDir = new File(System.getProperty("user.dir") + "/save/save" + (largest+1));
        gameSaveDir.mkdir();
        
        //Now init database
        String driver = "org.apache.derby.jdbc.EmbeddedDriver";
        String protocol = "jdbc:derby:";
        Class.forName(driver).newInstance();
        dataBaseConnection = DriverManager.getConnection(protocol + gameSaveDir.getAbsolutePath() + "/database" + ";create=true");
        dataBaseConnection.setAutoCommit(true);
        //Now init the database
        Scanner fileScanner = new Scanner(new File(System.getProperty("user.dir") + "/assets/scripts/sql/database/init/files.txt"));
        while (fileScanner.hasNextLine()) {
            String fileName = fileScanner.nextLine();
            File fp = new File(System.getProperty("user.dir") + "/assets/scripts/sql/database/init/" + fileName);
            byte[] encoded = Files.readAllBytes(fp.toPath());
            LOGGER.info("Parsing file " + fileName);
            String text = new String(encoded, StandardCharsets.US_ASCII);
            Statement statement = dataBaseConnection.createStatement();
            statement.execute(text);
        }
        
        //Create metadata file
        File metadata = new File(gameSaveDir.getAbsolutePath() + "/meta");
        PrintWriter metawriter = new PrintWriter(metadata);
        DatabaseMetaData dbmd = dataBaseConnection.getMetaData();
        JSONObject root = new JSONObject();
        JSONObject versionObject = new JSONObject();
        versionObject.put("major", ConquerSpace.VERSION.getMajor());
        versionObject.put("minor", ConquerSpace.VERSION.getMinor());
        versionObject.put("patch",ConquerSpace.VERSION.getPatch());
        root.put("version", versionObject);
        root.put("dbversion", dbmd.getDriverVersion());
        root.write(metawriter);
        metawriter.flush();
        metawriter.close();
        //Time, 
        //Etc...
    }
    
    public void shutdown() throws SQLException{
        try {
                DriverManager.getConnection("jdbc:derby:;shutdown=true");
            } catch (SQLException se) {
                if (( (se.getErrorCode() == 50000)
                            && ("XJ015".equals(se.getSQLState()) ))) {
                        // we got the expected exception
                        LOGGER.info("Derby shut down normally");
                        // Note that for single database shutdown, the expected
                        // SQL state is "08006", and the error code is 45000.
                    } else {
                        // if the error code or SQLState is different, we have
                        // an unexpected exception (shutdown failed)
                        LOGGER.error("Derby did not shut down normally", se);
                        throw se;
                    }

            }
    }
    
    public void quietShutdown() {
        try {
                DriverManager.getConnection("jdbc:derby:;shutdown=true");
            } catch (SQLException se) {
                if (( (se.getErrorCode() == 50000)
                            && ("XJ015".equals(se.getSQLState()) ))) {
                        // we got the expected exception
                        LOGGER.info("Derby shut down normally");
                        // Note that for single database shutdown, the expected
                        // SQL state is "08006", and the error code is 45000.
                    } else {
                        // if the error code or SQLState is different, we have
                        // an unexpected exception (shutdown failed)
                        LOGGER.error("Derby did not shut down normally", se);
                    }
            }
    }
}
