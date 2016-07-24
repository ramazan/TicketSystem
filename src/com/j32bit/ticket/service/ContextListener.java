//ServiceFacade'dan sonra tekrar düzenlenecek
package com.j32bit.ticket.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.apache.logging.log4j.Logger;

import com.j32bit.ticket.service.ServiceFacade;

import org.apache.logging.log4j.LogManager;


public class ContextListener implements ServletContextListener{
	final static Logger logger = LogManager.getLogger(ContextListener.class);

	public void contextDestroyed(ServletContextEvent e){
		logger.debug("ServletContextListener has destroyed");
	}
	
	public void contextInitialized(ServletContextEvent e){
		logger.debug("ServletContextListener has initialized");
		Properties prop = ReadProp();
		ServiceFacade.getInstance().init(prop);
		logger.debug("ServletContextListener has finished");
	}
	

	
	public Properties ReadProp(){
		Properties prop = new Properties();
		InputStream input = getClass().getClassLoader().getResourceAsStream("ticket.properties"); 
		try {
			prop.load(input);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 logger.debug("Connection is provided");
		 
		 prop.getProperty("dbUsername");
		 prop.getProperty("dbPassword");
		 prop.getProperty("dbPort");
		 prop.getProperty("dbServerName");
		 prop.getProperty("dbName");

		return prop;
	}
	
	

}
