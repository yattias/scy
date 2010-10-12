package eu.scy.actionlogging.logger;

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
        // convert YYYYMMDDTHH:mm:ss+HH00 into YYYYMMDDTHH:mm:ss+HH:00
        // - note the added colon for the Timezone
        result = result.substring(0, result.length() - 2) + ":" + result.substring(result.length() - 2);
        return result;
    }

    public long getISO8601AsTimeStamp(String isoTime) throws ParseException {
        String toParse = isoTime.substring(0, isoTime.length()-3)+isoTime.substring(isoTime.length() - 2);
        Date d = ISO8601FORMAT.parse(toParse);
        return d.getTime();
    }

}
