package com.j32bit.ticket.rest;

import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.j32bit.ticket.bean.Company;
import com.j32bit.ticket.service.ServiceFacade;

@Path("company")
public class CompanyRest {

	@POST
	@Path("/addCompany")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Company addCompany(Company company) throws Exception {
		return ServiceFacade.getInstance().addCompany(company);
	}

	@GET
	@Path("/getAllCompanies")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Company> getAllCompanies() {
		return ServiceFacade.getInstance().getAllCompanies();
	}

	@GET
	@Path("/getCompany")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Company getCompany(long companyID) {
		return ServiceFacade.getInstance().getCompany(companyID);
	}

}
