package ru.breffi.EntityTypeConverterServicePackage;

public class StandartStoryEntity implements IStoryEntity{

	public String _id;
	public String SalesForce_Id;
	
	public String getSalesForceId() {
		return SalesForce_Id;
	}

	public void setStoryId(String newId) {
		this._id = newId;
	}

	public String getStoryId() {
		return this._id;
	}

}
