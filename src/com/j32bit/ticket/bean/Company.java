package com.j32bit.ticket.bean;

public class Company {
	private long id;
	private String name;
	private String email;
	private String phone;
	private String address;
	private String fax;
	
	public Company(long id, String name, String email, String phone, String fax, String address) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.phone = phone;
		this.fax = fax;
		this.address = address;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	public long getId() {
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
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
	}
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		
		sb.append("Company name: ").append(name).append("\n");
		sb.append("id: ").append(id).append("\n");
		sb.append("email: ").append(email).append("\n");
		sb.append("phone: ").append(phone).append("\n");
		sb.append("fax: ").append(fax).append("\n");
		sb.append("address: ").append(address).append("\n");
				
		return sb.toString();
	}

	
}
