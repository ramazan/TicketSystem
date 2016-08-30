package com.j32bit.ticket.rest;

import java.util.ArrayList;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
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
@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.TEXT_PLAIN })
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
	public void addUser(User user) throws Exception {
		ServiceFacade.getInstance().addUser(user);
	}

	@Path("/getAllUsers")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed("admin")
	public ArrayList<User> getAllUsers() {
		return ServiceFacade.getInstance().getAllUsers();
	}

	@Path("/getUserInfo/{id}")
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public User getUserInfo(@PathParam("id") long ID) {
		return ServiceFacade.getInstance().getUserDetailWithID(ID);
	}

	@Path("/updateProfile")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@PermitAll
	public void updateUser(String password, @Context HttpServletRequest request) {
		User authenticatedUserDetails = (User) request.getSession().getAttribute("LOGIN_USER");
		String email = authenticatedUserDetails.getEmail();
		ServiceFacade.getInstance().updateProfile(password, email);
	}

	@Path("/deleteUser")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed("admin")
	public void deleteUser(User user) throws Exception {
		ServiceFacade.getInstance().deleteUser(user);
	}

	@Path("/updateUserData")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed("admin")
	public void updateUserData(User user) throws Exception {
		ServiceFacade.getInstance().updateUserData(user);
	}

}
