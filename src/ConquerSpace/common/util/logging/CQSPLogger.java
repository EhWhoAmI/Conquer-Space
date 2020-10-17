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
package ConquerSpace.common.util.logging;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Logger for CQSP.
 * @author EhWhoAmI
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
