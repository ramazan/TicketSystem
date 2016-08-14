package com.j32bit.ticket.service;

import java.util.ArrayList;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.j32bit.ticket.bean.Company;
import com.j32bit.ticket.bean.Department;
import com.j32bit.ticket.bean.Ticket;
import com.j32bit.ticket.bean.User;
import com.j32bit.ticket.dao.CompanyDAOService;
import com.j32bit.ticket.dao.DepartmentDAOService;
import com.j32bit.ticket.dao.TicketDAOService;
import com.j32bit.ticket.dao.UserDAOService;
import com.j32bit.ticket.enums.Status;

public class ServiceFacade {

	private final static Logger logger = LogManager.getLogger();

	private static ServiceFacade instance = null;

	private UserDAOService userService = null;
	private TicketDAOService ticketService = null;
	private CompanyDAOService companyService = null;
	private DepartmentDAOService departmentDAOService = null;

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
		
		companyService = new CompanyDAOService();
		companyService.init(prop);
		
		departmentDAOService = new DepartmentDAOService();
		departmentDAOService.init(prop);
		logger.debug("initialize finished");
	}

	public void addUser(User user) {
		 try {
			userService.addUser(user);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public ArrayList<User> getAllUsers() {
		ArrayList<User> users = userService.getAllUsers();
		logger.info("GET-USER # " + users.size());
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
	
	public ArrayList<Company> getAllCompanies(){
		return companyService.getAllcompanies();
	}

	public Department[] getAllDepartments() {
		return departmentDAOService.getAllDepartments();
	}

}
