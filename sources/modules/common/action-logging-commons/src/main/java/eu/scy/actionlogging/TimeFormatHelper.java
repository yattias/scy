package eu.scy.actionlogging;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class TimeFormatHelper {
	
	private static TimeFormatHelper instance = null;
	private static SimpleDateFormat ISO8601FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
	
	
	private TimeFormatHelper() {}
	
	public static TimeFormatHelper getInstance() {
		if (instance == null) {
			instance = new TimeFormatHelper();
			ISO8601FORMAT.setTimeZone(TimeZone.getDefault());
		}
		return instance;
	}
	
	public String getCurrentTimeMillisAsISO8601() {
		String result = ISO8601FORMAT.format(new Date(System.currentTimeMillis()));
		  //convert YYYYMMDDTHH:mm:ss+HH00 into YYYYMMDDTHH:mm:ss+HH:00
		  //- note the added colon for the Timezone
		  result = result.substring(0, result.length()-2)
		    + ":" + result.substring(result.length()-2);
		  return result; 
	}
	
	public long getISO8601AsLong(String iso8601Time){
	    Date parse=null;
        try {
            if (iso8601Time.lastIndexOf(":")==iso8601Time.length()-3){
                iso8601Time= iso8601Time.substring(0, iso8601Time.length()-3)+iso8601Time.substring(iso8601Time.length()-2, iso8601Time.length());
            }
            parse = ISO8601FORMAT.parse(iso8601Time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
	    return parse.getTime();
	}
	
}
