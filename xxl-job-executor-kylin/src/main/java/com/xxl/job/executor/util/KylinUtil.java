package com.xxl.job.executor.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Calendar;
import java.util.Date;


public class KylinUtil {
    public static final String CR = System.getProperty("line.separator");
    public static String getStackTracker(Throwable e) {
        return getClassicStackTrace(e);
    }

    public static String getClassicStackTrace(Throwable e) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        e.printStackTrace(printWriter);
        String string = stringWriter.toString();
        try {
            stringWriter.close();
        } catch (IOException ioe) {
            // is this really required?
        }
        return string;
    }


    public static Date beforeDateTime(int beforeday, int hour, int minus, int second) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - beforeday);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minus);
        calendar.set(Calendar.SECOND, second);
        return calendar.getTime();
    }
}
