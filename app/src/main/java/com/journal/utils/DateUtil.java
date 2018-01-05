package com.journal.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 类名称：com.exing.utils
 * 类描述：
 * 创建人：小土豆   QQ:515353776
 * 创建时间：2017.12.11
 */
public class DateUtil {

    /** 格式(yyyy/MM/dd) */
    public static final String DATE_PATTERN_1 = "yyyy-MM-dd";

    /** 格式(yy/MM/dd) */
    public static final String DATE_PATTERN_2 = "yy/MM/dd";

    /** 格式(yy/MM/dd HH:mm) */
    public static final String DATE_PATTERN_3 = "yy/MM/dd HH:mm";

    /** 格式(yyyy/MM/dd HH:mm) */
    public static final String DATE_PATTERN_4 = "yyyy/MM/dd HH:mm";

    /** 格式(yyyy/MM/dd HH:mm:ss) */
    public static final String DATE_PATTERN_5 = "yyyy/MM/dd HH:mm:ss";
    /** 格式(yyMMddHHmmss)  */
    public static final String DATE_PATTERN_6 = "yyMMddHHmmss";

    /** 格式(yyyy/MM/dd HH:mm:sssss) */
    public static final String DATE_PATTERN_7 = "yyyy/MM/dd HH:mm:sssss";

    /** 格式(yyyy/MM/dd HH:mm:ss:SSS) */
    public static final String DATE_PATTERN_8 = "yyyy/MM/dd HH:mm:ss:SSS";

    /** 格式(yyyyMMddHH) */
    public static final String DATE_PATTERN_9 = "yyyyMMddHH";

    /** 格式(yyyy-MM-dd HH:mm:ss) */
    public static final String DATE_PATTERN_10 = "yyyy-MM-dd HH:mm:ss";

    /** 格式(yyyy-MM-dd HH:mm) */
    public static final String DATE_PATTERN_11 = "yyyy-MM-dd HH:mm";
    /** 格式(yyyy) */
    public static final String DATE_PATTERN_16 = "yyyy";
    /** 格式(yyyy) */
    public static final String DATE_PATTERN_19 = "MM";
    /** 格式(yyyy-MM-dd) */
    public static final String DATE_PATTERN_15 = "yyyy-MM-dd";
    /** 格式(HH:mm:ss) */
    public static final String DATE_PATTERN_14 = "HH:mm:ss";

    /** 格式(yyyy-MM) */
    public static final String DATE_PATTERN_13 = "yyyy-MM";

    /** 格式(yyyyMMddHHmmss) */
    public static final String DATE_PATTERN_12 = "yyyyMMddHHmmss";

    /** 格式(yyyyMMddHHmmss) */
    public static final String DATE_PATTERN_17 = "yyyy.MM.dd HH:mm";
    /** 格式(yyyyMMddHHmmss) */
    public static final String DATE_PATTERN_18 = "yyyy.MM.dd";

    /**
     * 根据指定的格式对日期进行格式化处理
     *
     * @param date
     *            日期
     * @param format
     *            格式
     * @return 日期
     */
    public static String formatDate(Date date, String format) {
        if (null == date) {
            return getCurrentDate(format);
        }
        SimpleDateFormat df = (SimpleDateFormat) DateFormat.getDateInstance();
        df.applyPattern(format);
        return df.format(date);
    }

    /**
     * 获取指定格式的系统时间
     *
     * @param format
     *            格式
     * @return 日期
     */
    public static String getCurrentDate(String format) {
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        return formatDate(date, format);
    }


    /**
     * 获取当前时间10分钟前的指定格式的时间
     *
     * @param format
     *            格式
     * @return 日期
     */
    public static String get10minutesAgoDate(String format) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) - 1);
        Date date = cal.getTime();
        return formatDate(date, format);
    }

    /**
     * 获取当前时间一天前的时间
     * @param format
     * @return
     */
    public static String get1DaysAgoDate(String format){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) - 1);
        Date date = cal.getTime();
        return formatDate(date, format);
    }

    /**获取当前时间1个小时前的时间
     *  @param format
     * @return
     */
    public static String get1HoursAgoDate(String format){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY) - 1);
        Date date = cal.getTime();
        return formatDate(date, format);
    }

    /**
     * 获取当前时间一年前的时间
     * @param format
     * @return
     */
    public static String get1YearAgoDate(String format){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, cal.get(Calendar.YEAR) - 1);
        Date date = cal.getTime();
        return formatDate(date, format);
    }

    /**
     *
     * @param time
     *            时间
     * @param format
     *            格式
     * @return 格式后的时间
     */
    public static Date getFormatDate(String dateStr, String format) {
        Date date = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        try {
            date = dateFormat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return date;
    }
    /**
     *
     * @param dateStr
     * @param format
     * @return
     */
    public static String formatDate(String dateStr, String format) {
        Date date = null;
        if (TextUtil.isValidate(dateStr)) {
            date = new Date(Long.valueOf(dateStr));
        } else {
            date = new Date();
        }
        return formatDate(date, format);
    }

    /**
     * 获取两个时间差     得到的是分钟
     * @param startTime
     * @param endTime
     * @return
     */
    public static long getTimeCha(String startTime,String endTime,String format){
        Date startDtae =  getFormatDate(startTime, format);
        Date endDate =  getFormatDate(endTime, format);
        long timeCha = endDate.getTime() - startDtae.getTime();
        return timeCha / (1000 * 60);
    }


    /**
     *
     * @param time
     * @return
     */
    public static String toTime(long time) {
        SimpleDateFormat formatter = new SimpleDateFormat("mm:ss");
        String dateString = formatter.format(time);
        return dateString;
    }

    /**
     * 判断日期是否是当天
     * @param dateStr
     * @param format
     * @return
     */
    public static boolean isToday(String dateStr,String format){
        if (dateStr != null && !"".equals(dateStr)){
            Date formatDate = getFormatDate(dateStr, format);
            Date today = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(formatDate);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            calendar.setTime(today);
            if(day == calendar.get(Calendar.DAY_OF_MONTH)){
                return true;
            }
        }
        return false;
    }


    /**
     * 返回 start 大于等于 end 的比较结果
     * @param start
     * @param end
     * @return
     */
    public static boolean compare(Date start,Date end){
        if(start != null && end != null){
           if (start.getTime() >= end.getTime()){
               return true;
           }
        }
        return false;
    }


}
