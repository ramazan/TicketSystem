package com.j32bit.ticket.bean;

public class SessionUserBean {

	private String email;
	private String[] userRoles;

	public SessionUserBean() {
	}

	public SessionUserBean(String email, String[] userRoles) {
		this.email = email;
		this.userRoles = userRoles;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String[] getUserRoles() {
		return userRoles;
	}

	public void setUserRoles(String[] userRoles) {
		this.userRoles = userRoles;
	}
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		
		sb.append("E-Mail: ").append(getEmail()).append(" - ");
		
		String [] roles = getUserRoles();
		int roleSize = roles.length;
		
		for(int i=0;i!= roleSize;++i)
			sb.append("role").append(i).append(". ").append(roles[i]).append(" - ");
		
		return sb.toString();		
	}

}
