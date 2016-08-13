package com.j32bit.ticket.bean;

public class User {

	private int id;
	private String name;
	private String email;
	private String password;
	private int companyID;
	private String[] userRoles;
	private int departmentID;

	public User() {
		// no parameter constructor
	}

	public User(int id, String name, String email, String password, int companyID,int departmentID, String[] userRoles) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
		this.companyID = companyID;
		this.userRoles = userRoles;
		this.departmentID = departmentID;
	}
	
	

	public int getDepartmentID() {
		return departmentID;
	}

	public void setDepartmentID(int departmentID) {
		this.departmentID = departmentID;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public int getCompanyID() {
		return companyID;
	}

	public void setCompanyID(int companyID) {
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

	public String[] getUserRoles() {
		return userRoles;
	}

	public void setUserRoles(String[] userRoles) {
		this.userRoles = userRoles;
	}

	@Override
	public String toString() {
		StringBuilder userDetail = new StringBuilder();
		userDetail.append("name : ").append(name).append("\n");
		userDetail.append("email : ").append(email).append("\n");
		userDetail.append("password : ").append(password).append("\n");
		for (int i = 0; i < userRoles.length; ++i) {
			userDetail.append("role").append(i).append(" = ").append(userRoles[i]).append("\n");
		}

		return userDetail.toString();
	}

}
