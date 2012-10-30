package com.gmail.takashi316.easyguide.content;

import java.util.logging.Level;
import java.util.logging.Logger;

public class MyLogger {

	private static Logger logger;

	private static void initLogger() {
		if (logger != null) return;
		logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
		logger.setLevel(Level.INFO);
	}
	
	static void info(String s){
		initLogger();
		logger.info(s);
	}
	
}
