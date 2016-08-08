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

		String userEmail = request.getParameter("email");
		User authenticatedUser = ServiceFacade.getInstance().getUserDetailWithEmail(userEmail);

		HttpSession session = request.getSession();
		session.setAttribute("LOGIN_USER", authenticatedUser);

		try {
			response.sendRedirect("/Ticket_System/pages/dashboard.html");
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		logger.debug("user saved in session:" + authenticatedUser);
	}
}
