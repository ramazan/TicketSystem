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

import com.j32bit.ticket.bean.Company;
import com.j32bit.ticket.bean.Department;
import com.j32bit.ticket.bean.User;
import com.j32bit.ticket.service.ServiceFacade;

public class UserDAOService extends ConnectionHelper {

	private Logger logger = LogManager.getLogger(UserDAOService.class);

	public UserDAOService() {
		logger.info("constructed");
	}

	public void init(Properties prop) {
		logger.info("initialize started");
		super.init(prop);
		logger.info("initialize finished");
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
				logger.debug("sql query created : " + queryString);

				con = getConnection();
				pstAddUser = con.prepareStatement(queryString, Statement.RETURN_GENERATED_KEYS);
				
				if (logger.isTraceEnabled()) {
					queryLog.append("Query : ").append(queryString).append("\n");
					queryLog.append("Parameters : ").append("\n");
					queryLog.append("FULL_NAME : ").append(user.getName()).append("\n");
					queryLog.append("EMAIL : ").append(user.getEmail()).append("\n");
					queryLog.append("PASSWORD : ").append(user.getPassword()).append("\n");
					queryLog.append("COMPANY_ID : ").append(user.getCompany().getId()).append("\n");
					queryLog.append("DEPARTMENT_ID : ").append(user.getDepartment().getId()).append("\n");
					logger.trace(queryLog.toString());
				}

				pstAddUser.setString(1, user.getName());
				pstAddUser.setString(2, userEmail);
				pstAddUser.setString(3, user.getPassword());
				pstAddUser.setLong(4, user.getCompany().getId());
				pstAddUser.setLong(5, user.getDepartment().getId());

				pstAddUser.executeUpdate();

				rs = pstAddUser.getGeneratedKeys();

				if (rs.next()) {
					recordID = rs.getLong(1); /// PATLIYOR ÜSTÜN KÖRÜ İNCELEDİM
												/// BULAMADIM
					logger.debug("Record ID : " + recordID);
					user.setId(recordID);
				}

				queryAddRole.append("INSERT INTO user_roles ");
				queryAddRole.append("(EMAIL,ROLE) values (?,?)");
				queryString = queryAddRole.toString();
				logger.debug("sql query created : " + queryString);

				pstAddRole = con.prepareStatement(queryString);

				List<String> userRoles = user.getUserRoles();
				for (String role : userRoles) {
					
					if (logger.isTraceEnabled()) {
						queryLog = new StringBuilder();
						queryLog.append("Query : ").append(queryString).append("\n");
						queryLog.append("Parameters : ").append("\n");
						queryLog.append("EMAIL : ").append(userEmail).append("\n");
						queryLog.append("ROLE : ").append(role).append("\n");
						logger.trace(queryLog.toString());
					}
					
					pstAddRole.setString(1, userEmail);
					pstAddRole.setString(2, role);

					pstAddRole.executeUpdate();
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
		ArrayList<User> userList = new ArrayList<>();

		try {

			con = getConnection();
			String query = "SELECT * FROM users";
			logger.debug("sql query created : " + query);

			pstUsers = con.prepareStatement(query);

			if (logger.isTraceEnabled()) {
				StringBuilder queryLog = new StringBuilder();
				queryLog.append("Query created : ").append(query).append("\n");
				logger.trace(queryLog.toString());
			}

			rsUsers = pstUsers.executeQuery();

			while (rsUsers.next()) {
				user = new User();
				user.setId(rsUsers.getLong("ID"));
				user.setEmail(rsUsers.getString("EMAIL"));
				user.setName(rsUsers.getString("FULL_NAME"));
				user.setPassword(rsUsers.getString("PASSWORD"));

				long departmentID = rsUsers.getLong("DEPARTMENT_ID");
				Department department = ServiceFacade.getInstance().getDepartment(departmentID);
				user.setDepartment(department);

				long companyID = rsUsers.getLong("COMPANY_ID");
				Company company = ServiceFacade.getInstance().getCompany(companyID);
				user.setCompany(company);

				// GET ROLE
				query = "SELECT ROLE FROM user_roles WHERE EMAIL=?";
				pstRoles = con.prepareStatement(query.toString());
				
				if (logger.isTraceEnabled()) {
					StringBuilder queryLog = new StringBuilder();
					queryLog.append("Query created : ").append(query).append("\n");
					queryLog.append("Parameters : \n");
					queryLog.append("EMAIL : ").append(user.getEmail()).append("\n");
					logger.trace(queryLog.toString());
				}
				pstRoles.setString(1, user.getEmail());

				rsRoles = pstRoles.executeQuery();

				ArrayList<String> userRoles = new ArrayList<>();
				while (rsRoles.next()) {
					userRoles.add(rsRoles.getString("ROLE"));
				}
				user.setUserRoles(userRoles);

				userList.add(user);
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
		logger.debug("getAllUser finished. Total#" + userList.size());
		return userList;
	}

	public User getUser(String userEmail) {
		logger.debug("getUser started.");

		Connection con = null;
		PreparedStatement pstUser = null;
		PreparedStatement pstRole = null;
		ResultSet rsUser = null;
		ResultSet rsRole = null;

		User user = null;

		try {

			// GET USER
			con = getConnection();
			String query = "SELECT * FROM users WHERE EMAIL=?";
			logger.debug("sql query created : " + query);

			pstUser = con.prepareStatement(query);
			
			if (logger.isTraceEnabled()) {
				StringBuilder queryLog = new StringBuilder();
				queryLog.append("Query created : ").append("query").append("\n");
				queryLog.append("Parameters : ").append("\n");
				queryLog.append("EMAIL : ").append(userEmail).append("\n");
				logger.trace(queryLog.toString());
			}
			
			pstUser.setString(1, userEmail);

			rsUser = pstUser.executeQuery();

			if (rsUser.next()) {
				user = new User();
				user.setId(rsUser.getLong("ID"));
				user.setName(rsUser.getString("FULL_NAME"));
				user.setPassword(rsUser.getString("PASSWORD"));
				user.setEmail(rsUser.getString("EMAIL"));

				long departmentID = rsUser.getLong("DEPARTMENT_ID");
				Department department = ServiceFacade.getInstance().getDepartment(departmentID);
				user.setDepartment(department);

				long companyID = rsUser.getLong("COMPANY_ID");
				Company company = ServiceFacade.getInstance().getCompany(companyID);
				user.setCompany(company);

				// TODO: company id ler in isimleri alinacak
				// long userCompanyID = rsUser.getLong("COMPANY_ID");
				// long userDepartmentID = rsUser.getLong("DEPARTMENT_ID");

				// GET ROLE
				query = "SELECT ROLE FROM user_roles WHERE EMAIL=?";
				logger.debug("sql query created "+query);
				pstRole = con.prepareStatement(query.toString());
				
				if (logger.isTraceEnabled()) {
					StringBuilder queryLog = new StringBuilder();
					queryLog.append("Query created : ").append("query").append("\n");
					queryLog.append("Parameters : ").append("\n");
					queryLog.append("EMAIL : ").append(userEmail).append("\n");
					logger.trace(queryLog.toString());
				}
				pstRole.setString(1, userEmail);

				rsRole = pstRole.executeQuery();

				ArrayList<String> roles = new ArrayList<>();
				while (rsRole.next()) {
					roles.add(rsRole.getString(1));
				}
				user.setUserRoles(roles);
			}
		} catch (Exception e) {
			logger.debug("getUser error occured");
			e.printStackTrace();
		} finally {
			closeResultSet(rsRole);
			closeResultSet(rsUser);
			closePreparedStatement(pstUser);
			closePreparedStatement(pstRole);
			closeConnection(con);
		}
		logger.debug("getUser completed.");
		return user;
	}
}
