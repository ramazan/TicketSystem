//Date ve Ticket Sayısı(PK) eklendikten sonra komutlar düzenlenecek. 

package com.j32bit.ticket.dao.ticket;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.j32bit.ticket.bean.Priority;
import com.j32bit.ticket.bean.Ticket;
import com.j32bit.ticket.dao.ConnectionHelper;

public class TicketDAOService extends ConnectionHelper {
	private Logger logger = LogManager.getLogger(TicketDAOService.class);

	public void init(Properties prop) {
		logger.info("TicketDAOService service is initializing");
		super.init(prop);
		logger.info("TicketDAOService service is initialized");
	}

	public void storeTicket(Ticket ticket) throws Exception {// TODO loglama ve
																// kullanıma
																// dikkat
		logger.debug("storeTicket is started");
		Connection conn = null;
		PreparedStatement pStmt = null;
		StringBuilder query = new StringBuilder();
		StringBuilder querylog = new StringBuilder();
		try {
			query.append("INSERT INTO TICKETS ");
			query.append("(TITLE, MESSAGE) ");
			query.append("VALUES(?,?)");
			String queryString = query.toString();
			logger.debug("Sql query Created : " + queryString);

			conn = getConnection();

			pStmt = conn.prepareStatement(queryString);

			if (logger.isTraceEnabled()) {
				querylog.append("Query : ").append(queryString).append("\n");
				querylog.append("Parameters : ").append("\n");
				querylog.append("TITLE : ").append(ticket.getTitle()).append("\n");
				querylog.append("MESSAGE : ").append(ticket.getMessage()).append("\n");
				logger.trace(querylog.toString());
			}

			pStmt.setString(1, ticket.getTitle());
			pStmt.setString(2, ticket.getMessage());

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
			String query = "SELECT COUNT(id) FROM tickets";

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

				String title = rs.getString("title");
				String message = rs.getString("message");
				int id = rs.getInt("id");
				String department = rs.getString("department");
				String sender = rs.getString("sender");
				Date date = rs.getDate("date");
				ticketsArr[i] = new Ticket(id,null, sender, department, message,title,Priority.LOW);
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
