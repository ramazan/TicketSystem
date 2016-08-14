package com.j32bit.ticket.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TicketExceptionUtils extends ExceptionUtils {

	private static Logger log =  LogManager.getLogger();

	public static Exception handleException(Exception exception, String job) {

		String errMsg = "";
		Exception newException = null;

		if (exception instanceof WebApplicationException) {
			Response response = ((WebApplicationException) exception).getResponse();

			if (response.getStatus() == 403 && response.getEntity() == null) {
				errMsg = "Access denied";
			} else if (response.getStatus() == 404 && response.getEntity() == null) {
				errMsg = "Resource not found";
			}
			newException = new Exception(errMsg);
		} else {
			errMsg = "\nAn error occured. ("+ exception.getMessage()+")";
			newException = new Exception(errMsg);
		}

		log.debug("Error Message : " + errMsg);

		log.error(exception, exception);

		return newException;
	}

}
