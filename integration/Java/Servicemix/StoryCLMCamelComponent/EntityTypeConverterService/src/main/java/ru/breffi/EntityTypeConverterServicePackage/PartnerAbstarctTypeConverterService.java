package ru.breffi.EntityTypeConverterServicePackage;

/**
 * Класс предназначенный для получения непостоянных данных из настроек blueprint и выполняющий некоторую общую работу 
 * @author tselo
 *
 */
public abstract class PartnerAbstarctTypeConverterService implements PartnerTypeConverterService{

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

	public String getSFIdFieldName() {
		// TODO Auto-generated method stub
		return "SalesForce_Id";
	}

	
}
