package com.j32bit.ticket.rest;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import com.j32bit.ticket.bean.Ticket;
import com.j32bit.ticket.service.ServiceFacade;

@Path("/ticket")
public class TicketRest {

	@Path("/getAllTickets")
	@GET
	@RolesAllowed("admin")
	public Ticket[] getAllTickets() {
		Ticket[] tickets = ServiceFacade.getInstance().getAllTickets();
		return tickets;
	}

}
