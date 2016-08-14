package com.j32bit.ticket.bean;

import java.util.List;

public class User {

	private long id;
	private String name;
	private String email;
	private String password;
	private long companyID;
	private List<String> userRoles;
	private long departmentID;

	public User() {
		// no parameter constructor
	}

	public User(long id, String name, String email, String password, long companyID,long departmentID, List<String> userRoles) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
		this.companyID = companyID;
		this.userRoles = userRoles;
		this.departmentID = departmentID;
	}
	
	

	public long getDepartmentID() {
		return departmentID;
	}

	public void setDepartmentID(int departmentID) {
		this.departmentID = departmentID;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public long getCompanyID() {
		return companyID;
	}

	public void setCompanyID(long companyID) {
		this.companyID = companyID;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<String> getUserRoles() {
		return userRoles;
	}

	public void setUserRoles(List<String> userRoles) {
		this.userRoles = userRoles;
	}

	@Override
	public String toString() {
		StringBuilder userDetail = new StringBuilder();
		userDetail.append("name : ").append(name).append("\n");
		userDetail.append("email : ").append(email).append("\n");
		userDetail.append("password : ").append(password).append("\n");
		userDetail.append("role").append(userRoles.toString()).append("\n");
	
		return userDetail.toString();
	}

}
