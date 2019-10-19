package acceler.ocdl.utils;

import acceler.ocdl.dto.ModelDto;
import acceler.ocdl.exception.OcdlException;
import acceler.ocdl.service.impl.DefaultModelServiceImpl;
import org.apache.log4j.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TimeUtil {
    public static final String DATA_FORMAT = "yyyy-MM-dd hh:mm:ss";
    private static final Logger log = Logger.getLogger(TimeUtil.class);

    public static Date currentTime() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTime();
    }

    public static Date convertStringToDate(String s) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATA_FORMAT);
        return dateFormat.parse(s);
    }

    public static String convertDateToString(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATA_FORMAT);
        return dateFormat.format(date);
    }

    public static boolean isRecent(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day - 1);
        log.error("model time stamp: " + convertDateToString(date));
        log.error("previous day time stamp: " + convertDateToString(c.getTime()));

        log.error("if after: " + date.after(c.getTime()));
        return date.after(c.getTime());
    }

    public static void addNewFlag(List<ModelDto> modelDtos) {
        modelDtos.stream()
                .filter(m -> {
                    try {
                        if (m.getTimeStamp() != null) {
                            Date time = TimeUtil.convertStringToDate(m.getTimeStamp());
                            return TimeUtil.isRecent(time);
                        } else {
                            return false;
                        }
                    } catch (ParseException e) {
                        throw new OcdlException("Invalid time format of ModelDto timestamp.");
                    }
                })
                .forEach(m -> m.setNewFlag(true));
    }
}
