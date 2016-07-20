package com.j32bit.ticket.rest;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Path("/")
public class AdminRestful {
	
	private static Logger logger = LogManager.getLogger();
	
	@Path("/test")
	@GET
	@RolesAllowed("admin")
	public void test(){
		logger.debug("test completed");
		
	}

}
