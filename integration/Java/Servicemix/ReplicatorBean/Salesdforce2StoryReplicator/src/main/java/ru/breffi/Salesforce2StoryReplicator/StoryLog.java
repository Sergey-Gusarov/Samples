package ru.breffi.Salesforce2StoryReplicator;
import java.util.Date;


public class StoryLog {
	

	public String _id;
	 public Date Date;//=new Date(0);
	
	public Date getDate(){
		return Date;
	}
	
	public int Updated;
	public int Inserted;
	public int Deleted;
	public boolean Failed = false;
	public String Note="";
	public long Attempts;
	
	
	public void AddNote(String newNote){
		Note+=Note.isEmpty()?"":"\r\n";
		Note+=newNote;
	}
	@Override
	public boolean equals(Object slog){
		StoryLog other = (StoryLog) slog;
		if (this.Note == null) this.Note ="";
		if (other.Note == null) other.Note ="";
		return 
				this.Updated == other.Updated &&
				this.Inserted == other.Inserted &&
				this.Deleted == other.Deleted &&
				this.Note.equals(other.Note) &&
				this.Failed == other.Failed ;
	}
}
