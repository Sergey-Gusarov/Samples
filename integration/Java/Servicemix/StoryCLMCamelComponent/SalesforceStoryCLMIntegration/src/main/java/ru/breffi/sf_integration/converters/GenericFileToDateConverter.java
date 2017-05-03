package ru.breffi.sf_integration.converters;


import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Date;

import org.apache.camel.Converter;
import org.apache.camel.Exchange;
import org.apache.camel.NoTypeConversionAvailableException;
import org.apache.camel.TypeConverter;
import org.apache.camel.TypeConverters;
import org.apache.camel.component.file.GenericFile;

public class GenericFileToDateConverter implements TypeConverters {

	 @Converter
	    public Date FileToDate(@SuppressWarnings("rawtypes") GenericFile file, Exchange exchange) throws ParseException, NoTypeConversionAvailableException, IOException {
		 TypeConverter converter = exchange.getContext().getTypeConverter();
		 String string =  converter.convertTo(String.class, file);
		 return converter.convertTo(Date.class,string);

	    }
	 
	 @Converter
	    public Date StringToDate(String str, Exchange exchange) throws ParseException, NoTypeConversionAvailableException, IOException {
		 DateFormat format = new SimpleDateFormat("yyyyddmm");
		 return format.parse(str);

	    }

}
