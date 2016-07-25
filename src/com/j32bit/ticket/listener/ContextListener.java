package com.j32bit.ticket.listener;

import java.io.InputStream;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.j32bit.ticket.service.ServiceFacade;

public class ContextListener implements ServletContextListener {

	private final static Logger logger = LogManager.getLogger();

	public void contextInitialized(ServletContextEvent e) {
		logger.debug("ContextListener has started");
		Properties prop = readPropertiesFromFile("ticket.properties");
		ServiceFacade.getInstance().init(prop);
		logger.debug("ContextListener has finished");
	}

	public void contextDestroyed(ServletContextEvent e) {
		logger.debug("ContextListener has destroyed");
	}

	private Properties readPropertiesFromFile(String filePath) {

		Properties prop = null;
		InputStream input = null;
		logger.debug("Reading properties file started");

		try {
			input = getClass().getClassLoader().getResourceAsStream(filePath);
			prop = new Properties(); // creates empty file
			prop.load(input);
		} catch (Exception e) {
			logger.error("Properties file reading error : " + e.getMessage());
			e.printStackTrace();
		}
		logger.debug("Reading properties file completed");

		return prop; // return prop or null
	}
}
