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




public class LoginServlet extends HttpServlet{
	
	
	private static final long serialVersionUID = 1L;
	private Logger logger = LogManager.getLogger();
	
	public void init(){
		logger.debug("Login servlet initialized");
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException{
		
		String email = request.getParameter("email");
		
		User user = ServiceFacade.getInstance().getUser(email);
		
		logger.debug("user logged"+user);
		
		HttpSession session = request.getSession();
		session.setAttribute("LOGIN_USER", user.getName());
		
		response.sendRedirect("/Ticket_System/pages/admin/admin-dashboard.jsp");
		
	}

}
