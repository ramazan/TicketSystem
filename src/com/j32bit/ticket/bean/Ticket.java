package com.j32bit.ticket.bean;

import java.util.Date;

public class Ticket {
	
	private Date time;
	private User sender;
	private String department;
	private String message;
	private String title;
	private Priority priority;
	
	public Ticket(){}
	

	
	public User getSender() {
		return sender;
	}



	public void setSender(User sender) {
		this.sender = sender;
	}



	public String getDepartment() {
		return department;
	}



	public void setDepartment(String department) {
		this.department = department;
	}



	public Ticket(Date time, User sender, String department, String message, String title, Priority priority) {
		super();
		this.time = time;
		this.sender = sender;
		this.department = department;
		this.message = message;
		this.title = title;
		this.priority = priority;
	}

	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	public Priority getPriority() {
		return priority;
	}

	public void setPriority(Priority priority) {
		this.priority = priority;
	}

}

enum Priority{
	LOW, NORMAL, HIGH, EMERGENCY	
}
