package com.j32bit.ticket.service;

import java.util.ArrayList;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.j32bit.ticket.bean.Company;
import com.j32bit.ticket.bean.Department;
import com.j32bit.ticket.bean.Ticket;
import com.j32bit.ticket.bean.TicketResponse;
import com.j32bit.ticket.bean.User;
import com.j32bit.ticket.dao.CompanyDAOService;
import com.j32bit.ticket.dao.DepartmentDAOService;
import com.j32bit.ticket.dao.TicketDAOService;
import com.j32bit.ticket.dao.UserDAOService;

public class ServiceFacade {

	private final static Logger logger = LogManager.getLogger();

	private static ServiceFacade instance = null;

	private UserDAOService userService = null;
	private TicketDAOService ticketService = null;
	private CompanyDAOService companyService = null;
	private DepartmentDAOService departmentDAOService = null;

	private ServiceFacade() {
		logger.info("ServiceFacade constructed");
	} // singleton protect

	public static ServiceFacade getInstance() {

		if (instance == null) {
			instance = new ServiceFacade();
		}
		return instance;
	}

	public void init(Properties prop) {
		logger.info("initialize started");
		userService = new UserDAOService();
		userService.init(prop);

		ticketService = new TicketDAOService();
		ticketService.init(prop);

		companyService = new CompanyDAOService();
		companyService.init(prop);

		departmentDAOService = new DepartmentDAOService();
		departmentDAOService.init(prop);
		logger.info("initialize finished");
	}

	public void addUser(User user) {
		try {
			userService.addUser(user);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ArrayList<User> getAllUsers() {
		ArrayList<User> users = userService.getAllUsers();
		logger.info("GET-USER # " + users.size());
		return users;
	}

	public ArrayList<Ticket> getAllTickets(int status,User user) {
		ArrayList<Ticket> tickets = new ArrayList<>();
		if(user.checkRole("admin"))
			tickets = ticketService.getAllTickets(status);
		else if(user.checkRole("supporter"))
			tickets= ticketService.getAllDepartmentTickets(status,user.getDepartment().getId());
		else if(user.checkRole("client"))
			tickets = ticketService.getAllTickets(status);
		return tickets;
	}

	public User getUserDetailWithEmail(String userEmail) {
		return userService.getUser(userEmail);
	}

	public User getUserDetailWithID(long ID) {
		return userService.getUser(ID);
	}

	public void addTicket(Ticket ticket) throws Exception {
		ticketService.addTicket(ticket);
	}

	public void deleteTicket(long ticketID) throws Exception {
		ticketService.deleteTicket(ticketID);
	}

	public void editTicket(Ticket ticket) throws Exception {
		ticketService.editTicket(ticket);
	}

	public Company addCompany(Company company) {
		return companyService.addCompany(company);
	}

	public Company getCompany(long id) {
		return companyService.getCompany(id);
	}

	public ArrayList<Company> getAllCompanies() {
		return companyService.getAllcompanies();
	}

	public ArrayList<Department> getAllDepartments() {
		return departmentDAOService.getAllDepartments();
	}

	public Department getDepartment(long departmentID) {
		return departmentDAOService.getDepartment(departmentID);
	}

	public Department addDeparment(Department department) {
		return departmentDAOService.addDepartment(department);
	}

	public void updateProfile(String password, String email) {
		try {
			logger.debug("update  user service2 password " + password);

			userService.updateProfile(password, email);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Ticket getTicketDetails(long ID) {
		logger.debug("ServiceFacade getTicketDetails started  ID = " + ID);
		return ticketService.getTicketDetails(ID);
	}

	public TicketResponse addResponse(TicketResponse response) {
		return ticketService.addResponse(response);
	}

	public ArrayList<TicketResponse> getAllResponses(long ticketID) {
		return ticketService.getAllResponses(ticketID);
	}

	public void deleteUser(long userID, String email) throws Exception {
		userService.deleteUser(userID, email);
	}

	public void updateUserData(User user, String email) {
		try {
			userService.updateUserData(user, email);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
