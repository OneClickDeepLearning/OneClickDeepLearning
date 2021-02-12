package acceler.ocdl.utils;

import org.apache.log4j.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class TimeUtil {
    public static final String DATA_FORMAT = "yyyy-MM-dd hh:mm:ss";
    private static final Logger log = Logger.getLogger(TimeUtil.class);

    public static Date currentTime() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTime();
    }

    public static String currentTimeStampStr() {
        return String.valueOf(new Date().getTime());
    }

    public static Date convertStringToDate(String s) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATA_FORMAT);
        return dateFormat.parse(s);
    }

    public static String convertDateToString(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATA_FORMAT);
        return dateFormat.format(date);
    }
}
