package com.j32bit.ticket.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;

import javax.ws.rs.WebApplicationException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.j32bit.ticket.bean.Department;

public class DepartmentDAOService extends ConnectionHelper {

	private static Logger logger = LogManager.getLogger();

	public DepartmentDAOService() {
		logger.info("DepartmentDAOService constructed");
	}

	public void init(Properties prop) {
		logger.info("DepartmentDAOService service is initializing");
		super.init(prop);
		logger.info("DepartmentDAOService service is initialized");
	}

	public ArrayList<Department> getAllDepartments() {
		logger.debug("getAllDepartments started");

		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;

		ArrayList<Department> departments = new ArrayList<>();

		try {

			String query = "SELECT * FROM departments WHERE ID>0";
			logger.debug("sql query created : " + query);

			con = getConnection();

			pst = con.prepareStatement(query);

			rs = pst.executeQuery();

			while (rs.next()) {
				Department department = new Department();
				department.setId(rs.getInt("ID"));
				department.setName(rs.getString("DEPARTMENT_NAME"));

				departments.add(department);
			}

		} catch (Exception e) {
			logger.error("getAllDepartments error: " + e.getMessage());
		} finally {
			closeResultSet(rs);
			closePreparedStatement(pst);
			closeConnection(con);
		}
		logger.debug("getAllDepartments finished.");
		return departments;
	}

	public Department getDepartment(long departmentID) {
		logger.debug("getDepartment started");

		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;

		Department department = new Department();

		String query = "SELECT * FROM departments WHERE ID=?";
		logger.debug("sql query created : " + query);

		try {
			con = getConnection();

			pst = con.prepareStatement(query);
			if (logger.isTraceEnabled()) {
				StringBuilder queryLog = new StringBuilder();
				queryLog.append("Query : ").append(query).append("\n");
				queryLog.append("Parameters : ").append("\n");
				queryLog.append("ID : ").append(departmentID).append("\n");
				logger.trace(queryLog.toString());
			}

			pst.setLong(1, departmentID);
			rs = pst.executeQuery();

			if (rs.next()) {
				department.setName(rs.getString("DEPARTMENT_NAME"));
				department.setId(rs.getLong("ID"));
			} else {
				throw new Exception("Not found department");
			}
		} catch (Exception e) {
			// e.printStackTrace();
		} finally {
			closeResultSet(rs);
			closePreparedStatement(pst);
			closeConnection(con);
		}
		logger.debug("getDepartment finished");
		return department;
	}

	public void checkSimilarDepartmentRecord(Department department) throws Exception {
		logger.debug("checkSimilarDepartmentRecord is started");
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;

		String query = "SELECT DEPARTMENT_NAME FROM departments WHERE DEPARTMENT_NAME=?";

		try {
			con = getConnection();
			pst = con.prepareStatement(query);
			pst.setString(1, department.getName());

			rs = pst.executeQuery();
			if (rs.next()) {
				throw new WebApplicationException(409);
			}
		} catch (Exception e) {
			if (e instanceof WebApplicationException) {
				logger.error("checkSimilarDepartmentRecord similar company founded!");
				throw e;
			} else {
				logger.error("checkSimilarDepartmentRecord error:" + e.getMessage());
			}
		} finally {
			closeResultSet(rs);
			closePreparedStatement(pst);
			closeConnection(con);
			logger.debug("checkSimilarDepartmentRecord finished");
		}
	}

	public Department addDepartment(Department department) throws Exception {
		logger.debug("adddepartment started");

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement pst = null;
		StringBuilder query = new StringBuilder();

		checkSimilarDepartmentRecord(department);

		try {

			query.append("INSERT INTO departments ");
			query.append("(DEPARTMENT_NAME)");
			query.append("values (?)");
			String queryString = query.toString();
			logger.debug("sql query created :" + queryString);

			con = getConnection();
			pst = con.prepareStatement(queryString, Statement.RETURN_GENERATED_KEYS);

			if (logger.isTraceEnabled()) {
				StringBuilder queryLog = new StringBuilder();
				queryLog.append("Query : ").append(queryString).append("\n");
				queryLog.append("Parameters : ").append("\n");
				queryLog.append("Name : ").append(department.getName()).append("\n");
				logger.trace(queryLog.toString());
			}

			pst.setString(1, department.getName());

			pst.executeUpdate();

			rs = pst.getGeneratedKeys();
			if (rs.next()) {
				department.setId(rs.getLong(1));
			}
		} catch (Exception e) {
			logger.error("adddepartment error:" + e.getMessage());
		} finally {
			closeResultSet(rs);
			closePreparedStatement(pst);
			closeConnection(con);
		}
		logger.debug("adddepartment is finished");
		return department;
	}

	public void deleteDepartment(long ID) throws Exception {

		logger.debug("deleteDepartment started. Param: ticketID=" + ID);
		
		checkDepartmentUser(ID);

		// TODO: departmana ait kullanıcı ya da ticket kontrolü yapılacak....
	
		Connection con = null;
		PreparedStatement pstDepartment = null;
		StringBuilder queryDeleteDepartment = new StringBuilder();

		try {
			queryDeleteDepartment.append("DELETE FROM departments ");
			queryDeleteDepartment.append("WHERE ID=?");
			String queryString = queryDeleteDepartment.toString();
			logger.debug("sql query created : " + queryString);

			con = getConnection();
			pstDepartment = con.prepareStatement(queryString);

			if (logger.isTraceEnabled()) {
				StringBuilder queryLog = new StringBuilder();
				queryLog.append("Query : ").append(queryString).append("\n");
				queryLog.append("Parameters : ").append("\n");
				queryLog.append("Department_ID : ").append(ID).append("\n");
				logger.trace(queryLog.toString());
			}

			pstDepartment.setLong(1, ID);

			pstDepartment.executeUpdate();

		} catch (Exception e) {
			logger.error("error:" + e.getMessage());
		} finally {
			closePreparedStatement(pstDepartment);
			closeConnection(con);
		}
		logger.debug("deleteDepartment is finished");

	}

	public void checkDepartmentUser(long ID) throws Exception {
		logger.debug("checkDepartmentUser is started");
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;

		String query = "SELECT DISTINCT DEPARTMENT_ID FROM users WHERE DEPARTMENT_ID=?";

		try {
			con = getConnection();
			pst = con.prepareStatement(query);
			pst.setLong(1, ID);

			rs = pst.executeQuery();
			if (rs.next()) {
				throw new WebApplicationException(409);
			}
		} catch (Exception e) {
			if (e instanceof WebApplicationException) {
				logger.error("checkDepartmentUser error");
				throw e;
			} else {
				logger.error("checkDepartmentUser error:" + e.getMessage());
			}
		} finally {
			closeResultSet(rs);
			closePreparedStatement(pst);
			closeConnection(con);
			logger.debug("checkDepartmentUser finished");
		}
	}


}
