package com.j32bit.ticket.dao.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.j32bit.bean.Person;
import com.j32bit.ticket.bean.User;
import com.j32bit.ticket.dao.ConnectionHelper;

public class UserDAOService extends ConnectionHelper {
	
	private Logger logger = LogManager.getLogger(UserDAOService.class);

	public void init(Properties prop){
		super.init(prop);
	}

	public User getUser(String email) {

		logger.debug("getUser is started");
		User user = null;

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement pst = null;

		try {
			user = new User();
			String query = "SELECT * FROM USERS WHERE EMAIL=?";
			con = getConnection();
			pst = con.prepareStatement(query);
			pst.setString(1, email);
			rs = pst.executeQuery();
			while (rs.next()) {
				user.setName("NAME");
				user.setSurname("SURNAME");
				user.setCompany("COMPANY");
				user.setEmail("EMAIL");
				user.setPassword("PASSWORD");
	
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				closeResultSet(rs);
				closeStatement(pst);
				closeConnection(con);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		logger.debug("getUser is finished");
		return user;
	}
	
	
	public void storeUser(User user) {

		logger.debug("storeUser is started");
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		String query = "INSERT INTO USERS (NAME,SURNAME,COMPANY,EMAIL,PASSWORD) VALUES(?,?,?,?,?)";

		try {
			con = getConnection();
			pst = con.prepareStatement(query);
			pst.setString(1, user.getName());
			// System.out.println(person.getTC()+person.getName()+person.getPhone());
			pst.setString(2, user.getSurname());
			pst.setString(3, user.getCompany());
			pst.setString(4, user.getEmail());
			pst.setString(5, user.getPassword());
			pst.executeUpdate();
			// pst.executeQuery();
			closeResultSet(rs);
			closeStatement(pst);
			closeConnection(con);
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
		}
		logger.debug("storeUser is finished");
	}

	public void deleteUser(String email) {

		logger.debug("deleteUser is started");

		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		PreparedStatement pst = null;
		String query = "DELETE FROM USERS WHERE EMAIL=?";

		try {
			con = getConnection();
			pst = con.prepareStatement(query);
			pst.setString(1, email);
			pst.executeUpdate();

			closeResultSet(rs);
			closeStatement(st);
			closeConnection(con);
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
		}

		logger.debug("deleteUser is finished");
	}
	
	public User updateUser(User user) {

		logger.debug("updateUser is started");

		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		String query = "UPDATE USERS SET EMAIL=?, PASSWORD=? WHERE EMAIL=?";
		PreparedStatement pst = null;

		try {
			con = getConnection();
			pst = con.prepareStatement(query);

			pst.setString(1, user.getEmail());
			pst.setString(2, user.getPassword());
			pst.setString(3, user.getEmail());
			pst.executeUpdate();

			closeResultSet(rs);
			closeStatement(st);
			closeConnection(con);
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
		}

		logger.debug("updateUser is finished");
		
	}

	
}
