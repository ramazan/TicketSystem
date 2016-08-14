package com.j32bit.ticket.exception;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ExceptionMapperImpl implements ExceptionMapper<Exception> {

	public Response toResponse(Exception e) {
		List<StringKeyValuePer> exceptionList = new ArrayList<StringKeyValuePer>();
		exceptionList.add(new StringKeyValuePer(TicketExceptionUtils.handleException(e, e.getMessage()).getMessage()));
		GenericEntity<List<StringKeyValuePer>> entity = new GenericEntity<List<StringKeyValuePer>>(exceptionList) {
		};
		return Response.status(400).entity(entity).build();
	}

}
