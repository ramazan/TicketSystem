//Date ve Ticket Sayısı(PK) eklendikten sonra komutlar düzenlenecek. 

package com.j32bit.ticket.dao.ticket;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.j32bit.ticket.bean.Ticket;
import com.j32bit.ticket.dao.ConnectionHelper;

public class TicketDAOService extends ConnectionHelper {
	private Logger logger = LogManager.getLogger(TicketDAOService.class);

	public void init(Properties prop) {
		super.init(prop);
	}

	public void getTicket() {

	}

	public void storeTicket(Ticket ticket) {

		logger.debug("storeTicket is started");
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		String query = "INSERT INTO TICKETS (TITLE, MESSAGE) VALUES(?,?)";

		try {
			con = getConnection();
			pst = con.prepareStatement(query);
			pst.setString(1, ticket.getTitle());
			pst.setString(2, ticket.getMessage());
			pst.executeUpdate();
			closeResultSet(rs);
			closePreparedStatement(pst);
			closeConnection(con);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
		}
		logger.debug("storeTicket is finished");
	}

	public void deleteTicket() {

	}

	public void updateTicket(Ticket ticket) {

		logger.debug("updateTicket is started");

		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		String query = "UPDATE TICKETS SET TITLE=?, MESSAGE=? WHERE NUMBER=?";
		PreparedStatement pst = null;

		try {
			con = getConnection();
			pst = con.prepareStatement(query);

			pst.setString(1, ticket.getTitle());
			pst.setString(2, ticket.getMessage());
			// pst.setString(3, ticket.getNumber()); //TODO:PK ayarlandıktan
			// sonra
			pst.executeUpdate();

			closeResultSet(rs);
			closePreparedStatement(pst);
			closeConnection(con);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
		}

		logger.debug("updateTicket is finished");

	}

}
