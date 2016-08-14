package com.j32bit.ticket.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.j32bit.ticket.bean.User;

public class UserDAOService extends ConnectionHelper {

	private Logger logger = LogManager.getLogger(UserDAOService.class);

	public UserDAOService() {
		logger.debug("constructed");
	}

	public void init(Properties prop) {
		logger.debug("initialize started");
		super.init(prop);
		logger.debug("initialize finished");
	}

	public void addUser(User user) throws Exception {
		logger.debug("addUser started");

		Connection con = null;
		PreparedStatement pstAddUser = null;
		PreparedStatement pstAddRole = null;
		ResultSet rs = null;
		StringBuilder queryAddUser = new StringBuilder();
		StringBuilder queryAddRole = new StringBuilder();
		StringBuilder queryLog = new StringBuilder();
		long recordID = 0;

		String userEmail = user.getEmail();

		if (getUser(userEmail) != null) {
			throw new Exception("User Exist"); // TEST
		} else {
			try {
				queryAddUser.append("INSERT INTO users ");
				queryAddUser.append("(FULL_NAME,EMAIL,PASSWORD,COMPANY_ID,DEPARTMENT_ID)");
				queryAddUser.append("values (?,?,?,?,?)");
				String queryString = queryAddUser.toString();

				con = getConnection();

				pstAddUser = con.prepareStatement(queryString, Statement.RETURN_GENERATED_KEYS);

				pstAddUser.setString(1, user.getName());
				pstAddUser.setString(2, userEmail);
				pstAddUser.setString(3, user.getPassword());
				pstAddUser.setLong(4, user.getCompanyID());
				pstAddUser.setLong(5, user.getDepartmentID());
				pstAddUser.executeUpdate();

				if (logger.isTraceEnabled()) {
					queryLog.append("Query : ").append(queryString).append("\n");
					queryLog.append("Parameters : ").append("\n");
					queryLog.append("FULL_NAME : ").append(user.getName()).append("\n");
					queryLog.append("EMAIL : ").append(user.getEmail()).append("\n");
					queryLog.append("PASSWORD : ").append(user.getPassword()).append("\n");
					queryLog.append("COMPANY_ID : ").append(user.getCompanyID()).append("\n");
					queryLog.append("DEPARTMENT_ID : ").append(user.getDepartmentID()).append("\n");
					logger.trace(queryLog.toString());
				}

				rs = pstAddUser.getGeneratedKeys();
				if (rs.next()) {
					recordID = rs.getLong(1);   /// PATLIYOR ÜSTÜN KÖRÜ İNCELEDİM BULAMADIM 
					logger.debug("Record ID : "+recordID);
					user.setId(recordID);
				}

				queryAddRole.append("INSERT INTO user_roles ");
				queryAddRole.append("(EMAIL,ROLE) values (?,?)");
				queryString = queryAddRole.toString();

				pstAddRole = con.prepareStatement(queryString);

				List<String> userRoles = user.getUserRoles();
				for (String role : userRoles) {
					pstAddRole.setString(1, userEmail);
					pstAddRole.setString(2, role);
					pstAddRole.executeUpdate();

					if (logger.isTraceEnabled()) {
						queryLog = new StringBuilder();
						queryLog.append("Query : ").append(queryString).append("\n");
						queryLog.append("Parameters : ").append("\n");
						queryLog.append("EMAIL : ").append(userEmail).append("\n");
						queryLog.append("ROLE : ").append(role).append("\n");
						logger.trace(queryLog.toString());
					}
				}

			} catch (Exception e) {
				logger.debug("addUser error");
				e.printStackTrace();
			} finally {
				closePreparedStatement(pstAddRole);
				closePreparedStatement(pstAddUser);
				closeResultSet(rs);
				closeConnection(con);
			}
		}
		logger.debug("addUser completed");
	}

	public ArrayList<User> getAllUsers() {
		logger.debug("getAllUser started");

		Connection con = null;
		PreparedStatement pstUsers = null;
		PreparedStatement pstRoles = null;
		ResultSet rsUsers = null;
		ResultSet rsRoles = null;

		User user;
		ArrayList<User> users = new ArrayList<>();

		try {

			con = getConnection();
			String query = "SELECT * FROM users";

			pstUsers = con.prepareStatement(query);
			rsUsers = pstUsers.executeQuery();

			while (rsUsers.next()) {

				long userID = rsUsers.getLong("ID");
				String userName = rsUsers.getString("FULL_NAME");
				String userEmail = rsUsers.getString("EMAIL");
				String userPassword = rsUsers.getString("PASSWORD");
				long userCompanyID = rsUsers.getLong("COMPANY_ID");
				long userDepartmentID = rsUsers.getLong("DEPARTMENT_ID");

				// GET ROLE
				query = "SELECT ROLE FROM user_roles WHERE EMAIL=?";
				pstRoles = con.prepareStatement(query.toString());
				pstRoles.setString(1, userEmail);
				rsRoles = pstRoles.executeQuery();

				ArrayList<String> roles = new ArrayList<>();
				while (rsRoles.next()) {
					roles.add(rsRoles.getString("ROLE"));
				}

				user = new User(userID, userName, userEmail, userPassword, userCompanyID, userDepartmentID, roles);

				users.add(user);
			}
		} catch (Exception e) {
			logger.debug("getAllUser error occured");
			e.printStackTrace();
		} finally {
			closeResultSet(rsRoles);
			closeResultSet(rsUsers);
			closePreparedStatement(pstRoles);
			closePreparedStatement(pstUsers);
			closeConnection(con);
		}
		logger.debug("getAllUser finished");
		return users;
	}

	public User getUser(String userEmail) {
		logger.debug("getUser with email started:" + userEmail);

		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;

		User user = null;

		try {

			// GET USER
			con = getConnection();
			String query = "SELECT * FROM users WHERE EMAIL=?";
			pst = con.prepareStatement(query);
			pst.setString(1, userEmail);
			rs = pst.executeQuery();

			while (rs.next()) {
				int userID = rs.getInt("ID");
				String userName = rs.getString("FULL_NAME");
				String userPassword = rs.getString("PASSWORD");
				int userCompanyID = rs.getInt("COMPANY_ID");
				int userDepartmentID = rs.getInt("DEPARTMENT_ID");

				closeResultSet(rs);
				closePreparedStatement(pst);

				// GET ROLE
				query = "SELECT ROLE FROM user_roles WHERE EMAIL=?";
				pst = con.prepareStatement(query.toString());
				pst.setString(1, userEmail);
				rs = pst.executeQuery();

				List<String> roles = new ArrayList<>();
				while (rs.next()) {
					roles.add(rs.getString("ROLE"));
				}
				user = new User(userID, userName, userEmail, userPassword, userCompanyID, userDepartmentID, roles);
			}
		} catch (Exception e) {
			logger.debug("getUser error occured");
			e.printStackTrace();
		} finally {
			closeResultSet(rs);
			closePreparedStatement(pst);
			closeConnection(con);
		}
		logger.debug("getUser completed User:" + user);
		return user;
	}
}
