package com.j32bit.ticket.rest;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.j32bit.ticket.bean.Ticket;
import com.j32bit.ticket.bean.User;
import com.j32bit.ticket.service.ServiceFacade;

@Path("/ticket")
public class TicketRest {

	private static Logger logger = LogManager.getLogger();

	@Path("/addTicket")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed("client")
	public void storeTicket(Ticket ticket) throws Exception{
		ServiceFacade.getInstance().storeTicket(ticket);
		logger.debug("Ticket added: "+ticket);
	}

	
	@Path("/getAllTickets")
	@GET
	@RolesAllowed("admin")
	public Ticket[] getAllTickets() {
		Ticket[] tickets = ServiceFacade.getInstance().getAllTickets();
		return tickets;
	}

}
