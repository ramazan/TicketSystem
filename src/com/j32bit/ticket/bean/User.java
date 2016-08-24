package com.j32bit.ticket.bean;

import java.util.ArrayList;

public class User {

	private long id;
	private String name;
	private String email;
	private String password;
	private Department department;
	private Company company;
	private ArrayList<String> userRoles;

	public User() {
		// no parameter constructor
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

	public ArrayList<String> getUserRoles() {
		return userRoles;
	}

	public void setUserRoles(ArrayList<String> userRoles) {
		this.userRoles = userRoles;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}
	
	public boolean checkRole(String role){
		return userRoles.contains(role);
	}

	@Override
	public String toString() {
		StringBuilder userDetail = new StringBuilder();
		userDetail.append("name : ").append(name).append("\n");
		userDetail.append("email : ").append(email).append("\n");
		userDetail.append("password : ").append(password).append("\n");
		userDetail.append("departmentName : ").append(department.getName()).append("\n");
		userDetail.append("companyName : ").append(company.getName()).append("\n");
		userDetail.append("role").append(userRoles.toString()).append("\n");

		return userDetail.toString();
	}

}
