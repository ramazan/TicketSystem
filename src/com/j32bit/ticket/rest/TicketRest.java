package com.j32bit.ticket.rest;

import java.util.ArrayList;

import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import com.j32bit.ticket.bean.Ticket;
import com.j32bit.ticket.bean.TicketResponse;
import com.j32bit.ticket.bean.User;
import com.j32bit.ticket.service.ServiceFacade;

@Path("/ticket")
@PermitAll
@XmlAccessorType(XmlAccessType.FIELD)
@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.TEXT_PLAIN })
public class TicketRest {

	@Path("/addTicket")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@PermitAll
	public void storeTicket(Ticket ticket) throws Exception {
		ServiceFacade.getInstance().addTicket(ticket);
	}
	
	@Path("/getPostedTickets")
	@GET
	@PermitAll
	public ArrayList<Ticket> getPostedTickets(@Context HttpServletRequest request){
		
		User user = (User) request.getSession().getAttribute("LOGIN_USER");
		int status = Integer.valueOf(request.getParameter("status"));
		long userID = user.getId();
		return ServiceFacade.getInstance().getPostedTickets(status,userID);

	}

	@Path("/getAllTickets")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@PermitAll
	public ArrayList<Ticket> getAllTickets(@Context HttpServletRequest request) {
		
		User user = (User) request.getSession().getAttribute("LOGIN_USER");
		int status = Integer.valueOf(request.getParameter("status"));
		
		return ServiceFacade.getInstance().getAllTickets(status,user);
	}

	@Path("/getTicketDetails")
	@POST
	@PermitAll
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Ticket getTicketDetails(long ID) throws Exception {
		return ServiceFacade.getInstance().getTicketDetails(ID);
	}

	@Path("/addResponse")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@PermitAll
	public TicketResponse addResponse(TicketResponse response) {
		return ServiceFacade.getInstance().addResponse(response);
	}

	@Path("/getAllResponses")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@PermitAll
	public ArrayList<TicketResponse> getAllResponses(long ticketID) {
		return ServiceFacade.getInstance().getAllResponses(ticketID);
	}

	@Path("/deleteTicket")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@PermitAll
	public void deleteTicket(long ticketID) throws Exception {
		ServiceFacade.getInstance().deleteTicket(ticketID);
	}

	@Path("/editTicket")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@PermitAll
	public void editTicket(Ticket ticket) throws Exception {
		ServiceFacade.getInstance().editTicket(ticket);
	}

}
