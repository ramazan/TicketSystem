//Date ve Ticket Sayısı(PK) eklendikten sonra komutlar düzenlenecek. 

package com.j32bit.ticket.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.j32bit.ticket.bean.Ticket;
import com.j32bit.ticket.enums.Priority;

public class TicketDAOService extends ConnectionHelper {
	private Logger logger = LogManager.getLogger(TicketDAOService.class);

	public void init(Properties prop) {
		logger.info("TicketDAOService service is initializing");
		super.init(prop);
		logger.info("TicketDAOService service is initialized");
	}

	public void storeTicket(Ticket ticket) throws Exception {

		logger.debug("storeTicket is started");
		Connection conn = null;
		PreparedStatement pStmt = null;
		StringBuilder query = new StringBuilder();
		StringBuilder querylog = new StringBuilder();
		try {
			query.append("INSERT INTO tickets ");
			query.append("(sender, title,priority,department,message) ");
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
				querylog.append("SENDER : ").append(ticket.getFrom()).append("\n");
				querylog.append("PRIORITY : ").append(ticket.getPriority()).append("\n");
				querylog.append("DEPARTMENT : ").append(ticket.getDepartmentName()).append("\n");
				logger.trace(querylog.toString());
			}

			pStmt.setString(1, ticket.getFrom());
			pStmt.setString(2, ticket.getTitle());
			pStmt.setString(3, ticket.getPriority().toString());
			pStmt.setString(4, ticket.getDepartmentName());
			pStmt.setString(5, ticket.getMessage());

			pStmt.execute();
			closePreparedStatement(pStmt);
			closeConnection(conn);
		} catch (SQLException e) {
			logger.error(e.getMessage());
		} finally {
		}
		logger.debug("storeTicket is finished");
	}

	public Ticket[] getAllTickets() {

		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;

		Ticket[] ticketsArr = null;

		try {

			con = getConnection();
			String query = "SELECT COUNT(ID) FROM tickets";

			pst = con.prepareStatement(query);
			rs = pst.executeQuery();

			rs.next();
			int recordNumber = rs.getInt(1);

			ticketsArr = new Ticket[recordNumber];
			closePreparedStatement(pst);
			closeResultSet(rs);
			query = "SELECT * FROM tickets";
			pst = con.prepareStatement(query);
			rs = pst.executeQuery();

			int i = 0;
			while (rs.next()) {
				int id = rs.getInt("ID");
				Date date = rs.getDate("DATE");
				int senderID = rs.getInt("SENDER_ID");
				String title = rs.getString("TITLE");
				int status = rs.getInt("STATUS");
				int departmentID = rs.getInt("DEPARTMENT_ID");
				String message = rs.getString("MESSAGE");

				Priority priority = null; // TODO: BURASI DB DEN ALINIP
											// YAPILACAK
				ticketsArr[i] = new Ticket(id, date, "FROM", "DEPARTMENT", title, title, Priority.LOW, 5);
				++i;
			}
		} catch (Exception e) {
			logger.debug("getAllTickets error occured");
			e.printStackTrace();
		} finally {
			closeResultSet(rs);
			closePreparedStatement(pst);
			closeConnection(con);
		}
		return ticketsArr;
	}
}
