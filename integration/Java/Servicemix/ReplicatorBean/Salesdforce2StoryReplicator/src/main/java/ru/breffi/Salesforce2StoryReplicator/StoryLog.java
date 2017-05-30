package ru.breffi.Salesforce2StoryReplicator;
import java.util.Date;


public class StoryLog {
	

	 public Date Date;//=new Date(0);
	
	public Date getDate(){
		return Date;
	}
	
	public int Updated;
	public int Inserted;
	public int Deleted;
	public String Errors;
}
