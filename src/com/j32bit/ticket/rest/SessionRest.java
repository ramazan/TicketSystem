package com.j32bit.ticket.rest;

import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.j32bit.ticket.bean.User;

@Path("/session")
public class SessionRest {

	private static final Logger logger = LogManager.getLogger();

	@Path("/getAuthenticatedUser")
	@POST
	@PermitAll
	@Produces(MediaType.APPLICATION_JSON)
	public User getAuthenticatedUser(@Context HttpServletRequest request) {

		HttpSession session = request.getSession();
		User authenticatedUser = (User) session.getAttribute("LOGIN_USER");

		logger.debug("getAuthenticatedUser finished. userEmail:" + authenticatedUser.getEmail());
		return authenticatedUser;
	}
}
