package com.j32bit.ticket.rest;

import java.util.ArrayList;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import com.j32bit.ticket.bean.Ticket;
import com.j32bit.ticket.service.ServiceFacade;

@Path("/ticket")
@XmlAccessorType(XmlAccessType.FIELD)
@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML ,MediaType.TEXT_PLAIN})
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
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed("admin")
	public ArrayList<Ticket> getAllTickets() {
		return  ServiceFacade.getInstance().getAllTickets();
	}

}
