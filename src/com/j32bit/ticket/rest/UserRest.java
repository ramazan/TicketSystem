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
import com.j32bit.ticket.enums.Status;
import com.j32bit.ticket.service.ServiceFacade;

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
	public Status addUser(User user) {
		Status status = ServiceFacade.getInstance().addUser(user);
		logger.debug("addUser status: " + status);
		return status;
	}

	@Path("/getAllUsers")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed("admin")
	public User[] getAllUsers() {
		return ServiceFacade.getInstance().getAllUsers();
	}

}
