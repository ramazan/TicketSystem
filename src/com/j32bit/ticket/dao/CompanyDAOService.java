package com.j32bit.ticket.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.j32bit.ticket.bean.Company;

public class CompanyDAOService extends ConnectionHelper {

	private Logger logger = LogManager.getLogger(CompanyDAOService.class);

	public CompanyDAOService() {
		logger.debug("COMPANY DAOSERVICE constructed");
	}

	public void init(Properties prop) {
		logger.debug("CompanyDAOService initialize started");
		super.init(prop);
		logger.debug("CompanyDAOService initialize finished");
	}

	public Company addCompany(Company company) {
		logger.debug("addCompany started");

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement pst = null;
		long recordId = 0;
		checkSimilarCompanyRecord(company);
		try {
			String addcompanyQuery = "INSERT INTO companies (NAME,ADDRESS,EMAIL,PHONE,FAX) values (?,?,?,?,?)";

			con = getConnection();
			pst = con.prepareStatement(addcompanyQuery, Statement.RETURN_GENERATED_KEYS);
			pst.setString(1, company.getName());
			pst.setString(2, company.getAddress());
			pst.setString(3, company.getEmail());
			pst.setString(4, company.getPhone());
			pst.setString(5, company.getFax());
			pst.executeUpdate();

			rs = pst.getGeneratedKeys();
			if (rs.next()) {
				recordId = rs.getLong(1);
				company.setId(recordId);
			}

		} catch (Exception e) {
			logger.error("addcompany error:" + e.getMessage());
		} finally {
			closePreparedStatement(pst);
			closeResultSet(rs);
			closeConnection(con);
		}
		logger.debug("addCompany finished ID : " + recordId);
		return company;
	}

	private void checkSimilarCompanyRecord(Company company) {
		logger.debug("checkSimilarCompanyRecord started");
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;

		String query = "SELECT NAME FROM companies WHERE NAME like %?%";

		try {
			con = getConnection();
			pst = con.prepareStatement(query);
			pst.setString(1, company.getName());
			// TODO dao methodlarınız KAPATILAN storeTicket METHODU GİBİ OLMALI
			// !
			rs = pst.executeQuery();
			while (rs.next()) {
				throw new Exception("Similar record founds. Company Name :" + rs.getString("NAME"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeResultSet(rs);
			closePreparedStatement(pst);
			closeConnection(con);
		}
		logger.debug("checkSimilarCompanyRecord started");
	}

	public ArrayList<Company> getAllcompanies() {
		logger.debug("getAllcompany started");
		ArrayList<Company> companies = new ArrayList<Company>();
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		Company company;
		
		try {
			
			String query = "SELECT * FROM companies";
			
			con = getConnection();
			pst = con.prepareStatement(query);
			rs = pst.executeQuery();

			while (rs.next()) {
				company = new Company( rs.getLong("ID"), rs.getString("NAME"), rs.getString("EMAIL"), rs.getString("PHONE"), rs.getString("FAX"), rs.getString("ADDRESS"));
				companies.add(company);//TODO beanlerin setter ları kullanılır ise hata okunaklı bir kod olur. 
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			closeResultSet(rs);
			closePreparedStatement(pst);
			closeConnection(con);
		}
		logger.debug("getAllcompany finished. List size : "+companies.size());
		return companies;
	}
}
