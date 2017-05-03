package ru.breffi.EntityTypeConverterServicePackage;

import java.lang.reflect.Field;

/**
 * Класс предназначенный для получения непостоянных данных из настроек blueprint и выполняющий некоторую общую работу 
 * @author tselo
 *
 */
public abstract class AbstarctTypeConverterService implements EntityTypeConverterService {

	public String tableId;
	public String getTableId(){
		return tableId;
	}
	public void setTableId(String tableId){
		this.tableId = tableId;
	}
	
	public String logTableId;
	public String getLogTableId(){
		return this.logTableId;
	}
	public void setLogTableId(String logTableId) {
		this.logTableId = logTableId;
	}
	

	public String[] getSFQueryFields() {
		Field[] fields =  getSFEntityType().getDeclaredFields();
		String[] result = new String[fields.length+2];
		result[0]="Name";
		result[1]="Id";
		for(int i=2;i<result.length;i++){
			result[i]=fields[i-2].getName();
		}
		return result;
	}

	public String getSFTable() {
		return getSFEntityType().getSimpleName();
	}

	public abstract Class<?> getSFEntityType();
	
}
