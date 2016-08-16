package com.j32bit.ticket.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.j32bit.ticket.bean.Ticket;

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

	public ArrayList<Ticket> getAllTickets() {
		logger.debug("getAllTickets started");
		/*
		 * Connection con = null; PreparedStatement pst = null; ResultSet rs =
		 * null;
		 */

		ArrayList<Ticket> tickets = new ArrayList<>();
		/*
		 * try { String query = "SELECT * FROM tickets";
		 * logger.debug("sql query created : "+query);
		 * 
		 * con = getConnection();
		 * 
		 * pst = con.prepareStatement(query); rs = pst.executeQuery();
		 * 
		 * while (rs.next()) { long id = rs.getLong("ID"); Date date =
		 * rs.getDate("DATE"); int senderID = rs.getInt("SENDER_ID"); String
		 * title = rs.getString("TITLE"); int status = rs.getInt("STATUS"); int
		 * departmentID = rs.getInt("DEPARTMENT_ID"); String message =
		 * rs.getString("MESSAGE");
		 * 
		 * Priority priority = null; // TODO: BURASI DB DEN ALINACAK Ticket
		 * ticket = new Ticket(); ticket.setId(id); } } catch (Exception e) {
		 * logger.debug("getAllTickets error occured"); e.printStackTrace(); }
		 * finally { closeResultSet(rs); closePreparedStatement(pst);
		 * closeConnection(con); }
		 */
		return tickets;
	}
}
