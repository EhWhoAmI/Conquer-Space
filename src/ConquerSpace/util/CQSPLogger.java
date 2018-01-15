package ConquerSpace.util;

import java.io.File;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;

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
