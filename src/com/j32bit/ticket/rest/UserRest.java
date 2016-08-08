package com.j32bit.ticket.rest;

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

/**
 * @author burak UserResource
 *
 *         This class is used for backend of User.js
 *
 */
@Path("/user")
@XmlAccessorType(XmlAccessType.FIELD)
@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
public class UserRest {
	private static Logger logger = LogManager.getLogger();


	@GET
	public User getUserDetails(@Context HttpServletRequest request) throws Exception {
		String userId = request.getUserPrincipal().getName();
		logger.debug("UserRest - getUserDetails . userId : " + userId);
		User authenticatedUserDetails = (User) request.getSession().getAttribute("LOGIN_USER");
		return authenticatedUserDetails;
	}

	@Path("/addUser")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed("admin")
	public void addUser(User user) {
		ServiceFacade.getInstance().addUser(user);
		logger.debug("User added: " + user);
	}

	@Path("/getAllUsers")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed("admin")
	public User[] getAllUsers() {
		return ServiceFacade.getInstance().getAllUsers();
	}

}
