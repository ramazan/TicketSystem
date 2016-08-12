package com.j32bit.ticket.service;

import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.j32bit.ticket.bean.Company;
import com.j32bit.ticket.bean.Ticket;
import com.j32bit.ticket.bean.User;
import com.j32bit.ticket.dao.CompanyDAOService;
import com.j32bit.ticket.dao.TicketDAOService;
import com.j32bit.ticket.dao.UserDAOService;

public class ServiceFacade {

	private final static Logger logger = LogManager.getLogger();

	private static ServiceFacade instance = null;

	private UserDAOService userService = null;
	private TicketDAOService ticketService = null;
	private CompanyDAOService companyService = null;

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

	public void addUser(User user) {
		userService.addUser(user);
	}

	public User[] getAllUsers() {
		User[] users = userService.getAllUsers();
		logger.info("GET-USER # " + users.length);
		return users;
	}

	public Ticket[] getAllTickets() {
		Ticket[] tickets = ticketService.getAllTickets();
		logger.info("GET-TICKET # " + tickets.length);
		return tickets;
	}

	public User getUserDetailWithEmail(String userEmail) {
		return userService.getUser(userEmail);
	}

	// TODO: STORE TICKET DUZENLENECEK
	public void storeTicket(Ticket ticket) throws Exception {
		// ticketService.storeTicket(ticket);
	}

	public void addCompany(Company company) {
		companyService.addCompany(company);
	}

}
