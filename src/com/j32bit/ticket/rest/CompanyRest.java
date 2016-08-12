package com.j32bit.ticket.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.j32bit.ticket.bean.Company;
import com.j32bit.ticket.bean.Result;
import com.j32bit.ticket.service.ServiceFacade;

@Path("company")
public class CompanyRest {

	private static Logger logger = LogManager.getLogger();

	@POST
	@Path("/addCompany")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Result addCompany(Company company) {
		logger.debug("addComany started for :" + company);
		Result result = ServiceFacade.getInstance().addCompany(company);
		logger.info("addCompany finished status: " + result.status);

		return result;
	}

	@POST
	@Path("/getAllCompanies")
	@Produces(MediaType.APPLICATION_JSON)
	public Company[] getAllCompanies() {
		logger.debug("getAllCompanies started");
		Company[] companies = ServiceFacade.getInstance().getAllCompanies();
		logger.debug("getAllCompanies finished. Total company num:" + companies.length);
		return companies;
	}

}
