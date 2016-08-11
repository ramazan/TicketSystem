package com.j32bit.ticket.bean;

import java.util.Date;

public class Ticket {

	private Date time;
	private int id;
	private String from;
	private String departmentName;
	private String message;
	private String title;
	private Priority priority;
	private int status;

	public Ticket() {
	}

	public Ticket(int id, Date time, String from, String departmentName, String title, String message,
			Priority priority, int status) {
		this.id = id;
		this.time = time;
		this.from = from;
		this.departmentName = departmentName;
		this.title = title;
		this.message = message;
		this.priority = priority;
		this.status = status;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
