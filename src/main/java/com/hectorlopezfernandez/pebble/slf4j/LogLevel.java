package com.hectorlopezfernandez.pebble.slf4j;

import org.slf4j.Logger;

public enum LogLevel {

	ERROR {
		public void log(Logger logger, String msg) { logger.error(msg); }
	},
	WARN {
		public void log(Logger logger, String msg) { logger.warn(msg); }
	},
	INFO {
		public void log(Logger logger, String msg) { logger.info(msg); }
	},
	DEBUG {
		public void log(Logger logger, String msg) { logger.debug(msg); }
	},
	TRACE {
		public void log(Logger logger, String msg) { logger.trace(msg); }
	};
	
	public abstract void log(Logger logger, String msg);

}