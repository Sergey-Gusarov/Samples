package ru.breffi.EntityTypeConverterServiceImpl.territory;

import ru.breffi.EntityTypeConverterServicePackage.IStoryEntity;

public class Territory implements IStoryEntity {
public String _id;
public String SalesForce_Id;
public String Name;
public String Type;
public String Parent_SFId;


public String getSalesForceId() {
	return SalesForce_Id;
}
public void setStoryId(String newId) {
	// TODO Auto-generated method stub
	_id = newId;
}
public String getStoryId() {
	return _id;
}

}
