package com.j32bit.ticket.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.j32bit.ticket.bean.Department;

public class DepartmentDAOService extends ConnectionHelper {

	private static Logger logger = LogManager.getLogger();
	
	public DepartmentDAOService(){
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

			String query = "SELECT * FROM departments";
			logger.debug("sql query created : " + query);

			con = getConnection();

			pst = con.prepareStatement(query);

			if (logger.isTraceEnabled()) {
				logger.trace(query);
			}
			rs = pst.executeQuery();

			while (rs.next()) {

				String depName = rs.getString("DEPARTMENT_NAME");
				int depID = rs.getInt("ID");
				Department department = new Department(depName, depID);

				departments.add(department);
			}

			logger.debug("getAllDepartments completed. dep#" + departments.size());

		} catch (Exception e) {
			logger.error("getAllDepartments error: " + e.getMessage());
		}
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
			pst.setLong(1, departmentID);

			if (logger.isTraceEnabled()) {
				StringBuilder queryLog = new StringBuilder();
				queryLog.append("Query : ").append(query).append("\n");
				queryLog.append("Parameters : ").append("\n");
				queryLog.append("ID : ").append(departmentID).append("\n");
				logger.trace(queryLog.toString());
			}

			rs = pst.executeQuery();

			if (rs.next()) {
				department.setName(rs.getString("DEPARTMENT_NAME"));
				department.setId(departmentID);
			} else {
				department.setName("NO DEPARTMENT");
				department.setId(0);
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

	
	public Department addDepartment(Department department) {
		logger.debug("adddepartment started");

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement pst = null;
		StringBuilder query = new StringBuilder();
		StringBuilder queryLog = new StringBuilder();
		

		try {

			query.append("INSERT INTO departments ");
			query.append("(DEPARTMENT_NAME)");
			query.append("values (?)");
			String queryString = query.toString();
			logger.debug("sql query created :"+queryString);

			con = getConnection();
			pst = con.prepareStatement(queryString);

			pst.setString(1, department.getName());


			if (logger.isTraceEnabled()) { 
				queryLog.append("Query : ").append(queryString).append("\n");
				queryLog.append("Parameters : ").append("\n");
				queryLog.append("Name : ").append(department.getName()).append("\n");
				logger.trace(queryLog.toString());
			}

			pst.executeUpdate();

		} catch (Exception e) {
			logger.error("adddepartment error:" + e.getMessage());
		} finally {
			closeResultSet(rs);
			closePreparedStatement(pst);
			closeConnection(con);
		}
		logger.debug("adddepartment finished");
		return department;
	}
	
}
