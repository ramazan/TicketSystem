package com.j32bit.ticket.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.j32bit.ticket.bean.User;
import com.j32bit.ticket.enums.Status;

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

	public Status addUser(User user) {
		logger.debug("addUser started");
		logger.info("addUser user :"+user);

		Connection con = null;
		PreparedStatement pst = null;

		Status result = Status.SUCCESS;

		if (getUser(user.getEmail())!=null) {
			result = Status.USER_EXIST;
		} else {
			try {
				String addUserQuery = "INSERT INTO users (FULL_NAME,EMAIL,PASSWORD,COMPANY_ID,DEPARTMENT_ID) values (?,?,?,?,?)";

				con = getConnection();
				pst = con.prepareStatement(addUserQuery);
				pst.setString(1, user.getName());
				pst.setString(2, user.getEmail());
				pst.setString(3, user.getPassword());
				pst.setInt(4, user.getCompanyID());
				pst.setInt(5, user.getDepartmentID());
				pst.executeUpdate(); // to insert, update,delete and return
										// nothings

				int roleSize = user.getUserRoles().length;
				for (int i = 0; i != roleSize; ++i) {
					String addRoleQuery = "INSERT INTO user_roles (EMAIL,ROLE) values (?,?)";
					pst = con.prepareStatement(addRoleQuery);
					pst.setString(1, user.getEmail());
					pst.setString(2, user.getUserRoles()[i]);
					pst.executeUpdate();
				}

			} catch (Exception e) {
				// ERROR STATUSU DEGISTIRILECEK
				logger.debug("addUser error:" + e.getMessage());
				e.printStackTrace();
			} finally {
				closePreparedStatement(pst);
				closeConnection(con);
				logger.debug("addUser completed");
			}
		}
		return result;
	}

	public User[] getAllUsers() {
		logger.debug("getAllUser started");
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet userRS = null;
		ResultSet rs = null;
		User user;

		ArrayList<User> users = new ArrayList<>();

		try {

			con = getConnection();
			String query = "SELECT * FROM users";

			pst = con.prepareStatement(query);
			userRS = pst.executeQuery();

			while (userRS.next()) {

				int userID = userRS.getInt("ID");
				String userName = userRS.getString("FULL_NAME");
				String userEmail = userRS.getString("EMAIL");
				String userPassword = userRS.getString("PASSWORD");
				int userCompanyID = userRS.getInt("COMPANY_ID");
				int userDepartmentID = userRS.getInt("DEPARTMENT_ID");

				// GET ROLE
				query = "SELECT ROLE FROM user_roles WHERE EMAIL=?";
				pst = con.prepareStatement(query.toString());
				pst.setString(1, userEmail);
				rs = pst.executeQuery();

				List<String> roles = new ArrayList<>();
				String[] userRolesArr;
				while (rs.next()) {
					roles.add(rs.getString("ROLE"));
				}
				userRolesArr = roles.toArray(new String[0]); // bu daha performanslı + test sonuclarina bakildi
				logger.info("getAllUser role #" + userRolesArr.length);

				user = new User(userID, userName, userEmail, userPassword, userCompanyID,userDepartmentID, userRolesArr);
				logger.info("getAllUser user:" + user);
				users.add(user);
			}
		} catch (Exception e) {
			logger.debug("getAllUser error occured");
			e.printStackTrace();
		} finally {
			closeResultSet(rs);
			closeResultSet(userRS);
			closePreparedStatement(pst);
			closeConnection(con);
		}
		logger.debug("getAllUser finished");
		return users.toArray(new User[users.size()]);
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

			while(rs.next()){
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
				String[] userRolesArr;
				while (rs.next()) {
					roles.add(rs.getString("ROLE"));
				}
				userRolesArr = roles.toArray(new String[roles.size()]);
	
				user = new User(userID, userName, userEmail, userPassword, userCompanyID, userDepartmentID,userRolesArr);
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
