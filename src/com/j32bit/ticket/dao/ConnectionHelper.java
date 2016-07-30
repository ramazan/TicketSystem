package com.j32bit.ticket.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConnectionHelper {

	private final static Logger logger = LogManager.getLogger();
	private boolean useDataPool;
	private String JDBCDriverName;
	private String JDBCURL;
	private String dbUser;
	private String dbPassword;
	private String JNDIName;

	public void init(Properties properties) {
		logger.info("ConnectionHelper initializing");
		useDataPool = properties.getProperty("database.use.pool", "false").equalsIgnoreCase("true");
		JDBCDriverName = properties.getProperty("database.jdbc.driver");
		JDBCURL = properties.getProperty("database.jdbc.url");
		dbUser = properties.getProperty("database.username");
		dbPassword = properties.getProperty("database.password");
		JNDIName = properties.getProperty("database.jndi.name");
		logger.info("initialize initialized");
	}

	public Connection getConnection() throws Exception {
		if (useDataPool)
			return getConnectionFromDatasource();
		else
			return getConnectionLocal();
	}

	public Connection getConnectionLocal() throws Exception {
		Connection newConn = null;
		Class.forName(JDBCDriverName).newInstance();
		newConn = DriverManager.getConnection(JDBCURL, dbUser, dbPassword);
		logger.trace("Connection taken :" + newConn.getAutoCommit());
		return newConn;
	}

	public Connection getTransactionalConnection() throws Exception {
		Connection conn = getConnection();
		if (conn != null && !conn.isClosed()) {
			if (conn.getAutoCommit()) {
				conn.setAutoCommit(false);
			} else {
				// Devam eden transaction varsa roolback yapalim.
				logger.warn("Transaction is already active. Rollbacking active transaction");
				conn.rollback();
			}
		}
		return conn;
	}

	private Connection getConnectionFromDatasource() throws NamingException, SQLException {
		logger.trace("DataSource " + JNDIName);
		Context envCtx = (Context) new InitialContext();
		DataSource ds = (DataSource) envCtx.lookup(JNDIName);
		Connection conn = ds.getConnection();
		logger.trace("Connection taken");
		return conn;
	}

	public void closeConnection(Connection con) {
		try {
			if (con != null)
				con.close();
			logger.trace("Connection closed");
		} catch (Exception e) {
			logger.trace("Connection close error: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void closeResultSet(ResultSet rs) {
		try {
			if (rs != null)
				rs.close();
			logger.trace("ResultSet closed");
		} catch (Exception e) {
			logger.trace("ResultSet close error: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void closePreparedStatement(PreparedStatement pst) {
		try {
			if (pst != null)
				pst.close();
			logger.trace("PreparedStatement closed");
		} catch (Exception e) {
			logger.trace("PreparedStatement close error: " + e.getMessage());
			e.printStackTrace();
		}
	}
}
