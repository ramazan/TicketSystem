package com.j32bit.ticket.rest;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.j32bit.ticket.bean.User;
import com.j32bit.ticket.service.ServiceFacade;

@Path("/admin")
public class AdminRestful {
	
	private static Logger logger = LogManager.getLogger();
	
	@Path("/addUser")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed("admin")
	public void addUser(User user){
		ServiceFacade.getInstance().addUser(user);
		logger.debug("User added: "+user);
	}

}
