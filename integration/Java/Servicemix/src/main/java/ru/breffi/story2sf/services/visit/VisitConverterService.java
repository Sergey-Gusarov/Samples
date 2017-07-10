package ru.breffi.story2sf.services.visit;

import java.util.Calendar;
import java.util.Date;

import com.sforce.soap.partner.sobject.SObject;

import ru.breffi.story2sf.services.IStoryEntity;
import ru.breffi.story2sf.services.StandartConverterService;

public class VisitConverterService extends StandartConverterService{

	public VisitConverterService(int tableId, int logTableId) {
		super(tableId, logTableId, "BF_Visits__c", "BF_Visits_StoryCLM_Id__c");
	}
	
	@Override
	public Class<? extends IStoryEntity> getStoryType() {
		return Visit.class;
	}
	
	private Calendar dateToCalendar(Date date) {
			Calendar calendar = Calendar.getInstance();
			if (date!=null) 
				{
					calendar.setTime(date);
					return calendar;
				}
			else return null;
			
		}
	  
	@Override
	public SObject ConvertToSF(IStoryEntity sf) {
		  SObject s = new SObject();
		  Visit visit = (Visit) sf;
		  s.setType("BF_Visits__c");
		  s.setId(visit.SFId);
		  s.addField("BF_Visits_Checkin__c", visit.CheckIn);
		  s.addField("BF_Visits_Confirmed__c", visit.Confirmed);
		  s.addField("BF_Visits_Offset__c", visit.Offset);
		  s.addField("BF_Visits_PresentationId__c", visit.PresentationId);
		  s.addField("BF_Visits_SessionId__c", visit.SessionId);
		  
		  s.addField("BF_Visits_Slides__c", visit.Slides);
		  s.addField("BF_Visits_UserId__c", visit.UserId);
		  s.addField("BF_Visits_Address__c", visit.Address);
		  
		  s.addField("BF_Visits_Start__c", dateToCalendar(visit.Start));
		  s.addField("BF_Visits_End__c", dateToCalendar(visit.End));
		  s.addField("BF_Visits_Length__c", visit.Length);
		  s.addField("BF_Visits_Longitude__c", visit.Longitude);
		  s.addField("BF_Visits_Latitude__c", visit.Latitude);
		  
		  s.addField("BF_Visits_SFEmployeeId__c", visit.SFEmployeeId);
		  s.addField("BF_Visits_SFUser__c", visit.SFUserId);
		  s.addField("BF_Visits_Territory__c", visit.SFTerritoryId);
		  s.addField("BF_Visits_Company__c", visit.SFCompanyId);
		  s.addField("BF_Visits_Username__c", visit.Username);
		  s.addField("BF_Visits_StoryCLM_Id__c", visit._id);
		  return s;
	}

	

	

}
