package com.j32bit.ticket.servlet.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SessionFilter implements Filter {
	private static Logger logger = LogManager.getLogger();

	public void destroy() {
		logger.info("sessionFilter is destroyed");
	}

	// yapilan tüm istekleri filtrele sessison bittiyse yönlendir
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		HttpSession session = request.getSession(false);

		logger.debug("url:" + request.getRequestURI());

		// logine git
		if (request.getRequestURI().startsWith("/Ticket_System/login")) {
			logger.debug("goLogin");
			chain.doFilter(request, response);
		} else if (request.getRequestURI().startsWith("/Ticket_System/rest/")) {
			if (session == null || session.isNew() || session.getAttribute("LOGIN_USER") == null) {
				logger.debug("Has timed out");
				response.sendRedirect(request.getContextPath());
			} else {
				chain.doFilter(request, response);
			}
		} else { // izin ver
			logger.debug("url pass");
			chain.doFilter(request, response);
		}
	}

	public void init(FilterConfig config) throws ServletException {
		logger.info("SessionFilter is initialized");

	}
}
