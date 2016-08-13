package com.j32bit.ticket.rest;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.j32bit.ticket.bean.Department;
import com.j32bit.ticket.service.ServiceFacade;

@Path("department")
public class DepartmentRest {
	
	private static Logger logger = LogManager.getLogger();
	
	@POST
	@Path("getAllDepartments")
	@Produces(MediaType.APPLICATION_JSON)
	public Department[] getAllDepartments(){
		logger.debug("getAllDepartments started");
		Department [] departments = ServiceFacade.getInstance().getAllDepartments();
		logger.info("getAllDepartments dep #"+departments.length);
		return departments;
	}

}
