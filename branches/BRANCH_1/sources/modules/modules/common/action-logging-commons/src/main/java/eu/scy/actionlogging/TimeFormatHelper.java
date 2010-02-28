package eu.scy.actionlogging;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class TimeFormatHelper {

    private static TimeFormatHelper instance = null;

    //The ISO8601 format. It is modified in some methods of this class.
    private static SimpleDateFormat ISO8601FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
    //private default constructor
    private TimeFormatHelper() {}

    /**
     * This method returns the {@code singleton} instance of the TimeFormatHelper.
     * @return The {@code singleton} instance of this class.
     */
    public static TimeFormatHelper getInstance() {
        if (instance == null) {
            instance = new TimeFormatHelper();
            ISO8601FORMAT.setTimeZone(TimeZone.getDefault());
        }
        return instance;
    }

    /**
     * This method returns the current time as formated (<i>modified</i>) <b>ISO8601</b> {@link String}.<br/>
     * This {@link String} looks like {@code YYYYMMDDTHH:mm:ss+HH:00}.<br/>
     * Please be aware of the last colon. Due to the format, the milliseconds will disappear in the new format.
     * 
     * @return A {@link String} with the current time in (<i>modified</i>) <b>ISO8601</b>.
     */
    public String getCurrentTimeMillisAsISO8601() {
        return getLongAsISO8601(System.currentTimeMillis());
    }

    /**
     * This method returns a given {@code long} (<i>millis from midnight, January 1, 1970 UTC</i>) timestamp in a more readable (modified) <b>ISO8601</b> format.<br/>
     * This String looks like {@code YYYYMMDDTHH:mm:ss+HH:00}. <br/>
     * Please be aware of the last colon. Due to the format, the milliseconds will disappear in the new format.
     * 
     * @param timestampInMillis
     *            The timestamp to convert in millis since 1970.
     * @return A {@link String} with the timestamp in (modified) <b>ISO8601</b>
     */
    public String getLongAsISO8601(long timestampInMillis) {
        String result = ISO8601FORMAT.format(new Date(timestampInMillis));
        // convert YYYYMMDDTHH:mm:ss+HH00 into YYYYMMDDTHH:mm:ss+HH:00
        // - note the added colon for the Timezone
        result = result.substring(0, result.length() - 2) + ":" + result.substring(result.length() - 2);
        return result;
    }

    /**
     * This method converts a given <b>ISO8601</b> or <i>modified</i> <b>ISO8601</b> {@link String} into a timestamp in millis from midnight, January 1, 1970 UTC.<br/>
     * Please be aware that the accuracy of the returned {@code long} is reduced to seconds.
     * 
     * @param iso8601Time
     *            A {@link String} containing the timestamp in <b>ISO8601</b> or <i>modified</i> <b>ISO8601</b>.
     * @return A {@link long} with the converted timestamp in millis from midnight, January 1, 1970 UTC 8 (accuracy is seconds).
     */
    public long getISO8601AsLong(String iso8601Time) {
        Date parse = null;
        try {
            if (iso8601Time.lastIndexOf(":") == iso8601Time.length() - 3) {
                iso8601Time = iso8601Time.substring(0, iso8601Time.length() - 3) + iso8601Time.substring(iso8601Time.length() - 2, iso8601Time.length());
            }
            parse = ISO8601FORMAT.parse(iso8601Time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return parse.getTime();
    }

}
