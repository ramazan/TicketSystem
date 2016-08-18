package com.j32bit.ticket.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.j32bit.ticket.bean.Department;
import com.j32bit.ticket.bean.Ticket;
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

	public ArrayList<Ticket> getAllTickets() {
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
			String queryString = query.toString();
			logger.debug("Sql query Created : " + queryString);

			conn = getConnection();
			pStmt = conn.prepareStatement(queryString);

			rs = pStmt.executeQuery();

			while (rs.next()) {

				Ticket ticket = new Ticket();
				ticket.setId(rs.getLong("ID"));
				ticket.setTitle(rs.getString("TITLE"));
				ticket.setMessage(rs.getString("MESSAGE"));
				ticket.setPriority(rs.getInt("PRIORITY"));
				ticket.setStatus(true); // TODO : DB DEN AL
				ticket.setTime(rs.getTimestamp("DATE").toString());

				// logger.debug("TiME "+rs.getTimestamp("DATE"));

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
				ticket.setStatus(true); // TODO: db den boolean olarak alinacak
				ticket.setTitle(rs.getString("TITLE"));
				ticket.setTime(rs.getTimestamp("DATE").toString());

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
}
