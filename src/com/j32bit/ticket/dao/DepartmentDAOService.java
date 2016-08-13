package com.j32bit.ticket.dao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.j32bit.ticket.bean.Department;


public class DepartmentDAOService extends ConnectionHelper{
	
	private static Logger logger = LogManager.getLogger();
	
	public void init(Properties prop) {
		logger.info("DepartmentDAOService service is initializing");
		super.init(prop);
		logger.info("DepartmentDAOService service is initialized");
	}
	
	public Department [] getAllDepartments(){
		logger.debug("getAllDepartments started");
		
		Connection con = null;
		PreparedStatement pst=null;
		ResultSet rs=null;
		
		List<Department> departments = new ArrayList<>();
		
		try {
			con = getConnection();
			
			String query = "SELECT * FROM departments";
			
			pst = con.prepareStatement(query);
			rs = pst.executeQuery();
			
			while(rs.next()){
				
				String depName = rs.getString("NAME");
				int depID = rs.getInt("ID");
				Department department = new Department(depName,depID);
				
				departments.add(department);			
			}
			
			logger.info("getAllDepartments dep#"+departments.size());
			
		} catch (Exception e) {
			logger.error("getAllDepartments error:");
			e.printStackTrace();
		}
		return departments.toArray(new Department[0]);
	}

}
