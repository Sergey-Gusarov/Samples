package ru.breffi.sf_integration.dto;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import com.google.gson.annotations.JsonAdapter;

import ru.breffi.storyclmsdk.TypeAdapters.CustomDateTypeAdapter;

public class StoryLog {
	
	@JsonAdapter(CustomDateTypeAdapter.class)
	 public Date Date=new Date(0);
	
	public Date getDate(){
		return Date;
		
	}
	
	public String getDateForSF(){
		
		TimeZone tz = TimeZone.getTimeZone("UTC");
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss-00:00"); // Quoted "Z" to indicate UTC, no timezone offset
		df.setTimeZone(tz);
		String ISODate = df.format(this.Date);
		//System.out.println("getDateForSF: " + ISODate);
		return ISODate;
	}
	
	public int Updated;
	public int Inserted;
	public String Errors;
}
