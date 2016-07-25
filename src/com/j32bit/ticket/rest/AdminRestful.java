package com.j32bit.ticket.rest;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.j32bit.ticket.bean.User;
import com.j32bit.ticket.service.ServiceFacade;

@Path("/")
public class AdminRestful {
	
	private static Logger logger = LogManager.getLogger();
	
	@Path("/test")
	@GET
	@RolesAllowed("admin")
	public void test(){
		
		User user = ServiceFacade.getInstance().getUser("hm");
		System.out.println("Test User: "+user);
		
	}

}
