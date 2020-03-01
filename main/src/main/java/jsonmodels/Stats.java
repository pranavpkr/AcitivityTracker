package jsonmodels;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Stats {

	public String activity_name;
	public long occurrences;
	
	public static Date getManupulatedDateDate( int day ){
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, day);
		return cal.getTime();
	}
}
