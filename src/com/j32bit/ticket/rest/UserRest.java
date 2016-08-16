package com.j32bit.ticket.rest;

import java.util.ArrayList;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.j32bit.ticket.bean.User;
import com.j32bit.ticket.service.ServiceFacade;

@Path("/user")
@XmlAccessorType(XmlAccessType.FIELD)
@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML ,MediaType.TEXT_PLAIN})
public class UserRest {
	private static Logger logger = LogManager.getLogger();

	@GET
	public User getUserDetails(@Context HttpServletRequest request) throws Exception {
		String userId = request.getUserPrincipal().getName();
		logger.debug("UserRest - getUserDetails . userId : " + userId);
		User authenticatedUserDetails = (User) request.getSession().getAttribute("LOGIN_USER");
		authenticatedUserDetails.getEmail();
		return authenticatedUserDetails;
	}

	@Path("/addUser")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed("admin")
	public void addUser(User user) {
		ServiceFacade.getInstance().addUser(user);
	}

	@Path("/getAllUsers")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed("admin")
	public ArrayList<User> getAllUsers() {
		return ServiceFacade.getInstance().getAllUsers();
	}
	
	@Path("/updateUser")
	@POST
	public void updateUser(String password,@Context HttpServletRequest request) {
		User authenticatedUserDetails = (User) request.getSession().getAttribute("LOGIN_USER");
		String email=authenticatedUserDetails.getEmail();
		ServiceFacade.getInstance().updateUser(password,email);
	}
	

}
