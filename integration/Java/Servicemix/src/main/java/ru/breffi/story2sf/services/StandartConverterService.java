package ru.breffi.story2sf.services;

public abstract class StandartConverterService implements IConverterService{
	
	
	public StandartConverterService(int tableId, int logTableId, String sftable, String storyIdNameInSF){
		this.tableId=tableId;
		this.logTableId = logTableId;
		this.SFTable = sftable;
		this.StoryIdNameInSF = storyIdNameInSF;
	}
	String SFTable = null;
	
	public int tableId;
	public int getTableId(){
		return tableId;
	}
	
	public void setTableId(int tableId){
		this.tableId = tableId;
	}
	
	public int logTableId;
	public int getLogTableId(){
		return this.logTableId;
	}
	public void setLogTableId(int logTableId) {
		this.logTableId = logTableId;
	}
	public String getSFTable(){
		return this.SFTable;
	}

	String StoryIdNameInSF;
	public String getStoryIdNameInSF() {
		// TODO Auto-generated method stub
		return StoryIdNameInSF;
	}
}
