package acceler.ocdl.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtil {

    public static final String DATA_FORMAT = "yyyy-MM-dd";


    public static Date currentTime() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTime();
    }

    public static Date convertStringToDate(String s) throws ParseException {
        Date date = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATA_FORMAT);
        return dateFormat.parse(s);
    }
}
