//Date ve Ticket Sayısı(PK) eklendikten sonra komutlar düzenlenecek. 

package com.j32bit.ticket.dao.ticket;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.j32bit.ticket.bean.Ticket;
import com.j32bit.ticket.dao.ConnectionHelper;

public class TicketDAOService extends ConnectionHelper {
	private Logger logger = LogManager.getLogger(TicketDAOService.class);

	public void init(Properties prop) {
		logger.info("TicketDAOService service is initializing");
		super.init(prop);
		logger.info("TicketDAOService service is initialized");
	}

	public void storeTicket(Ticket ticket) throws Exception {//TODO loglama ve kullanıma dikkat
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

	public void deleteTicket() {

	}

	public void updateTicket(Ticket ticket) {

		// logger.debug("updateTicket is started");
		//
		// Connection con = null;
		// Statement st = null;
		// ResultSet rs = null;
		// String query = "UPDATE TICKETS SET TITLE=?, MESSAGE=? WHERE
		// NUMBER=?";
		// PreparedStatement pst = null;
		//
		// try {
		// con = getConnection();
		// pst = con.prepareStatement(query);
		//
		// pst.setString(1, ticket.getTitle());
		// pst.setString(2, ticket.getMessage());
		// // pst.setString(3, ticket.getNumber()); //TODO:PK ayarlandıktan
		// // sonra
		// pst.executeUpdate();
		//
		// closeResultSet(rs);
		// closePreparedStatement(pst);
		// closeConnection(con);
		// } catch (SQLException e) {
		// e.printStackTrace();
		// } finally {
		// }
		//
		// logger.debug("updateTicket is finished");

	}

}