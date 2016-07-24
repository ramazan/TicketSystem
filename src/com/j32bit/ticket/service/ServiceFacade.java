package com.j32bit.ticket.service;

import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.j32bit.ticket.dao.ticket.TicketDAOService;
import com.j32bit.ticket.dao.user.UserDAOService;
public class ServiceFacade {
	
	private final static Logger logger= LogManager.getLogger();
	
	private static ServiceFacade instance =null;
	//TODO: add this interfaces 
	private UserDAOService userService=null;
	private TicketDAOService ticketService=null;
	
	private ServiceFacade(){
		logger.debug("ServiceFacade constructed");
	} // singleton protect
	
	public static ServiceFacade getInstance(){
		
		if(instance==null){
			instance = new ServiceFacade();
		}
		return instance;
	}
	
	public void init(Properties prop){
		userService = new UserDAOService();
		userService.init(prop);
		ticketService = new TicketDAOService();
		ticketService.init(prop);
	}
	

}
