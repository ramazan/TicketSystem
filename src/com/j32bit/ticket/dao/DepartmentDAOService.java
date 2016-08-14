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

			con = getConnection();

			pst = con.prepareStatement(query);

			if (logger.isTraceEnabled()) {
				logger.trace(query);
			}
			rs = pst.executeQuery();

			while (rs.next()) {

				String depName = rs.getString("NAME");
				int depID = rs.getInt("ID");
				Department department = new Department(depName, depID);

				departments.add(department);
			}

			logger.debug("getAllDepartments completed. dep#" + departments.size());

		} catch (Exception e) {
			logger.error("getAllDepartments error:");
			e.printStackTrace();
		}
		return departments;
	}

}
