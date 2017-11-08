package ru.breffi.Salesforce2StoryReplicator;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class Utils {
	
	
	  public static String GetIsoDate(Date date){
			TimeZone tz = TimeZone.getTimeZone("UTC");
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss-00:00"); // Quoted "Z" to indicate UTC, no timezone offset
			df.setTimeZone(tz);
			String ISODate = df.format(date);
			return ISODate;
	  }

	  public static String JoinByComma(String[] strings){
		  if (strings.length==0) return "";
		  String res =  String.join("\', \'",strings);
		  return "\'"+res+"\'";
	  }
	  
	  public static String JoinByComma(List<String> strings){
		  return JoinByComma(strings.toArray(new String[0]));
	  }
	  
	  /**
	   * Get a diff between two dates
	   * @param date1 the oldest date
	   * @param date2 the newest date
	   * @param timeUnit the unit in which you want the diff
	   * @return the diff value, in the provided unit
	   */
	  public static long GetDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
	      long diffInMillies = date2.getTime() - date1.getTime();
	      return timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
	  }
	  
	
	 /*
	  * Обработка списка по частям размером partSize
	  */
	  public static <T> void ListConsumePiecemeal(List<T> list, int partSize, ThrowingConsumer<List<T>> consumer){
		 for(int i=0;i<list.size();i+=partSize){
			 List<T> currentList = list.subList(i, Math.min(i+partSize, list.size()));
			 consumer.accept(currentList);
		 }
	 }
	 
	 public static String JoinStackTrace(Throwable e) {
		    StringWriter writer = null;
		    try {
		        writer = new StringWriter();
		        JoinStackTrace(e, writer);
		        return writer.toString();
		    }
		    finally {
		        if (writer != null)
		            try {
		                writer.close();
		            } catch (IOException e1) {
		                // ignore
		            }
		    }
		}

		public static void JoinStackTrace(Throwable e, StringWriter writer) {
		    PrintWriter printer = null;
		    try {
		        printer = new PrintWriter(writer);

		        while (e != null) {

		            printer.println(e);
		            StackTraceElement[] trace = e.getStackTrace();
		            for (int i = 0; i < trace.length; i++)
		                printer.println("\tat " + trace[i]);

		            e = e.getCause();
		            if (e != null)
		                printer.println("Caused by:\r\n");
		        }
		    }
		    finally {
		        if (printer != null)
		            printer.close();
		    }
		}
}
