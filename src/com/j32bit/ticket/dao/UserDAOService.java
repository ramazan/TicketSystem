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
		StringBuilder query = new StringBuilder();
		String queryRoles;


		User user;
		ArrayList<User> userList = new ArrayList<>();

		try {

			con = getConnection();
			query.append("SELECT users.*, companies.COMPANY_NAME,companies.EMAIL AS COMPANY_EMAIL,");
			query.append("companies.ADDRESS, companies.PHONE, companies.FAX,departments.DEPARTMENT_NAME ");
			query.append("FROM users INNER JOIN companies ON users.COMPANY_ID=companies.ID ");
			query.append("INNER JOIN departments ON users.DEPARTMENT_ID=departments.ID");

			String queryString = query.toString();
			logger.debug("sql query created : " + queryString);

			pstUsers = con.prepareStatement(queryString);

			rsUsers = pstUsers.executeQuery();

			while (rsUsers.next()) {
				user = new User();
				user.setId(rsUsers.getLong("ID"));
				user.setEmail(rsUsers.getString("EMAIL"));
				user.setName(rsUsers.getString("FULL_NAME"));
				user.setPassword(rsUsers.getString("PASSWORD"));

				Department department = new Department();
				department.setName(rsUsers.getString("DEPARTMENT_NAME"));
				department.setId(rsUsers.getLong("DEPARTMENT_ID"));

				user.setDepartment(department);

				Company company = new Company();
				company.setAddress(rsUsers.getString("ADDRESS"));
				company.setEmail(rsUsers.getString("COMPANY_EMAIL"));
				company.setFax(rsUsers.getString("FAX"));
				company.setId(rsUsers.getLong("COMPANY_ID"));
				company.setName(rsUsers.getString("COMPANY_NAME"));
				company.setPhone("PHONE");

				user.setCompany(company);

				
				// GET ROLE
				
				/* string builder kullanamadım cunku 
				*  buradaki durum farklı append dediğimde  her seferinde sonuna ekliyor 
				*  biraz çirkin duruyor fakat şimdilik bu durumda kalacak :)  
				*  git commit -m ":)"  --Ramazan 
				*/
				queryRoles = "SELECT ROLE FROM user_roles WHERE EMAIL='"+user.getEmail()+"';";
				pstRoles = con.prepareStatement(queryRoles.toString());
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
		StringBuilder query = new StringBuilder();

		User user = null;

		try {

			// GET USER
			con = getConnection();
			query.append("SELECT users.*, companies.COMPANY_NAME,companies.EMAIL AS COMPANY_EMAIL,");
			query.append("companies.ADDRESS, companies.PHONE, companies.FAX,departments.DEPARTMENT_NAME ");
			query.append("FROM users INNER JOIN companies ON users.COMPANY_ID=companies.ID ");
			query.append("INNER JOIN departments ON users.DEPARTMENT_ID=departments.ID WHERE users.EMAIL=?");

			String queryString = query.toString();
			logger.debug("sql query created : " + queryString);

			pstUser = con.prepareStatement(queryString);
			pstUser.setString(1, userEmail);

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
				user.setEmail(rsUser.getString("EMAIL"));
				user.setName(rsUser.getString("FULL_NAME"));
				user.setPassword(rsUser.getString("PASSWORD"));

				Department department = new Department();
				department.setName(rsUser.getString("DEPARTMENT_NAME"));
				department.setId(rsUser.getLong("DEPARTMENT_ID"));

				user.setDepartment(department);

				Company company = new Company();
				company.setAddress(rsUser.getString("ADDRESS"));
				company.setEmail(rsUser.getString("COMPANY_EMAIL"));
				company.setFax(rsUser.getString("FAX"));
				company.setId(rsUser.getLong("COMPANY_ID"));
				company.setName(rsUser.getString("COMPANY_NAME"));
				company.setPhone("PHONE");

				user.setCompany(company);

				// GET ROLE
				queryString = "SELECT ROLE FROM user_roles WHERE EMAIL=?";
				logger.debug("sql query created " + queryString);
				pstRole = con.prepareStatement(queryString);

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

	// OVERLOADING YAPIYORUZ
	public User getUser(long userID) {
	    logger.debug("getUser started.");

	    Connection con = null;
	    PreparedStatement pstUser = null;
	    PreparedStatement pstRole = null;
	    ResultSet rsUser = null;
	    ResultSet rsRole = null;
	    StringBuilder query = new StringBuilder();

	    User user = null;

	    try {

	      // GET USER
	      con = getConnection();
	      query.append("SELECT users.*, companies.COMPANY_NAME,companies.EMAIL AS COMPANY_EMAIL,");
	      query.append("companies.ADDRESS, companies.PHONE, companies.FAX,departments.DEPARTMENT_NAME ");
	      query.append("FROM users INNER JOIN companies ON users.COMPANY_ID=companies.ID ");
	      query.append("INNER JOIN departments ON users.DEPARTMENT_ID=departments.ID WHERE users.ID=?");

	      String queryString = query.toString();
	      logger.debug("sql query created : " + queryString);

	      pstUser = con.prepareStatement(queryString);
	      pstUser.setLong(1, userID);

	      rsUser = pstUser.executeQuery();

	      if (rsUser.next()) {
	        user = new User();
	        user.setId(rsUser.getLong("ID"));
	        user.setEmail(rsUser.getString("EMAIL"));
	        user.setName(rsUser.getString("FULL_NAME"));
	        user.setPassword(rsUser.getString("PASSWORD"));

	        Department department = new Department();
	        department.setName(rsUser.getString("DEPARTMENT_NAME"));
	        department.setId(rsUser.getLong("DEPARTMENT_ID"));

	        user.setDepartment(department);

	        Company company = new Company();
	        company.setAddress(rsUser.getString("ADDRESS"));
	        company.setEmail(rsUser.getString("COMPANY_EMAIL"));
	        company.setFax(rsUser.getString("FAX"));
	        company.setId(rsUser.getLong("COMPANY_ID"));
	        company.setName(rsUser.getString("COMPANY_NAME"));
	        company.setPhone("PHONE");

	        user.setCompany(company);

	        // GET ROLE
	        queryString = "SELECT ROLE FROM user_roles WHERE EMAIL=?";
	        logger.debug("sql query created " + queryString);
	        pstRole = con.prepareStatement(queryString);

	        pstRole.setString(1, user.getEmail());

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
	
	
	
	public void updateProfile(String password, String email) throws Exception {

		logger.debug("updateUser started");

		Connection con = null;
		PreparedStatement pstUpdateUser = null;
		ResultSet rs = null;
		StringBuilder queryUpdateUser = new StringBuilder();
		StringBuilder queryLog = new StringBuilder();

		try {
			queryUpdateUser.append("UPDATE users SET PASSWORD=? WHERE EMAIL=?");
			String queryString = queryUpdateUser.toString();
			logger.debug("sql query created : " + queryString);

			con = getConnection();

			pstUpdateUser = con.prepareStatement(queryString);

			if (logger.isTraceEnabled()) {
				queryLog.append("Query : ").append(queryString).append("\n");
				queryLog.append("Parameters : ").append("\n");
				queryLog.append("PASSWORD : ").append(password).append("\n");
				queryLog.append("EMAIL : ").append(email).append("\n");
				logger.trace(queryLog.toString());
			}

			pstUpdateUser.setString(1, password);
			pstUpdateUser.setString(2, email);

			pstUpdateUser.executeUpdate();

		} catch (Exception e) {
			logger.debug("UpdateUser error" + e.getMessage());
		} finally {
			closePreparedStatement(pstUpdateUser);
			closeResultSet(rs);
			closeConnection(con);
		}

		logger.debug("UpdateUser completed");
	}

}
