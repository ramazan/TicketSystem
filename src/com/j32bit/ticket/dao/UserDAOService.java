package com.j32bit.ticket.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.ws.rs.WebApplicationException;

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

	private void storeUserRoles(User user) throws Exception {
		logger.debug("storeUserRoles is started");
		Connection con = null;
		PreparedStatement pStmt = null;
		StringBuilder query = new StringBuilder();
		String queryString = null;

		try {
			con = getConnection();
			query.append("INSERT INTO user_roles ");
			query.append("(EMAIL,ROLE) values (?,?)");
			queryString = query.toString();
			logger.debug("sql query created : " + queryString);

			pStmt = con.prepareStatement(queryString);

			List<String> userRoles = user.getUserRoles();
			for (String role : userRoles) {
				if (logger.isTraceEnabled()) {
					StringBuilder queryLog = new StringBuilder();
					queryLog.append("Query : ").append(queryString)
							.append("\n");
					queryLog.append("Parameters : ").append("\n");
					queryLog.append("EMAIL : ").append(user.getEmail())
							.append("\n");
					queryLog.append("ROLE : ").append(role).append("\n");
					logger.trace(queryLog.toString());
				}
				pStmt.setString(1, user.getEmail());
				pStmt.setString(2, role);

				pStmt.executeUpdate();
			}

		} catch (Exception e) {
			logger.error("storeUserRole: " + e.getMessage());
		} finally {
			closePreparedStatement(pStmt);
			closeConnection(con);
		}
		logger.debug("storeUserRoles is finished");
	}

	public void addUser(User user) throws Exception {
		logger.debug("addUser started");
		Connection con = null;
		PreparedStatement pstAddUser = null;
		ResultSet rs = null;
		StringBuilder query = new StringBuilder();
		StringBuilder queryLog = new StringBuilder();
		long recordID = 0;

		String userEmail = user.getEmail();

		if (getUser(userEmail) != null) {
			throw new WebApplicationException(409);
		} else {
			try {
				query.append("INSERT INTO users ");
				query.append("(FULL_NAME,EMAIL,PASSWORD,COMPANY_ID,DEPARTMENT_ID)");
				query.append("values (?,?,?,?,?)");
				String queryString = query.toString();
				logger.debug("sql query created : " + queryString);

				con = getConnection();
				pstAddUser = con.prepareStatement(queryString,
						Statement.RETURN_GENERATED_KEYS);

				if (logger.isTraceEnabled()) {
					queryLog.append("Query : ").append(queryString)
							.append("\n");
					queryLog.append("Parameters : ").append("\n");
					queryLog.append("FULL_NAME : ").append(user.getName())
							.append("\n");
					queryLog.append("EMAIL : ").append(user.getEmail())
							.append("\n");
					queryLog.append("PASSWORD : ").append(user.getPassword())
							.append("\n");
					queryLog.append("COMPANY_ID : ")
							.append(user.getCompany().getId()).append("\n");
					queryLog.append("DEPARTMENT_ID : ")
							.append(user.getDepartment().getId()).append("\n");
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
					recordID = rs.getLong(1);
					logger.debug("Record ID : " + recordID);
					user.setId(recordID);
				}
				storeUserRoles(user);
			} catch (Exception e) {
				logger.debug("addUser error");
				e.printStackTrace();
			} finally {
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
		ResultSet rsUsers = null;
		StringBuilder query = new StringBuilder();

		User user = null;
		ArrayList<User> userList = new ArrayList<>();

		try {
			con = getConnection();
			query.append("SELECT users.*, user_roles.ROLE, ");
			query.append("companies.COMPANY_NAME, departments.DEPARTMENT_NAME ");
			query.append("FROM users INNER JOIN companies ON users.COMPANY_ID=companies.ID ");
			query.append("INNER JOIN departments ON users.DEPARTMENT_ID=departments.ID ");
			query.append("INNER JOIN user_roles ON users.EMAIL=user_roles.EMAIL");
			String queryString = query.toString();
			logger.debug("sql query created : " + queryString);

			pstUsers = con.prepareStatement(queryString);

			rsUsers = pstUsers.executeQuery();

			long visitedID = -1;// -1 idye sahip kimse olamaz
			while (rsUsers.next()) {
				// yeni kullanici geldi ilk bilgileri oku
				// eski kullanici ise sadece rollerini oku
				if (visitedID != rsUsers.getLong("ID")) {
					user = new User();
					userList.add(user); // referansını listeye ekle
					visitedID = rsUsers.getLong("ID");
					user.setId(rsUsers.getLong("ID"));
					user.setEmail(rsUsers.getString("EMAIL"));
					user.setName(rsUsers.getString("FULL_NAME"));
					user.setPassword(rsUsers.getString("PASSWORD"));

					Department department = new Department();
					department.setName(rsUsers.getString("DEPARTMENT_NAME"));
					department.setId(rsUsers.getLong("DEPARTMENT_ID"));

					user.setDepartment(department);

					Company company = new Company();
					company.setId(rsUsers.getLong("COMPANY_ID"));
					company.setName(rsUsers.getString("COMPANY_NAME"));

					user.setCompany(company);

					user.getUserRoles().add(rsUsers.getString("ROLE"));
				} else { // geri kalan rolleri oku
					user.getUserRoles().add(rsUsers.getString("ROLE"));
				}
			}
		} catch (Exception e) {
			logger.debug("getAllUser error occured");
			e.printStackTrace();
		} finally {
			closeResultSet(rsUsers);
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
			query.append("SELECT users.*, companies.COMPANY_NAME, departments.DEPARTMENT_NAME ");
			query.append("FROM users INNER JOIN companies ON users.COMPANY_ID=companies.ID ");
			query.append("INNER JOIN departments ON users.DEPARTMENT_ID=departments.ID WHERE users.EMAIL=?");

			String queryString = query.toString();
			logger.debug("sql query created : " + queryString);

			pstUser = con.prepareStatement(queryString);
			pstUser.setString(1, userEmail);

			if (logger.isTraceEnabled()) {
				StringBuilder queryLog = new StringBuilder();
				queryLog.append("Query created : ").append("query")
						.append("\n");
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
				company.setId(rsUser.getLong("COMPANY_ID"));
				company.setName(rsUser.getString("COMPANY_NAME"));

				user.setCompany(company);

				// GET ROLE
				queryString = "SELECT ROLE FROM user_roles WHERE EMAIL=?";
				logger.debug("sql query created " + queryString);
				pstRole = con.prepareStatement(queryString);

				if (logger.isTraceEnabled()) {
					StringBuilder queryLog = new StringBuilder();
					queryLog.append("Query created : ").append("query")
							.append("\n");
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
		logger.debug("getUser with ID  started.");

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
			query.append("SELECT users.*, companies.COMPANY_NAME, departments.DEPARTMENT_NAME ");
			query.append("FROM users INNER JOIN companies ON users.COMPANY_ID=companies.ID ");
			query.append("INNER JOIN departments ON users.DEPARTMENT_ID=departments.ID WHERE users.ID=?");

			String queryString = query.toString();
			logger.debug("getUser with ID  sql query created : " + queryString);

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
				company.setId(rsUser.getLong("COMPANY_ID"));
				company.setName(rsUser.getString("COMPANY_NAME"));

				user.setCompany(company);

				// GET ROLE
				queryString = "SELECT ROLE FROM user_roles WHERE EMAIL=?";
				logger.debug("getUser with ID  sql query created "
						+ queryString);
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
			logger.debug("getUser with ID  error occured");
			e.printStackTrace();
		} finally {
			closeResultSet(rsRole);
			closeResultSet(rsUser);
			closePreparedStatement(pstUser);
			closePreparedStatement(pstRole);
			closeConnection(con);
		}
		logger.debug("getUser with ID completed.");
		return user;
	}

	public void updateProfile(String password, String email) throws Exception {

		logger.debug("updateUser started");

		Connection con = null;
		PreparedStatement pstUpdateUser = null;
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
			closeConnection(con);
		}

		logger.debug("Update profile completed");
	}

	public void deleteUser(User user) {

		logger.debug("deleteUser started. Param: userID=" + user.getId()
				+ "   userEmail:" + user.getEmail());

		Connection con = null;
		PreparedStatement pstRoles = null;
		PreparedStatement pstUser = null;
		StringBuilder queryDeleteRole = new StringBuilder();
		StringBuilder queryDeleteUser = new StringBuilder();

		try {

			queryDeleteRole.append("DELETE FROM user_roles ");
			queryDeleteRole.append("WHERE EMAIL=?");
			String queryString = queryDeleteRole.toString();
			logger.debug("sql query created : " + queryString);

			con = getConnection();
			pstRoles = con.prepareStatement(queryString);

			if (logger.isTraceEnabled()) {
				StringBuilder queryLog = new StringBuilder();
				queryLog.append("Query : ").append(queryString).append("\n");
				queryLog.append("Parameters : ").append("\n");
				queryLog.append("EMAIL : ").append(user.getEmail())
						.append("\n");
				logger.trace(queryLog.toString());
			}

			pstRoles.setString(1, user.getEmail());

			pstRoles.executeUpdate();

			queryDeleteUser.append("DELETE FROM users ");
			queryDeleteUser.append("WHERE ID=?");
			queryString = queryDeleteUser.toString();
			logger.debug("sql query created :" + queryString);

			pstUser = con.prepareStatement(queryString);

			if (logger.isTraceEnabled()) {
				StringBuilder queryLog = new StringBuilder();
				queryLog.append("Query : ").append(queryString).append("\n");
				queryLog.append("Parameters : ").append("\n");
				queryLog.append("ID : ").append(user.getId()).append("\n");
				logger.trace(queryLog.toString());
			}

			pstUser.setLong(1, user.getId());
			pstUser.executeUpdate();
		} catch (Exception e) {
			logger.error("error:" + e.getMessage());
		} finally {
			closePreparedStatement(pstRoles);
			closePreparedStatement(pstUser);
			closeConnection(con);
		}
		logger.debug("deleteUser is finished");
	}

	public void updateUserData(User user) throws Exception {
		
		//TODO  daha iyi bir yöntem ile iyileştirilmeli.
		
		logger.debug("updateUserData started");

		Connection con = null;
		PreparedStatement pstUpdateUser = null;
		PreparedStatement pstUpdateRole = null;
		PreparedStatement pstDeleteRole = null;
		StringBuilder queryUpdateUser = new StringBuilder();
		StringBuilder queryUpdateRole = new StringBuilder();
		StringBuilder queryDeleteRole = new StringBuilder();

		StringBuilder queryLog = new StringBuilder();
		queryLog.append("Parameters : ").append("\n");
		queryLog.append("ID : ").append(user.getId()).append("\n");
		queryLog.append("FULL_NAME : ").append(user.getName()).append("\n");
		queryLog.append("EMAIL : ").append(user.getEmail()).append("\n");
		queryLog.append("PASSWORD : ").append(user.getPassword()).append("\n");
		queryLog.append("COMPANY_ID : ").append(user.getCompany().getId())
				.append("\n");
		queryLog.append("DEPARTMENT_ID : ")
				.append(user.getDepartment().getId()).append("\n");
		logger.debug(queryLog.toString());

		try {
			//yeni bilgileri set et
			queryUpdateUser.append("UPDATE users SET ");
			queryUpdateUser
					.append("PASSWORD=? , EMAIL=? , FULL_NAME=? , COMPANY_ID=? , DEPARTMENT_ID=? ");
			queryUpdateUser.append(" WHERE ID=? ;");
			String queryString = queryUpdateUser.toString();
			logger.debug("sql query created : " + queryString);

			con = getConnection();
			pstUpdateUser = con.prepareStatement(queryString);

			pstUpdateUser.setString(1, user.getPassword());
			pstUpdateUser.setString(2, user.getEmail());
			pstUpdateUser.setString(3, user.getName());
			pstUpdateUser.setLong(4, user.getCompany().getId());
			pstUpdateUser.setLong(5, user.getDepartment().getId());
			pstUpdateUser.setLong(6, user.getId());

			pstUpdateUser.executeUpdate();
			
			//eski rolleri sil
			queryDeleteRole.append("DELETE FROM user_roles WHERE EMAIL=? ; ");
			queryString = queryDeleteRole.toString();

			pstDeleteRole = con.prepareStatement(queryString);

			pstDeleteRole.setString(1, user.getEmail());

			pstDeleteRole.executeUpdate();
			
			//yeni rolleri set et
			queryUpdateRole.append("INSERT INTO user_roles ");
			queryUpdateRole.append("(EMAIL,ROLE) values (?,?)");
			queryString = queryUpdateRole.toString();

			pstUpdateRole = con.prepareStatement(queryString);

			List<String> userRoles = user.getUserRoles();

			for (String role : userRoles) {
				pstUpdateRole.setString(1, user.getEmail());
				pstUpdateRole.setString(2, role);
				pstUpdateRole.executeUpdate();
			}
		} catch (Exception e) {
			logger.debug("updateUserData error");
			e.printStackTrace();
		} finally {
			closePreparedStatement(pstUpdateUser);
			closePreparedStatement(pstDeleteRole);
			closePreparedStatement(pstUpdateRole);
			closeConnection(con);
		}
		logger.debug("updateUserData completed");
	}
}
