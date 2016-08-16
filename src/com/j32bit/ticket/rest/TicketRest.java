package com.j32bit.ticket.rest;

import java.util.ArrayList;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import com.j32bit.ticket.bean.Ticket;
import com.j32bit.ticket.service.ServiceFacade;

@Path("/ticket")
public class TicketRest {

	@Path("/addTicket")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@PermitAll
	public void storeTicket(Ticket ticket) throws Exception{
		ServiceFacade.getInstance().addTicket(ticket);
	}

	
	@Path("/getAllTickets")
	@GET
	@RolesAllowed("admin")
	public ArrayList<Ticket> getAllTickets() {
		return  ServiceFacade.getInstance().getAllTickets();
	}

}
