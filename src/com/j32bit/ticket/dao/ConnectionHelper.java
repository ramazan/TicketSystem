package com.j32bit.ticket.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

public class ConnectionHelper {

	private final static Logger logger = LogManager.getLogger();
	
	private DataSource dataSource;

	public void init(Properties properties) {
		
		if(properties!=null){
			dataSource = getMysqlDataSource(properties);
		}else{
			// TODO MYSQL DATASOURCE CONNECTION POOL?
			// BURAK ABIIIII
		}
	}
	
	public Connection getConnection(){
		
		Connection connection=null;
		
		logger.debug("getConnection started");
		
		try {
			connection = dataSource.getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		logger.debug("getConnection finished");
		return connection;
	}
	

	public void closeConnection(Connection con) {
		try {
			if (con != null)
				con.close();
			logger.debug("Connection closed");
		} catch (Exception e) {
			logger.debug("Connection close error: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void closeResultSet(ResultSet rs) {
		try {
			if (rs != null)
				rs.close();
			logger.debug("ResultSet closed");
		} catch (Exception e) {
			logger.debug("ResultSet close error: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void closePreparedStatement(PreparedStatement pst) {
		try {
			if (pst != null)
				pst.close();
			logger.debug("PreparedStatement closed");
		} catch (Exception e) {
			logger.debug("PreparedStatement close error: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private MysqlDataSource getMysqlDataSource(Properties prop) {

		MysqlDataSource mysqlDS = null;

		logger.debug("Started to create mysqlds from properties");
		String dbUsername = prop.getProperty("dbUsername");
		String dbPassword = prop.getProperty("dbPassword");
		int dbPort = Integer.parseInt(prop.getProperty("dbPort"));
		String dbServerName = prop.getProperty("dbServerName");
		String dbName = prop.getProperty("dbName");

		if (dbServerName == null || dbName == null || dbPassword == null || dbName == null) {
			logger.debug("Properties file reading implementation error");
		} else {
			mysqlDS = new MysqlDataSource();
			mysqlDS.setUser(dbUsername);
			mysqlDS.setPassword(dbPassword);
			mysqlDS.setPort(dbPort);
			mysqlDS.setDatabaseName(dbName);
			mysqlDS.setServerName(dbServerName);
			logger.debug("mysql datasource created.");
		}

		return mysqlDS;
	}

}
