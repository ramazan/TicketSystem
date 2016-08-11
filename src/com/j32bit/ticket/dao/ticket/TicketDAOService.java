//Date ve Ticket Sayısı(PK) eklendikten sonra komutlar düzenlenecek. 

package com.j32bit.ticket.dao.ticket;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.j32bit.ticket.bean.Priority;
import com.j32bit.ticket.bean.Ticket;
import com.j32bit.ticket.dao.ConnectionHelper;
//import com.sun.jmx.snmp.Timestamp;

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
				querylog.append("SENDER : ").append(ticket.getSender()).append("\n");
				querylog.append("PRIORITY : ").append(ticket.getPriority()).append("\n");
				querylog.append("DEPARTMENT : ").append(ticket.getDepartment()).append("\n");
				logger.trace(querylog.toString());
			}

			pStmt.setString(1, ticket.getSender());
			pStmt.setString(2, ticket.getTitle());
			pStmt.setString(3, ticket.getPriority().toString());
			pStmt.setString(4, ticket.getDepartment());
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

		Ticket[] ticketsArr=null;

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

				String title = rs.getString("TITLE");
				String message = rs.getString("MESSAGE");
				int id = rs.getInt("ID");
				String department = rs.getString("DEPARTMENT");
				String sender = rs.getString("FROM");
				Date date = rs.getDate("DATE");
				Priority priority=null; // TODO: BURASI DB DEN ALINIP YAPILACAK
				ticketsArr[i] = new Ticket(id,date, sender, department, message,title,Priority.LOW);
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
