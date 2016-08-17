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
		logger.info("CompanyDAOService constructed");
	}

	public void init(Properties prop) {
		logger.info("CompanyDAOService initialize started");
		super.init(prop);
		logger.info("CompanyDAOService initialize finished");
	}

	public Company addCompany(Company company) {
		logger.debug("addCompany started");

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement pst = null;
		StringBuilder query = new StringBuilder();
		StringBuilder queryLog = new StringBuilder();
		long recordId = 0;

		checkSimilarCompanyRecord(company); // TODO : ?????? EXCEPTIONLARDAN
											// SONRA TEKRAR BAKILACAK

		try {

			query.append("INSERT INTO companies ");
			query.append("(COMPANY_NAME,ADDRESS,EMAIL,PHONE,FAX) ");
			query.append("VALES (?,?,?,?,?)");
			String queryString = query.toString();
			logger.debug("sql query created :" + queryString);

			con = getConnection();
			// auto incremenet index leri almak icin 2.parametre lazim
			pst = con.prepareStatement(queryString, Statement.RETURN_GENERATED_KEYS);

			if (logger.isTraceEnabled()) { // trace bas sonra calistir
				queryLog.append("Query : ").append(queryString).append("\n");
				queryLog.append("Parameters : ").append("\n");
				queryLog.append("Name : ").append(company.getName()).append("\n");
				queryLog.append("Address : ").append(company.getAddress()).append("\n");
				queryLog.append("Email : ").append(company.getEmail()).append("\n");
				queryLog.append("Phone : ").append(company.getPhone()).append("\n");
				queryLog.append("Fax : ").append(company.getFax()).append("\n");
				logger.trace(queryLog.toString());
			}

			pst.setString(1, company.getName());
			pst.setString(2, company.getAddress());
			pst.setString(3, company.getEmail());
			pst.setString(4, company.getPhone());
			pst.setString(5, company.getFax());

			pst.executeUpdate();

			rs = pst.getGeneratedKeys();
			if (rs.next()) {
				recordId = rs.getLong(1); // id icin uretilen AI keyler
				company.setId(recordId);
			}

		} catch (Exception e) {
			logger.error("addcompany error:" + e.getMessage());
		} finally {
			closeResultSet(rs);
			closePreparedStatement(pst);
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

		String query = "SELECT COMPANY_NAME FROM companies WHERE COMPANY_NAME=?";

		try {
			con = getConnection();
			pst = con.prepareStatement(query);
			pst.setString(1, company.getName());

			rs = pst.executeQuery();
			if (rs.next()) {
				throw new Exception("Similar record founds. Company Name :" + rs.getString("COMPANY_NAME"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeResultSet(rs);
			closePreparedStatement(pst);
			closeConnection(con);
		}
		logger.debug("checkSimilarCompanyRecord finished");
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
			logger.debug("sql query created : " + query);
			con = getConnection();
			pst = con.prepareStatement(query);
			rs = pst.executeQuery();

			while (rs.next()) {
				company = new Company();
				company.setId(rs.getLong("ID"));
				company.setName(rs.getString("COMPANY_NAME"));
				company.setEmail(rs.getString("EMAIL"));
				company.setPhone(rs.getString("PHONE"));
				company.setFax(rs.getString("FAX"));
				company.setAddress(rs.getString("ADDRESS"));
				companies.add(company);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			closeResultSet(rs);
			closePreparedStatement(pst);
			closeConnection(con);
		}
		logger.debug("getAllcompany finished. company#: " + companies.size());
		return companies;
	}

	public Company getCompany(long companyID) {
		logger.debug("getCompany started");

		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;

		Company company = null;

		String query = "SELECT * FROM companies WHERE ID=?";
		logger.debug("sql query created : " + query);

		try {
			con = getConnection();

			pst = con.prepareStatement(query);
			pst.setLong(1, companyID);

			if (logger.isTraceEnabled()) {
				StringBuilder queryLog = new StringBuilder();
				queryLog.append("Query : ").append(query).append("\n");
				queryLog.append("Parameters : ").append("\n");
				queryLog.append("ID : ").append(companyID).append("\n");
				logger.trace(queryLog.toString());
			}

			rs = pst.executeQuery();

			if (rs.next()) {
				company = new Company();
				company.setName(rs.getString("COMPANY_NAME"));
				company.setId(rs.getLong("ID"));
				company.setEmail(rs.getString("EMAIL"));
				company.setFax(rs.getString("FAX"));
				company.setPhone(rs.getString("PHONE"));
				company.setAddress(rs.getString("ADDRESS"));
			} else {
				throw new Exception("Company not found!!!");
			}
		} catch (Exception e) {
			logger.error("getCompany error : "+e.getMessage());
		} finally {
			closeResultSet(rs);
			closePreparedStatement(pst);
			closeConnection(con);
		}
		logger.debug("getCompany finished");
		return company;
	}
}
