package ConquerSpace.util;

import java.util.logging.Logger;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;

public class CQSPLogger {
	public static Logger getLogger (String name) {
		Handler hand = new ConsoleHandler();
		Logger log = Logger.getLogger(name);
		log.addHandler(hand);
		return log;
	}
}