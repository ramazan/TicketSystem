package com.j32bit.ticket.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.j32bit.ticket.bean.Department;
import com.j32bit.ticket.bean.Ticket;
import com.j32bit.ticket.bean.TicketResponse;
import com.j32bit.ticket.bean.User;

public class TicketDAOService extends ConnectionHelper {
	private Logger logger = LogManager.getLogger(TicketDAOService.class);

	public TicketDAOService() {
		logger.info("TicketDAOService constructed");
	}

	public void init(Properties prop) {
		logger.info("TicketDAOService service is initializing");
		super.init(prop);
		logger.info("TicketDAOService service is initialized");
	}

	public void addTicket(Ticket ticket) throws Exception {

		logger.debug("addTicket is started");
		Connection conn = null;
		PreparedStatement pStmt = null;
		StringBuilder query = new StringBuilder();
		StringBuilder querylog = new StringBuilder();
		try {
			query.append("INSERT INTO tickets ");
			query.append("(SENDER_ID, TITLE,DEPARTMENT_ID,MESSAGE,PRIORITY) ");
			query.append("VALUES(?,?,?,?,?)");
			String queryString = query.toString();
			logger.debug("Sql query Created : " + queryString);

			conn = getConnection();
			pStmt = conn.prepareStatement(queryString);

			if (logger.isTraceEnabled()) {
				querylog.append("Query : ").append(queryString).append("\n");
				querylog.append("Parameters : ").append("\n");
				querylog.append("TITLE : ").append(ticket.getTitle()).append("\n");
				querylog.append("MESSAGE : ").append(ticket.getMessage()).append("\n");
				querylog.append("SENDER_ID : ").append(ticket.getSender().getId()).append("\n");
				querylog.append("PRIORITY : ").append(ticket.getPriority()).append("\n");
				querylog.append("DEPARTMENT_ID : ").append(ticket.getDepartment().getId()).append("\n");
				logger.trace(querylog.toString());
			}

			pStmt.setLong(1, ticket.getSender().getId());
			pStmt.setString(2, ticket.getTitle());
			pStmt.setLong(3, ticket.getDepartment().getId());
			pStmt.setString(4, ticket.getMessage());
			pStmt.setInt(5, ticket.getPriority());

			pStmt.execute();

		} catch (SQLException e) {
			logger.error(e.getMessage());
		} finally {
			closePreparedStatement(pStmt);
			closeConnection(conn);
		}
		logger.debug("addTicket is finished");
	}
	
	public ArrayList<Ticket> getPostedTickets(int status, long userID){
		logger.debug("getPostedTickets started");
		Connection conn = null;
		PreparedStatement pStmt = null;
		ResultSet rs = null;
		StringBuilder query = new StringBuilder();

		ArrayList<Ticket> tickets = new ArrayList<>();

		try {
			query.append("SELECT tickets.*, users.FULL_NAME, departments.DEPARTMENT_NAME FROM tickets ");
			query.append("INNER JOIN users ON users.ID = tickets.SENDER_ID ");
			query.append("INNER JOIN departments ON tickets.DEPARTMENT_ID = departments.ID ");
			query.append("WHERE STATUS=? AND tickets.SENDER_ID=?");
			String queryString = query.toString();
			logger.debug("Sql query Created : " + queryString);

			conn = getConnection();
			pStmt = conn.prepareStatement(queryString);
			
			if(logger.isTraceEnabled()){
				StringBuilder queryLog = new StringBuilder();
				queryLog.append("Query : ").append(queryString).append("\n");
				queryLog.append("Parameters : ").append("\n");
				queryLog.append("STATUS : ").append(status).append("\n");
				queryLog.append("SENDER_ID : ").append(userID).append("\n");
				logger.trace(queryLog);
			}
			
			pStmt.setInt(1, status);
			pStmt.setLong(2, userID);
			rs = pStmt.executeQuery();

			while (rs.next()) {

				Ticket ticket = new Ticket();
				ticket.setId(rs.getLong("ID"));
				ticket.setTitle(rs.getString("TITLE"));
				ticket.setMessage(rs.getString("MESSAGE"));
				ticket.setPriority(rs.getInt("PRIORITY"));
				ticket.setTime(rs.getTimestamp("DATE").toString());
				
				if(rs.getInt("STATUS")==1){
					ticket.setStatus(true);
				}else{
					ticket.setStatus(false);
				}

				User user = new User();
				user.setName(rs.getString("FULL_NAME"));
				user.setId(rs.getLong("SENDER_ID"));

				Department department = new Department();
				department.setId(rs.getLong("DEPARTMENT_ID"));
				department.setName(rs.getString("DEPARTMENT_NAME"));

				ticket.setDepartment(department);
				ticket.setSender(user);

				tickets.add(ticket);
			}

		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			closeResultSet(rs);
			closePreparedStatement(pStmt);
			closeConnection(conn);
		}
		logger.debug("getPostedTickets is finished");
		return tickets;		
	}
	
	public void closeTicket(long ticketID){
		logger.debug("closeTicket is started. ticketID:"+ticketID);
		Connection con = null;
		PreparedStatement pst = null;
		StringBuilder query = new StringBuilder();

		try {
			query.append("UPDATE tickets ");
			query.append("SET tickets.STATUS=0 ");
			query.append("WHERE tickets.ID=? ");
			String queryString = query.toString();
			logger.debug("sql query created : " + queryString);

			con = getConnection();
			pst = con.prepareStatement(queryString);

			if (logger.isTraceEnabled()) {
				StringBuilder queryLog = new StringBuilder();
				queryLog.append("Query : ").append(queryString).append("\n");
				queryLog.append("Parameters :").append("\n");
				queryLog.append("ID :").append(ticketID).append("\n");
				logger.trace(queryLog.toString());
			}

			pst.setLong(1, ticketID);
			pst.executeUpdate();
		} catch (Exception e) {
			logger.error("addResponse error :" + e.getMessage());
		} finally {
			closePreparedStatement(pst);
			closeConnection(con);
		}
		logger.debug("closeTicket is finished");
	}
	
	
	public ArrayList<Ticket> getAllDepartmentTickets(int status, User user){
		logger.debug("getAllDepartmentTickets started. Status:"
						+status+" User ID:"+user.getId());
		Connection conn = null;
		PreparedStatement pStmt = null;
		ResultSet rs = null;
		StringBuilder query = new StringBuilder();

		ArrayList<Ticket> tickets = new ArrayList<>();

		try {
			query.append("SELECT tickets.*, users.FULL_NAME, departments.DEPARTMENT_NAME FROM tickets ");
			query.append("INNER JOIN users ON users.ID = tickets.SENDER_ID ");
			query.append("INNER JOIN departments ON tickets.DEPARTMENT_ID = departments.ID ");
			query.append("WHERE STATUS=? AND tickets.DEPARTMENT_ID=? ");
			String queryString = query.toString();
			logger.debug("Sql query Created : " + queryString);

			conn = getConnection();
			pStmt = conn.prepareStatement(queryString);
			
			if(logger.isTraceEnabled()){
				StringBuilder queryLog = new StringBuilder();
				queryLog.append("Query : ").append(queryString).append("\n");
				queryLog.append("Parameters : ").append("\n");
				queryLog.append("STATUS : ").append(status).append("\n");
				queryLog.append("DEPARTMENT_ID : ").append(user.getDepartment().getId()).append("\n");
				logger.trace(queryLog);
			}
			
			pStmt.setInt(1, status);
			pStmt.setLong(2, user.getDepartment().getId());
			rs = pStmt.executeQuery();

			while (rs.next()) {

				Ticket ticket = new Ticket();
				ticket.setId(rs.getLong("ID"));
				ticket.setTitle(rs.getString("TITLE"));
				ticket.setMessage(rs.getString("MESSAGE"));
				ticket.setPriority(rs.getInt("PRIORITY"));
				ticket.setTime(rs.getTimestamp("DATE").toString());
				
				if(rs.getInt("STATUS")==1){
					ticket.setStatus(true);
				}else{
					ticket.setStatus(false);
				}

				User ticketSender = new User();
				ticketSender.setName(rs.getString("FULL_NAME"));
				ticketSender.setId(rs.getLong("SENDER_ID"));

				Department department = new Department();
				department.setId(rs.getLong("DEPARTMENT_ID"));
				department.setName(rs.getString("DEPARTMENT_NAME"));

				ticket.setDepartment(department);
				ticket.setSender(ticketSender);

				tickets.add(ticket);
			}

		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			closeResultSet(rs);
			closePreparedStatement(pStmt);
			closeConnection(conn);
		}
		logger.debug("getAllTicket is finished");
		return tickets;
	}
	
	

	public ArrayList<Ticket> getAllTickets(int status) {
		logger.debug("getAllTickets started");
		Connection conn = null;
		PreparedStatement pStmt = null;
		ResultSet rs = null;
		StringBuilder query = new StringBuilder();

		ArrayList<Ticket> tickets = new ArrayList<>();

		try {
			query.append("SELECT tickets.*, users.FULL_NAME, departments.DEPARTMENT_NAME FROM tickets ");
			query.append("INNER JOIN users ON users.ID = tickets.SENDER_ID ");
			query.append("INNER JOIN departments ON tickets.DEPARTMENT_ID = departments.ID ");
			query.append("WHERE STATUS=? ");
			String queryString = query.toString();
			logger.debug("Sql query Created : " + queryString);

			conn = getConnection();
			pStmt = conn.prepareStatement(queryString);
			

			
			if(logger.isTraceEnabled()){
				StringBuilder queryLog = new StringBuilder();
				queryLog.append("Query : ").append(queryString).append("\n");
				queryLog.append("Parameters : ").append("\n");
				queryLog.append("STATUS : ").append(status).append("\n");
				logger.trace(queryLog);
			}
			
			pStmt.setInt(1, status);
			rs = pStmt.executeQuery();

			while (rs.next()) {

				Ticket ticket = new Ticket();
				ticket.setId(rs.getLong("ID"));
				ticket.setTitle(rs.getString("TITLE"));
				ticket.setMessage(rs.getString("MESSAGE"));
				ticket.setPriority(rs.getInt("PRIORITY"));
				ticket.setTime(rs.getTimestamp("DATE").toString());
				
				if(rs.getInt("STATUS")==1){
					ticket.setStatus(true);
				}else{
					ticket.setStatus(false);
				}

				User user = new User();
				user.setName(rs.getString("FULL_NAME"));
				user.setId(rs.getLong("SENDER_ID"));

				Department department = new Department();
				department.setId(rs.getLong("DEPARTMENT_ID"));
				department.setName(rs.getString("DEPARTMENT_NAME"));

				ticket.setDepartment(department);
				ticket.setSender(user);

				tickets.add(ticket);
			}

		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			closeResultSet(rs);
			closePreparedStatement(pStmt);
			closeConnection(conn);
		}
		logger.debug("getAllTicket is finished");
		return tickets;
	}

	public Ticket getTicketDetails(long ticketID) {
		logger.debug("getTicketDetails started. ticketID:" + ticketID);

		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		StringBuilder query = new StringBuilder();

		Ticket ticket = null;

		try {
			query.append("SELECT tickets.*, departments.DEPARTMENT_NAME, users.FULL_NAME, users.EMAIL FROM tickets ");
			query.append("INNER JOIN departments ON tickets.DEPARTMENT_ID=departments.ID ");
			query.append("INNER JOIN users ON users.id=tickets.SENDER_ID ");
			query.append("WHERE tickets.ID=?");
			String queryString = query.toString();
			logger.debug("sql query created : " + queryString);

			con = getConnection();

			pst = con.prepareStatement(queryString);

			if (logger.isTraceEnabled()) {
				StringBuilder queryLog = new StringBuilder();
				queryLog.append("Query : ").append(queryString).append("\n");
				queryLog.append("Parameters ").append("\n");
				queryLog.append("ID : ").append(ticketID).append("\n");
				logger.trace(queryLog.toString());
			}

			pst.setLong(1, ticketID);

			rs = pst.executeQuery();

			if (rs.next()) {
				ticket = new Ticket();
				ticket.setId(ticketID);
				ticket.setMessage(rs.getString("MESSAGE"));
				ticket.setPriority(rs.getInt("PRIORITY"));
				ticket.setTitle(rs.getString("TITLE"));
				ticket.setTime(rs.getTimestamp("DATE").toString());
				
				if(rs.getInt("STATUS")==1){
					ticket.setStatus(true);
				}else{
					ticket.setStatus(false);
				}

				User user = new User();
				user.setName(rs.getString("FULL_NAME"));
				user.setId(rs.getLong("SENDER_ID"));
				user.setEmail(rs.getString("EMAIL"));

				ticket.setSender(user);

				Department department = new Department();
				department.setId(rs.getLong("DEPARTMENT_ID"));
				department.setName(rs.getString("DEPARTMENT_NAME"));

				ticket.setDepartment(department);
			}

		} catch (Exception e) {
			logger.error("getTicketDetails error :" + e.getMessage());
		} finally {
			closeResultSet(rs);
			closePreparedStatement(pst);
			closeConnection(con);
		}
		logger.debug("getTicketDetails finished");

		return ticket;
	}

	public TicketResponse addResponse(TicketResponse response) {
		logger.debug("addResponse is started");
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;

		StringBuilder query = new StringBuilder();

		try {
			query.append("INSERT INTO ticket_responses ");
			query.append("(SENDER_ID,TICKET_ID,RESPONSE) ");
			query.append("VALUES(?,?,?) ");
			String queryString = query.toString();
			logger.debug("sql query created : " + queryString);

			con = getConnection();
			pst = con.prepareStatement(queryString, Statement.RETURN_GENERATED_KEYS);

			if (logger.isTraceEnabled()) {
				StringBuilder queryLog = new StringBuilder();
				queryLog.append("Query : ").append(queryString).append("\n");
				queryLog.append("Parameters :").append("\n");
				queryLog.append("SENDER_ID :").append(response.getSender().getId()).append("\n");
				queryLog.append("TICKET_ID :").append(response.getTicketID()).append("\n");
				queryLog.append("RESPONSE : ").append(response.getMessage()).append("\n");
				logger.trace(queryLog.toString());
			}

			pst.setLong(1, response.getSender().getId());
			pst.setLong(2, response.getTicketID());
			pst.setString(3, response.getMessage());

			pst.executeUpdate();

			rs = pst.getGeneratedKeys();
			if (rs.next()) {
				response.setId(rs.getLong(1));
			}

		} catch (Exception e) {
			logger.error("addResponse error :" + e.getMessage());
		} finally {
			closeResultSet(rs);
			closePreparedStatement(pst);
			closeConnection(con);
		}

		logger.debug("Add response finished");
		return response;
	}

	public ArrayList<TicketResponse> getAllResponses(long ticketID) {
		logger.debug("getAllResponses is started. TicketID:" + ticketID);
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;

		ArrayList<TicketResponse> ticketResponses = new ArrayList<>();
		StringBuilder query = new StringBuilder();

		try {
			query.append("SELECT ticket_responses.*, users.FULL_NAME, users.EMAIL FROM ticket_responses ");
			query.append("INNER JOIN users ON ticket_responses.SENDER_ID=users.ID ");
			query.append("WHERE ticket_responses.TICKET_ID=? ORDER BY ticket_responses.ID ASC ");

			String queryString = query.toString();
			logger.debug("sql query create : " + queryString);

			con = getConnection();

			pst = con.prepareStatement(queryString);

			if (logger.isTraceEnabled()) {
				StringBuilder queryLog = new StringBuilder();
				queryLog.append("Query : ").append(queryString).append("\n");
				queryLog.append("Paramters :").append("\n");
				queryLog.append("TICKET_ID : ").append(ticketID).append("\n");
				logger.trace(queryLog.toString());
			}

			pst.setLong(1, ticketID);
			rs = pst.executeQuery();

			while (rs.next()) {

				TicketResponse response = new TicketResponse();
				response.setDate(rs.getTimestamp("DATE").toString());
				response.setId(rs.getLong("ID"));
				response.setMessage(rs.getString("RESPONSE"));
				response.setTicketID(ticketID);

				User sender = new User();
				sender.setEmail(rs.getString("EMAIL"));
				sender.setName(rs.getString("FULL_NAME"));

				response.setSender(sender);

				ticketResponses.add(response);
			}

		} catch (Exception e) {
			logger.error("getAllResponses error :" + e.getMessage());

		} finally {
			closeResultSet(rs);
			closePreparedStatement(pst);
			closeConnection(con);
		}
		logger.debug("getAllResponses is finished");
		return ticketResponses;
	}

	public void deleteTicket(long ticketID) throws Exception {
		logger.debug("deleteTicket started. Param: ticketID=" + ticketID);

		Connection con = null;
		PreparedStatement pstResp = null;
		PreparedStatement pstTicket = null;
		StringBuilder queryDeleteResp = new StringBuilder();
		StringBuilder queryDeleteTicket = new StringBuilder();

		try {

			// first delete responses
			queryDeleteResp.append("DELETE FROM ticket_responses ");
			queryDeleteResp.append("WHERE TICKET_ID=?");
			String queryString = queryDeleteResp.toString();
			logger.debug("sql query created : " + queryString);

			con = getConnection();
			pstResp = con.prepareStatement(queryString);

			if (logger.isTraceEnabled()) {
				StringBuilder queryLog = new StringBuilder();
				queryLog.append("Query : ").append(queryString).append("\n");
				queryLog.append("Parameters : ").append("\n");
				queryLog.append("TICKET_ID : ").append(ticketID).append("\n");
				logger.trace(queryLog.toString());
			}
			pstResp.setLong(1, ticketID);

			pstResp.executeUpdate();

			// delete ticket
			queryDeleteTicket.append("DELETE FROM tickets ");
			queryDeleteTicket.append("WHERE ID=?");
			queryString = queryDeleteTicket.toString();
			logger.debug("sql query created :" + queryString);

			pstTicket = con.prepareStatement(queryString);

			if (logger.isTraceEnabled()) {
				StringBuilder queryLog = new StringBuilder();
				queryLog.append("Query : ").append(queryString).append("\n");
				queryLog.append("Parameters : ").append("\n");
				queryLog.append("ID : ").append(ticketID).append("\n");
				logger.trace(queryLog.toString());
			}

			pstTicket.setLong(1, ticketID);
			pstTicket.executeUpdate();
		} catch (Exception e) {
			logger.error("error:" + e.getMessage());
		} finally {
			closePreparedStatement(pstResp);
			closePreparedStatement(pstTicket);
			closeConnection(con);
		}
		logger.debug("deleteTicket is finished");
	}

	public void editTicket(Ticket newTicket) throws Exception {

		logger.debug("editTicket started.");

		Connection con = null;
		PreparedStatement pst = null;
		StringBuilder query = new StringBuilder();

		try {
			//TODO: duruma göre devamı gelicek, query bastan yazilmalı
			query.append("UPDATE FROM tickets ");
			query.append("SET column1=value1,column2=value2,...");
			query.append("WHERE TICKET_ID=?");
			String queryString = query.toString();
			logger.debug("sql query created : " + queryString);

			con = getConnection();
			pst = con.prepareStatement(queryString);
			
		} catch (Exception e) {
			logger.debug("error :" + e.getMessage());
		} finally {
			closePreparedStatement(pst);
			closeConnection(con);
		}
	}
}
