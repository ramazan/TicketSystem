package com.j32bit.ticket.servlet.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
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

	private ArrayList<String> urlList;

	public void destroy() {
	}

	// yapilan tüm istekleri filtrele sessison bittiyse yönlendir
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		String url = request.getServletPath();
		boolean allowedRequest = false;

		if (urlList.contains(url)) {
			allowedRequest = true;
		}	

		if (!allowedRequest) {
			HttpSession session = request.getSession(false);
			if (null == session) {
				response.sendRedirect("index.html");
			}
		}
		chain.doFilter(req, res);
	}

	public void init(FilterConfig config) throws ServletException {
		logger.info("SessionFilter initialize started");
		String urls = config.getInitParameter("urls");
		StringTokenizer token = new StringTokenizer(urls, ",");

		urlList = new ArrayList<String>();

		while (token.hasMoreTokens()) {
			urlList.add(token.nextToken());
		}
		logger.info("SessionFilter is initialized");
	}
}
