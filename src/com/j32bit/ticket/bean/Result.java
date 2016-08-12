package com.j32bit.ticket.bean;

import com.j32bit.ticket.enums.Status;

public class Result {
	public Status status;
	public int companyID; // yeni company eklenince bu degerde dondurulecek
	
	public Result(){
		companyID=0;
		status = Status.SUCCESS;
	}
}
