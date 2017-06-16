package com.xxl.job.executor.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Calendar;
import java.util.Date;

/**
 * 工具方法
 */
public class KylinUtil {
    public static final String CR = System.getProperty("line.separator");

    /**
     * 打印当前堆栈信息
     *
     * @param e
     * @return
     */
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

    /**
     * 返回前几天的，某个时间的时分秒，如2017-06-16 8:27:59秒
     *
     * @param beforeday 前几天
     * @param hour
     * @param minus
     * @param second
     * @return
     */
    public static Date beforeDateTime(int beforeday, int hour, int minus, int second) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - beforeday);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minus);
        calendar.set(Calendar.SECOND, second);
        return calendar.getTime();
    }
}
