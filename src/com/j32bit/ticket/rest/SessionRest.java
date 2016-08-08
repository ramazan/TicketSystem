package com.j32bit.ticket.rest;

import java.io.IOException;

import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.j32bit.ticket.bean.User;
import com.j32bit.ticket.service.ServiceFacade;

@Path("/session")
public class SessionRest {

	private static final Logger logger = LogManager.getLogger();

	@Path("/login")
	@GET
	@PermitAll
	public void login(@Context HttpServletRequest request, @Context HttpServletResponse response) {

		String userEmail = request.getParameter("email");
		User authenticatedUser = ServiceFacade.getInstance().getUserDetailWithEmail(userEmail);

		HttpSession session = request.getSession();
		session.setAttribute("LOGIN_USER", authenticatedUser);

		try {
			response.sendRedirect("/Ticket_System/pages/dashboard.html");
			logger.debug("user login successful. User:" + authenticatedUser);
		} catch (IOException e) {
			logger.error(e.getMessage());
		}

	}

	@Path("/getAuthenticatedUser")
	@GET
	@PermitAll
	@Produces(MediaType.APPLICATION_JSON)
	public User getAuthenticatedUser(@Context HttpServletRequest request) {

		HttpSession session = request.getSession();
		User authenticatedUser = (User) session.getAttribute("LOGIN_USER");

		logger.debug("getAuthenticatedUser finished. User:" + authenticatedUser);
		return authenticatedUser;
	}

	@Path("/logout")
	@GET
	@PermitAll
	public void logout(@Context HttpServletRequest request, @Context HttpServletResponse response) {

		HttpSession session = request.getSession();
		session.invalidate();
		logger.debug("logout status : success");
		
	}
}
