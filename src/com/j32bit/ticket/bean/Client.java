package com.j32bit.ticket.bean;

public class Client {

	private String Name;
	private String Surname;
	private String Company;
	private String Mail;
	private String Password; 
	
	public Client(String Name, String Surname, String Company, String Mail, String Password){
		this.Name=Name;
		this.Surname=Surname;
		this.Company=Company;
		this.Mail=Mail;
		this.Password=Password;
	}
	
	public Client(){}
	
	public String getName(){
		return Name;
	}
	
	public String getSurname(){
		return Surname;
	}
	
	public String getCompany(){
		return Company;
	}
	
	public String getMail(){
		return Mail;
	}
	
	public String getPassword(){
		return Password;
	}
	
	public void setName(String newName){
		this.Name = newName;
	}
	
	public void setSurname(String newSurname){
		this.Surname = newSurname;
	}
	
	public void setCompany(String newCompany){
		this.Company = newCompany;
	}
	
	public void setMail(String newMail){
		this.Mail = newMail;
	}
	
	public void setPassword(String newPassword){
		this.Password = newPassword;
	}
}
