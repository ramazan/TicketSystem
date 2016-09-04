package com.j32bit.ticket.servlet;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.j32bit.ticket.bean.User;
import com.j32bit.ticket.service.ServiceFacade;

public class LoginServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static Logger logger = LogManager.getLogger();

	public void init() {
		logger.debug("login servlet initialized");
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) {

		logger.debug("login is started");
		
		String userEmail = request.getUserPrincipal().getName();
		logger.debug("login email:"+userEmail);
		User authenticatedUser = ServiceFacade.getInstance().getUserDetailWithEmail(userEmail);

		HttpSession session = request.getSession();
		logger.debug("session ID:"+session.getId());
		session.setAttribute("LOGIN_USER", authenticatedUser);
		session.setMaxInactiveInterval(1*15);

		try {
			response.sendRedirect("/Ticket_System/index.html");
			
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		logger.debug("login completed. userEmail:" + authenticatedUser.getEmail());
	}
}
