package com.infosys.UTAF;

import org.apache.log4j.Logger;

public class UTAFLog {
	private static Logger Log = Logger.getLogger(UTAFLog.class.getName());
	
	public static void info(String message) {
		Log.info(message);
	}

	public static void warn(String message) {
		Log.warn(message);
	}
	
	public static void error(String message) {
		Log.error(message);
	}
	
	public static void fatal(String message) {
		Log.fatal(message);
	}
	
	public static void debug(String message) {
		Log.debug(message);
	}
	public static void trace(String message) {
		Log.trace(message);
	}



}
