package com.coma.go.Utils;

import android.support.annotation.Nullable;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Koma on 11.05.2018.
 */

public class TimeUtils {

    public static int getCurrentMonth(){
        return Calendar.getInstance().get(Calendar.MONTH) + 1;
    }

    public static String getMonth(int month) {
        String monthStr = "Month:" + month;
        try{
            monthStr = new DateFormatSymbols(Locale.ENGLISH).getMonths()[month - 1];
        }catch (ArrayIndexOutOfBoundsException aiob){
            Logger.e("TimeUtilsMonth", aiob);
        }
        return monthStr;
    }

    public static String getCurrentMonthName() {
        int month = getCurrentMonth();
        String monthStr = "Month:" + month;
        try{
            monthStr = new DateFormatSymbols(Locale.ENGLISH).getMonths()[month - 1];
        }catch (ArrayIndexOutOfBoundsException aiob){
            Logger.e("TimeUtilsMonth", aiob);
        }

        return monthStr;
    }

    private static DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH);
    public static long fromDateToUnix(String time) {
        long unixTime = 0;
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+1:00"));
        try {
            unixTime = dateFormat.parse(time).getTime();
            //unixTime = unixTime;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return unixTime;
    }

    public static String fromDateToDate(String time) {
        DateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        Date date = null;
        try {
            date = utcFormat.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        DateFormat pstFormat = new SimpleDateFormat("dd.MM.yyyy-HH:mm");
        pstFormat.setTimeZone(TimeZone.getTimeZone("PST"));

        return pstFormat.format(date);
    }

    public static @Nullable String fromUnixToDate(Long unix){
        String vv = null;
        try {
            Date df = new Date(unix);
            vv = dateFormat.format(df);
        }catch (IllegalArgumentException iae){
            Logger.e("fromUnixToDate", iae);
        }
        return vv;
    }

    public static @Nullable String fromUnixToTime(Long unix){
        DateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
        String vv = null;
        try {
            Date df = new Date(unix);
            vv = dateFormat.format(df);
        }catch (IllegalArgumentException iae){
            Logger.e("fromUnixToDate", iae);
        }
        return vv;
    }
}
