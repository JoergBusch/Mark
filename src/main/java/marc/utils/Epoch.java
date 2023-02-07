package marc.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.TimeZone;

public class Epoch {
	
	public static long timestamp(String date) {
		
		long epoch = 0;
		
		SimpleDateFormat f = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
		f.setTimeZone(TimeZone.getTimeZone("GMT+1"));
		try {
		    Date d = f.parse(date);
		    epoch = d.getTime() / 1000l;
		} catch (ParseException e) {
		    e.printStackTrace();
		}
		return epoch;
		
	}
	
	public static ZonedDateTime secondsToZoned(long seconds) {
		
		Date d = new Date(seconds * 1000l);
		ZonedDateTime ldt = ZonedDateTime.ofInstant(d.toInstant(), ZoneId.systemDefault());
		
		return ldt;
		
	}
	
	public static long now() {
		
		long epoch = 0;
		
		 Date d = new Date();
		 epoch = d.getTime() / 1000l;
		 return epoch;
	}
	
	public static long weekAgo() {
		
		long epoch = 0;
		
		Date d = new Date();
		LocalDateTime ldt = LocalDateTime.ofInstant(d.toInstant(), ZoneId.systemDefault());
		ldt = ldt.minusDays(7);
		Date out = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
		
		epoch = out.getTime() / 1000l;
		
		return epoch;
	}
}
