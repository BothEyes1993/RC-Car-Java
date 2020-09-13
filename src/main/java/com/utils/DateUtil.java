package com.utils;

import com.constants.Constants;
import com.exception.CrmebException;
import com.utils.vo.dateLimitUtilVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/** excel操作类
 * @author Mr.zhang
 * @Description 日期时间类
 * @since 2020-04-17
 **/
public final class DateUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(DateUtil.class);

    private DateUtil() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 获取当前日期,指定格式
     * 描述:<描述函数实现的功能>.
     *
     * @return
     */
    public static String nowDate() {
        return nowDate(Constants.DATE_FORMAT_NUM);
    }

    /**
     * 获取当前年,指定格式
     * 描述:<描述函数实现的功能>.
     *
     * @return
     */
    public static String nowYear() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.YEAR) + "";
    }

    /**
     * 获取上一年,指定格式
     * 描述:<描述函数实现的功能>.
     *
     * @return
     */
    public static String lastYear() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -1);
        return cal.get(Calendar.YEAR) + "";
    }

    /**
     * 获取当前日期,指定格式
     * 描述:<描述函数实现的功能>.
     *
     * @return
     */
    public static Date nowDateTime() {
        return strToDate(nowDateTimeStr(), Constants.DATE_FORMAT);
    }

    /**
     * 获取当前日期,指定格式
     * 描述:<描述函数实现的功能>.
     *
     * @return
     */
    public static String nowDateTimeStr() {
        return nowDate(Constants.DATE_FORMAT);
    }

    /**
     * 获取当前日期,指定格式
     * 描述:<描述函数实现的功能>.
     *
     * @return
     */
    public static String nowDate(String DATE_FORMAT) {
        SimpleDateFormat dft = new SimpleDateFormat(DATE_FORMAT);
        return dft.format(new Date());
    }

    /**
     * 获取当前日期,指定格式
     * 描述:<描述函数实现的功能>.
     *
     * @return
     */
    public static String nowDateTime(String DATE_FORMAT) {
        SimpleDateFormat dft = new SimpleDateFormat(DATE_FORMAT);
        return dft.format(new Date());
    }

    /**
     * 获取当前日期,指定格式
     * 描述:<描述函数实现的功能>.
     *
     * @return
     */
    public static Integer getNowTime() {
        long t = (System.currentTimeMillis()/1000L);
        return Integer.parseInt(String.valueOf(t));
    }

    /**
     * 获取当前日期,指定格式
     * 描述:<描述函数实现的功能>.
     *
     * @return
     */
    public static Long getTime() {
        return (System.currentTimeMillis()/1000L);
    }

    /**
     * 获取当前日期,指定格式
     * 描述:<描述函数实现的功能>.
     *
     * @return
     */
    public static Date nowDateTimeReturnDate(String DATE_FORMAT) {
        SimpleDateFormat dft = new SimpleDateFormat(DATE_FORMAT);
        return strToDate(dft.format(new Date()), DATE_FORMAT);
    }

    /**
     * convert a date to string in a specifies fromat.
     *
     * @param date
     * @param DATE_FORMAT
     * @return
     */
    public static String dateToStr(Date date, String DATE_FORMAT) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat myFormat = new SimpleDateFormat(DATE_FORMAT);
        return myFormat.format(date);
    }

    /**
     * parse a String to Date in a specifies fromat.
     *
     * @param dateStr
     * @param DATE_FORMAT
     * @return
     * @throws ParseException
     */
    public static Date strToDate(String dateStr, String DATE_FORMAT) {
        SimpleDateFormat myFormat = new SimpleDateFormat(DATE_FORMAT);
        try {
            return myFormat.parse(dateStr);
        } catch (ParseException e) {
            return null;
        }
    }


    /**
     * date add Second
     *
     * @param date
     * @param num
     * @return
     */
    public static Date addSecond(Date date, int num) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.SECOND, num);
        return calendar.getTime();
    }

    /**
     * date add Second return String
     *
     * @param date
     * @param num
     * @return
     */
    public static String addSecond(Date date, int num, String DATE_FORMAT) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.SECOND, num);
        return dateToStr(calendar.getTime(), DATE_FORMAT);
    }

    /**
     * 指定日期加上天数后的日期
     *
     * @param num     为增加的天数
     * @param newDate 创建时间
     * @return
     */
    public static final String addDay(String newDate, int num, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        try {
            Date currdate = format.parse(newDate);
            Calendar ca = Calendar.getInstance();
            ca.setTime(currdate);
            ca.add(Calendar.DATE, num);
            return format.format(ca.getTime());
        } catch (ParseException e) {
            LOGGER.error("转化时间出错,", e);
            return null;
        }
    }

    /**
     * 指定日期加上天数后的日期
     *
     * @param num     为增加的天数
     * @param newDate 创建时间
     * @return
     */
    public static final String addDay(Date newDate, int num, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        Calendar ca = Calendar.getInstance();
        ca.setTime(newDate);
        ca.add(Calendar.DATE, num);
        return format.format(ca.getTime());
    }

    /**
     * convert long to date
     *
     * @param dateLong
     * @return
     */
    public static Date longToDate(long dateLong) {
        return new Date(dateLong);
    }

    /**
     * convert long to date string
     *
     * @param dateLong
     * @param DATE_FORMAT
     * @return
     */
    public static String longToDate(long dateLong, String DATE_FORMAT) {
        return dateToStr(new Date(dateLong), DATE_FORMAT);
    }

    /**
     * compare two date String with a pattern
     *
     * @param date1
     * @param date2
     * @param pattern
     * @return
     */
    public static int compareDate(String date1, String date2, String pattern) {
        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(pattern);
        try {
            Date dt1 = DATE_FORMAT.parse(date1);
            Date dt2 = DATE_FORMAT.parse(date2);
            if (dt1.getTime() > dt2.getTime()) {
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                return -1;
            } else {
                return 0;
            }

        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 检查日期格式是否合法
     * @param date
     * @param style
     * @return
     */
    public static boolean checkDateFormat(String date, String style) {
        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(style);
        try {
            // 设置lenient为false.
            // 否则SimpleDateFormat会比较宽松地验证日期，比如2007/02/29会被接受，并转换成2007/03/01
            DATE_FORMAT.setLenient(false);
            DATE_FORMAT.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }


    public static long getTwoDateDays(Date beforeDay, Date afterDay) {
        SimpleDateFormat sm = new SimpleDateFormat(Constants.DATE_FORMAT_NUM);
        long days = -1;
        try {
            days = (sm.parse(sm.format(afterDay)).getTime() - sm.parse(sm.format(beforeDay)).getTime()) / (1000 * 3600 * 24);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return days;
    }


    //获取时间戳11位
    public static int getSecondTimestamp(Date date){
        if (null == date) {
            return 0;
        }
        String timestamp = String.valueOf(date.getTime()/1000);
        return Integer.parseInt(timestamp);
    }

    //获取时间戳11位
    public static int getSecondTimestamp(String date){
        if (null == date) {
            return 0;
        }
        Date date1 = strToDate(date, Constants.DATE_FORMAT);
        if(date1 == null){
            return 0;
        }
        String timestamp = String.valueOf(date1.getTime()/1000);
        return Integer.parseInt(timestamp);
    }

    //获取时间戳11位
    public static int getSecondTimestamp(){
        Date date = strToDate(nowDateTime(Constants.DATE_FORMAT), Constants.DATE_FORMAT);
        if (null == date) {
            return 0;
        }
        String timestamp = String.valueOf(date.getTime()/1000);
        return Integer.parseInt(timestamp);
    }

    /** 获得昨天日期:yyyy-MM-dd  HH:mm:ss */
    public static String getYesterdayStr() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -1);
        SimpleDateFormat startSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return startSdf.format(c.getTime());
    }

    /** 获得本周第一天:yyyy-MM-dd HH:mm:ss */
    public static String getWeekStartDay() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.WEEK_OF_MONTH, 0);
        c.set(Calendar.DAY_OF_WEEK, 2);
        SimpleDateFormat startSdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        return startSdf.format(c.getTime());
    }

    /** 获得本周最后一天:yyyy-MM-dd HH:mm:ss */
    public static String getWeekEndDay() {
        return addDay(getWeekStartDay(), 7, Constants.DATE_FORMAT);
    }

    /** 获得上周第一天:yyyy-MM-dd HH:mm:ss */
    public static String getLastWeekStartDay() {
        return addDay(getWeekStartDay(), -7, Constants.DATE_FORMAT);
    }

    /** 获得上周最后一天:yyyy-MM-dd HH:mm:ss */
    public static String getLastWeekEndDay() {
        return addDay(getLastWeekStartDay(), 7, Constants.DATE_FORMAT);
    }

    /** 获得本月最后一天:yyyy-MM-dd HH:mm:ss */
    public static String getMonthEndDay() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        SimpleDateFormat endSdf = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
        return endSdf.format(c.getTime());
    }

    /** 获得上月第一天:yyyy-MM-dd HH:mm:ss */
    public static String getLastMonthStartDay() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, -1);
        c.set(Calendar.DAY_OF_MONTH, 1);
        SimpleDateFormat startSdf = new SimpleDateFormat("yyyy-MM-01 00:00:00");
        return startSdf.format(c.getTime());
    }

    /** 获得上月最后一天:yyyy-MM-dd HH:mm:ss */
    public static String getLastMonthEndDay() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, -1);
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        SimpleDateFormat endSdf = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
        return endSdf.format(c.getTime());
    }

    /** 获得上年第一天:yyyy-MM-dd HH:mm:ss */
    public static String getLastYearStartDay() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.YEAR, -1);
        SimpleDateFormat startSdf = new SimpleDateFormat("yyyy-01-01 00:00:00");
        return startSdf.format(c.getTime());
    }

    /** 获得上年最后一天:yyyy-MM-dd HH:mm:ss */
    public static String getLastYearEndDay() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.YEAR, -1);
        SimpleDateFormat endSdf = new SimpleDateFormat("yyyy-12-31 23:59:59");
        return endSdf.format(c.getTime());
    }

    /**
     * 计算时间范围
     * @param data string 类型
     * @author Mr.Zhang
     * @since 2020-05-06
     * @return dateLimitUtilVo
     */
    public static dateLimitUtilVo getDateLimit(String data){
        //时间计算
        String startTime = null;
        String endTime = DateUtil.nowDateTime(Constants.DATE_FORMAT);
        String day = DateUtil.nowDateTime(Constants.DATE_FORMAT_START);
        String end = DateUtil.nowDateTime(Constants.DATE_FORMAT_END);

        if(!StringUtils.isBlank(data)){
            switch (data){
                case Constants.SEARCH_DATE_DAY:
                    startTime = day;
                    break;
                case Constants.SEARCH_DATE_YESTERDAY:
                    startTime = DateUtil.addDay(day, -1, Constants.DATE_FORMAT_START);
                    endTime = DateUtil.addDay(end, -1, Constants.DATE_FORMAT_END);
                    break;
                case Constants.SEARCH_DATE_LATELY_7:
                    startTime = DateUtil.addDay(day, -7, Constants.DATE_FORMAT_START);
                    break;
                case Constants.SEARCH_DATE_WEEK:
                    startTime = getWeekStartDay();
                    endTime = getWeekEndDay();
                    break;
                case Constants.SEARCH_DATE_PRE_WEEK:
                    startTime = getLastWeekStartDay();
                    endTime = getLastWeekEndDay();
                    break;
                case Constants.SEARCH_DATE_LATELY_30:
                    startTime = DateUtil.addDay(day, -30, Constants.DATE_FORMAT_START);
                    break;
                case Constants.SEARCH_DATE_MONTH:
                    startTime = DateUtil.nowDateTime(Constants.DATE_FORMAT_MONTH_START);
                    endTime = getMonthEndDay();
                    break;
                case Constants.SEARCH_DATE_PRE_MONTH:
                    startTime = getLastMonthStartDay();
                    endTime = getLastMonthEndDay();
                    break;
                case Constants.SEARCH_DATE_YEAR:
                    startTime = DateUtil.nowDateTime(Constants.DATE_FORMAT_YEAR_START);
                    endTime = DateUtil.nowDateTime(Constants.DATE_FORMAT_YEAR_END);
                    break;
                case Constants.SEARCH_DATE_PRE_YEAR:
                    startTime = getLastYearStartDay();
                    endTime = getLastYearEndDay();
                    break;
                default:
                    List<String> list = CrmebUtil.stringToArrayStr(data);
                    if(list.size() == 1){
                        throw new CrmebException("选择时间参数格式错误，请在 " +
                                Constants.SEARCH_DATE_DAY + "|" +
                                Constants.SEARCH_DATE_YESTERDAY + "|" +
                                Constants.SEARCH_DATE_LATELY_7 + "|" +
                                Constants.SEARCH_DATE_LATELY_30 + "|" +
                                Constants.SEARCH_DATE_MONTH + "|" +
                                Constants.SEARCH_DATE_YEAR + "|自定义时间范围（格式：yyyy-MM-dd HH:mm:ss，两个时间范围用逗号分割）");
                    }
                    startTime = list.get(0);
                    endTime = list.get(1);
                    break;
            }
        }
        return new dateLimitUtilVo(startTime, endTime);
    }

    /**
     * 获取某一时间段内的时间集合
     * @param data string 类型
     * @author Mr.Zhang
     * @since 2020-05-06
     * @return List<Date>
     */
    public static List<String> getListDate(String data) {

        //获取30天的开始结束日期
        dateLimitUtilVo dateLimit = DateUtil.getDateLimit(data);

        //定义日期集合
        List<String> date = new ArrayList<>();

        //开始日期
        Date startDate = DateUtil.strToDate(dateLimit.getStartTime(), Constants.DATE_FORMAT);

        //结束日期
        Date endDate = DateUtil.strToDate(dateLimit.getEndTime(), Constants.DATE_FORMAT);

        while (endDate.after(startDate)){
            date.add(DateUtil.dateToStr(startDate, Constants.DATE_FORMAT_DATE)); // 放入集合
            startDate = DateUtil.strToDate(DateUtil.addDay(startDate, 1, Constants.DATE_FORMAT), Constants.DATE_FORMAT); //循环一次 加一天
        }

        return date;
    }
}
