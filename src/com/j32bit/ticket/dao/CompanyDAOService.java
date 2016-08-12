package com.j32bit.ticket.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.j32bit.ticket.bean.Company;
import com.j32bit.ticket.enums.Error;

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

	public Error addCompany(Company company) {
		logger.debug("addCompany started");

		Connection con = null;
		PreparedStatement pst = null;

		Error result = Error.SUCCESS;

		// TODO : company check edilecek, ayni kisi eklenmesine karsin

		try {
			String addcompanyQuery = "INSERT INTO companies (NAME,ADRESS,MAIL,PHONE,FAX) values (?,?,?,?,?)";

			con = getConnection();
			pst = con.prepareStatement(addcompanyQuery);
			pst.setString(1, company.getName());
			pst.setString(2, company.getAddress());
			pst.setString(3, company.getEmail());
			pst.setString(4, company.getPhone());
			pst.setString(5, company.getFax());
			pst.executeUpdate(); // to insert, update,delete and return nothings

		} catch (Exception e) {
			// ERROR STATUSU DEGISTIRILECEK
			logger.debug("addcompany error:" + e.getMessage());
			e.printStackTrace();
		} finally {
			closePreparedStatement(pst);
			closeConnection(con);
			logger.debug("addcompany completed");
		}
		return result;
	}

	/*public company[] getAllcompanys() {
		logger.debug("getAllcompany started");
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet companyRS = null;
		ResultSet rs=null;
		Company company;

		ArrayList<Company> companys = new ArrayList<>();

		try {

			con = getConnection();
			String query = "SELECT * FROM companies";

			pst = con.prepareStatement(query);
			companyRS = pst.executeQuery();

			while (companyRS.next()) {

				int companyID = companyRS.getInt("ID");
				String companyName = companyRS.getString("FULL_NAME");
				String companyEmail = companyRS.getString("EMAIL");
				String companyPassword = companyRS.getString("PASSWORD");
				int companyCompanyID = companyRS.getInt("COMPANY_ID");


				// GET ROLE
				query = "SELECT ROLE FROM company_roles WHERE EMAIL=?";
				pst = con.prepareStatement(query.toString());
				pst.setString(1, companyEmail);
				rs = pst.executeQuery();

				List<String> roles = new ArrayList<>();
				String[] companyRolesArr;
				while (rs.next()) {
					roles.add(rs.getString("ROLE"));
				}
				companyRolesArr = roles.toArray(new String[roles.size()]);
				logger.info("getAllcompany role:" + companyRolesArr);

				// GET COMPANY
				closeResultSet(rs);
				closePreparedStatement(pst);

				Company company = new Company(0); // bos company olustur
				if (companyCompanyID != 0) { // company bos yani yoksa
					query = "SELECT * FROM companies WHERE ID=?";
					pst = con.prepareStatement(query.toString());
					pst.setInt(1, companyCompanyID);
					rs = pst.executeQuery();

					String companyEmail = null;
					String companyName = null;
					String companyAddress = null;
					String companyPhone = null;
					String companyFax = null;
					while (rs.next()) {
						companyEmail = rs.getString("EMAIL");
						companyName = rs.getString("NAME");
						companyAddress = rs.getString("ADDRESS");
						companyPhone = rs.getString("NAME");
						companyFax = rs.getString("FAX");
					}

					company = new Company(companyCompanyID, companyName, companyEmail, companyPhone, companyFax,
							companyAddress);
					logger.info("getAllcompany company:" + company);
				}
				company = new company(companyID, companyName, companyEmail, companyPassword, company, companyRolesArr);
				logger.info("getAllcompany company:"+company);
				companys.add(company);
			}
		} catch (Exception e) {
			logger.debug("getAllcompany error occured");
			e.printStackTrace();
		} finally {
			closeResultSet(rs);
			closeResultSet(companyRS);
			closePreparedStatement(pst);
			closeConnection(con);
		}
		logger.debug("getAllcompany finished");
		return companys.toArray(new company[companys.size()]);
	}*/
	
	
}
