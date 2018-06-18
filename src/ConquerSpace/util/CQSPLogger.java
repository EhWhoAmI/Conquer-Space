package ConquerSpace.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Logger for CQSP.
 * @author Zyun
 */
public class CQSPLogger {
    /**
     * Inits a logger.
     * @param name name of logger. Usually the name of the class
     * @return Logger.
     */
    public static Logger getLogger(String name) {
        return LogManager.getLogger(name);
    }
    
    /**
     * Init the Error log.
     */
    public static void initLoggers() {
         LogManager.getLogger("ErrorLog");
    }
}
