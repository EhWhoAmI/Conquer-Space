package ConquerSpace.util;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author Zyun
 */
public class CQSPLogger {
    /**
     *
     * @param name
     * @return
     */
    public static Logger getLogger(String name) {
        return LogManager.getLogger(name);
    }
    
    public static void initLoggers() {
         Logger log = LogManager.getLogger("ErrorLog");
    }
}
