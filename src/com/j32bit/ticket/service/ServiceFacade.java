package com.j32bit.ticket.service;

import java.util.ArrayList;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.j32bit.ticket.bean.User;
import com.j32bit.ticket.dao.ticket.TicketDAOService;
import com.j32bit.ticket.dao.user.UserDAOService;

public class ServiceFacade {

	private final static Logger logger = LogManager.getLogger();

	private static ServiceFacade instance = null;

	private UserDAOService userService = null;
	private TicketDAOService ticketService = null;

	private ServiceFacade() {
		logger.debug("ServiceFacade constructed");
	} // singleton protect

	public static ServiceFacade getInstance() {

		if (instance == null) {
			instance = new ServiceFacade();
		}
		return instance;
	}

	public void init(Properties prop) {
		logger.debug("initialize started");
		userService = new UserDAOService();
		userService.init(prop);
		ticketService = new TicketDAOService();
		ticketService.init(prop);
		logger.debug("initialize finished");
	}
	
	// this is a test
	public User getUser(String email){
		User user = userService.getUser(email);
		return user;
	}
	

}
