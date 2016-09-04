package com.j32bit.ticket.rest;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.j32bit.ticket.bean.Department;
import com.j32bit.ticket.bean.User;
import com.j32bit.ticket.service.ServiceFacade;

@Path("department")
public class DepartmentRest {

	
	@Path("/getAllDepartments")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Department> getAllDepartments() {
		return ServiceFacade.getInstance().getAllDepartments();
	}
	
	@POST
	@Path("/addDepartment")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Department addDepartment(Department department) throws Exception {
		return ServiceFacade.getInstance().addDeparment(department);
	}
	
	@POST
	@Path("/getDepartmentDetails")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Department getDepartmentDetails(long ID) throws Exception {
		return ServiceFacade.getInstance().getDepartment(ID);
	}
	
	@POST
	@Path("/deleteDepartment")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public void deleteDepartment(long ID) throws Exception {
		 ServiceFacade.getInstance().deleteDepartment(ID);
	}
	
	@POST
	@Path("/getBadges")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Integer>  getBadges(@Context HttpServletRequest request){
		User user = (User) request.getSession().getAttribute("LOGIN_USER");
		long userID = user.getId();
		return ServiceFacade.getInstance().getBadges(userID);
	}
}
