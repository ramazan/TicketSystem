package com.j32bit.ticket.bean;

public class User {

	private String name;
	private String surname;
	private String company;
	private String email;
	private String password;
	private String role;
	
	public User(){
		// no parameter constructor
	}
	
	public User(String name, String surname, String company, String email, String password, String role) {
		super();
		this.name = name;
		this.surname = surname;
		this.company = company;
		this.email = email;
		this.password = password;
		this.role = role;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
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
	
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	@Override
	public String toString() {
		return "User [name=" + name + ", surname=" + surname + ", company=" + company + ", email=" + email
				+ ", password=" + password + ", role=" + role + "]";
	}
	
}
