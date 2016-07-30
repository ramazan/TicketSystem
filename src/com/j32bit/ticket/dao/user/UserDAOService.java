package com.j32bit.ticket.dao.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.j32bit.ticket.bean.User;
import com.j32bit.ticket.dao.ConnectionHelper;

public class UserDAOService extends ConnectionHelper {
	
	private Logger logger = LogManager.getLogger(UserDAOService.class);
	
	public UserDAOService(){
		logger.debug("constructed");
	}

	public void init(Properties prop){
		logger.debug("initialize started");
		super.init(prop);
		logger.debug("initialize finished");
	}
	
	public void addUser(User user){
		
		Connection con=null;
		PreparedStatement pst=null;
		
		try{
			String addUserQuery = "INSERT INTO users (email,password,name,surname,company) values (?,?,?,?,?)";
			
			con = getConnection();
			pst = con.prepareStatement(addUserQuery);
			pst.setString(1, user.getEmail());
			pst.setString(2, user.getPassword());
			pst.setString(3, user.getName());
			pst.setString(4, user.getSurname());
			pst.setString(5, user.getCompany());
			pst.executeUpdate(); // to insert, update,delete and return nothings	
			
			int roleSize = user.getUserRoles().length;
			for(int i=0;i!=roleSize;++i){
				String addRoleQuery = "INSERT INTO user_roles (email,role) values (?,?)";
				pst = con.prepareStatement(addRoleQuery);
				pst.setString(1, user.getEmail());
				pst.setString(2, user.getUserRoles()[i]);
				pst.executeUpdate();
			}
	
		}catch(Exception e){
			logger.debug("addUser error:"+e.getMessage());
			e.printStackTrace();
		}finally{
			closePreparedStatement(pst);
			closeConnection(con);	
			logger.debug("addUser completed");
		}
	}
	
	
	public User[] getAllUsers(){
		
		Connection con=null;
		PreparedStatement pst=null;
		ResultSet rs=null;
		ResultSet roleResultSet=null;
		
		ArrayList<User> users = new ArrayList<>();
		
		
		try{
			
			con = getConnection();
			String query = "SELECT * FROM users";
			
			pst = con.prepareStatement(query);
			rs = pst.executeQuery();
			
			while(rs.next()){
				
				String name=rs.getString("name");
				String surname=rs.getString("surname");
				String email=rs.getString("email");
				String password=rs.getString("password");
				String company = rs.getString("company");
				
				query = "SELECT * FROM user_roles WHERE email=\""+email+"\"";
				pst = con.prepareStatement(query);
				roleResultSet = pst.executeQuery();
				
				List<String> roles = new ArrayList<>();
				String[] rolesArr;
				while(roleResultSet.next()){
					roles.add(roleResultSet.getString("role"));					
				}
				
				rolesArr = roles.toArray(new String[roles.size()]);
				User user = new User(name,surname,company,email,password,rolesArr);
				
				users.add(user);	
			}	
		}catch(Exception e){
			logger.debug("getAllUser error occured");
			e.printStackTrace();
		}finally{
			closeResultSet(rs);
			closePreparedStatement(pst);
			closeConnection(con);
		}
		
		return users.toArray(new User[users.size()]);
	}
	
	
	
	
	/* 

	public User getUser(String email) {

		logger.debug("getUser is started");
		User user = null;

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement pst = null;

		try {
			user = new User();
			String query = "SELECT * FROM users WHERE EMAIL=?";
			con = getConnection();
			pst = con.prepareStatement(query);
			pst.setString(1, email);
			rs = pst.executeQuery();
			while (rs.next()) {
				user.setName(rs.getString("name"));
				user.setSurname(rs.getString("surname"));
				user.setCompany(rs.getString("company"));
				user.setEmail(rs.getString("email"));
				user.setPassword(rs.getString("password"));
	
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
				closeResultSet(rs);
				closePreparedStatement(pst);
				closeConnection(con);
		}
		logger.debug("getUser is finished");
		return user;
	}
	
	
	public void storeUser(User user) {

		logger.debug("storeUser is started");
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		String query = "INSERT INTO USERS (NAME,SURNAME,COMPANY,EMAIL,PASSWORD) VALUES(?,?,?,?,?)";

		try {
			con = getConnection();
			pst = con.prepareStatement(query);
			pst.setString(1, user.getName());
			// System.out.println(person.getTC()+person.getName()+person.getPhone());
			pst.setString(2, user.getSurname());
			pst.setString(3, user.getCompany());
			pst.setString(4, user.getEmail());
			pst.setString(5, user.getPassword());
			pst.executeUpdate();
			// pst.executeQuery();
			closeResultSet(rs);
			closePreparedStatement(pst);
			closeConnection(con);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
		}
		logger.debug("storeUser is finished");
	}

	public void deleteUser(String email) {

		logger.debug("deleteUser is started");

		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		PreparedStatement pst = null;
		String query = "DELETE FROM USERS WHERE EMAIL=?";

		try {
			con = getConnection();
			pst = con.prepareStatement(query);
			pst.setString(1, email);
			pst.executeUpdate();

			closeResultSet(rs);
			closePreparedStatement(pst);
			closeConnection(con);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
		}

		logger.debug("deleteUser is finished");
	}
	
	public void updateUser(User user) {

		logger.debug("updateUser is started");

		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		String query = "UPDATE USERS SET EMAIL=?, PASSWORD=? WHERE EMAIL=?";
		PreparedStatement pst = null;

		try {
			con = getConnection();
			pst = con.prepareStatement(query);

			pst.setString(1, user.getEmail());
			pst.setString(2, user.getPassword());
			pst.setString(3, user.getEmail());
			pst.executeUpdate();

			closeResultSet(rs);
			closePreparedStatement(pst);
			closeConnection(con);
		} catch (SQLException  e) {
			e.printStackTrace();
		} finally {
		}

		logger.debug("updateUser is finished");
		
	}
*/
	
}
