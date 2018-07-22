package de.slag.binclock;

import java.util.logging.Logger;

public class LogFactory {
	
	public static Logger getLog(final Class c) {
		return Logger.getLogger(c.getClass().getName());
	}

}
