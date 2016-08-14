package com.j32bit.ticket.rest;

import java.util.ArrayList;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.j32bit.ticket.bean.Department;
import com.j32bit.ticket.service.ServiceFacade;

@Path("department")
public class DepartmentRest {

	@POST
	@Path("getAllDepartments")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Department> getAllDepartments() {
		return ServiceFacade.getInstance().getAllDepartments();
	}

}
