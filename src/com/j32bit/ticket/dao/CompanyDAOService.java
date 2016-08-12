package com.j32bit.ticket.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.j32bit.ticket.bean.Company;
import com.j32bit.ticket.bean.Result;
import com.j32bit.ticket.enums.Status;

public class CompanyDAOService extends ConnectionHelper {

	
	private Logger logger = LogManager.getLogger(CompanyDAOService.class);

	public CompanyDAOService() {
		logger.debug("COMPANY DAOSERVICE constructed");
	}

	public void init(Properties prop) {
		logger.debug("initialize started");
		super.init(prop);
		logger.debug("initialize finished");
	}

	public Result addCompany(Company company) {
		logger.debug("addCompany started");

		Connection con = null;
		PreparedStatement pst = null;

		Result result = new Result();
		
		int companyID = getCompanyID(company);
		
		if(companyID!=0){ // company varsa
			result.status = Status.COMPANY_EXIST;
			result.companyID = companyID;
		}else{
			try {
				String addcompanyQuery = "INSERT INTO companies (NAME,ADDRESS,EMAIL,PHONE,FAX) values (?,?,?,?,?)";
	
				con = getConnection();
				pst = con.prepareStatement(addcompanyQuery);
				pst.setString(1, company.getName());
				pst.setString(2, company.getAddress());
				pst.setString(3, company.getEmail());
				pst.setString(4, company.getPhone());
				pst.setString(5, company.getFax());
				pst.executeUpdate(); // to insert, update,delete and return nothings
	
				result.companyID = getCompanyID(company);
				result.status = Status.SUCCESS;
			} catch (Exception e) {
				// ERROR STATUSU DEGISTIRILECEK
				logger.debug("addcompany error:" + e.getMessage());
				e.printStackTrace();
			} finally {
				closePreparedStatement(pst);
				closeConnection(con);
				logger.debug("addcompany completed");
			}
		}
		return result;
	}
	
	/*
	 * @param company
	 * @return company yoksa 0 return eder
	 */
	public int getCompanyID(Company company){
		
		Connection con=null;
		PreparedStatement pst=null;
		ResultSet rs=null;
		
		int companyID=0;
		
		String query = "SELECT ID FROM companies WHERE NAME=?";
		
		try {
			con = getConnection();
			pst = con.prepareStatement(query);
			pst.setString(1, company.getName());
			
			rs = pst.executeQuery();
			
			while(rs.next()){
				companyID = rs.getInt("ID");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			closeResultSet(rs);
			closePreparedStatement(pst);
			closeConnection(con);			
		}
		
		return companyID;
	}

	public Company[] getAllcompanies() {
		logger.debug("getAllcompany started");
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet companyRS = null;
		Company company;

		ArrayList<Company> companyies = new ArrayList<>();

		try {

			con = getConnection();
			String query = "SELECT * FROM companies";

			pst = con.prepareStatement(query);
			companyRS = pst.executeQuery();

			while (companyRS.next()) {

				int companyID = companyRS.getInt("ID");
				String companyName = companyRS.getString("NAME");
				String companyEmail = companyRS.getString("EMAIL");
				String companyPhone = companyRS.getString("PHONE");
				String companyFax = companyRS.getString("FAX");
				String companyAddress = companyRS.getString("ADDRESS");

				company = new Company(companyID, companyName, companyEmail, companyPhone, companyFax,
							companyAddress);
				companyies.add(company);
			}
		} catch (Exception e) {
			logger.debug("getAllcompany error occured");
			e.printStackTrace();
		} finally {
			closeResultSet(companyRS);
			closePreparedStatement(pst);
			closeConnection(con);
		}
		logger.debug("getAllcompany finished");
		return companyies.toArray(new Company[0]);
	}
}
