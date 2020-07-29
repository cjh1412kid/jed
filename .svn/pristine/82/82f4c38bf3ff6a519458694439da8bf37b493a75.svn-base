/**
 * Copyright 2018 人人开源 http://www.renren.io
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package io.nuite.common.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import io.nuite.common.exception.RRException;

/**
 * 日期处理
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年12月21日 下午12:53:33
 */
public class DateUtils {
    /**
     * 时间格式(yyyy-MM-dd)
     */
    public final static String DATE_PATTERN = "yyyy-MM-dd";
    /**
     * 时间格式(yyyy/MM/dd)
     */
    public final static String DATE_PATTERN2 = "yyyy/MM/dd";
    /**
     * 时间格式(yyyy-MM-dd HH:mm:ss)
     */
    public final static String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * 时间格式(yyyy-MM-dd HH:mm:ss)
     */
    public final static String DATE_TIME_SECOND_PATTERN = "yyyy-MM-dd HH:mm:ss.SSS";

    public final static String WX_DATE_PATTERN = "yyyyMMddHHmmss";

    /**
     * 日期格式化 日期格式为：yyyy-MM-dd
     *
     * @param dateStr 日期
     * @return 返回yyyy-MM-dd格式日期
     */
    public static Date parse(String dateStr, String pattern) {
        if (StringUtils.isNotBlank(dateStr)) {
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            try {
                return df.parse(dateStr);
            } catch (ParseException e) {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * 日期格式化 日期格式为：yyyy-MM-dd
     *
     * @param date 日期
     * @return 返回yyyy-MM-dd格式日期
     */
    public static String format(Date date) {
        return format(date, DATE_PATTERN);
    }
    
    /**
     * 日期格式化 日期格式为：yyyy/MM/dd
     *
     * @param date 日期
     * @return 返回yyyy/MM/dd格式日期
     */
    public static String format2(Date date) {
        return format(date, DATE_PATTERN2);
    }
    

    /**
     * 日期格式化 日期格式为：yyyy-MM-dd
     *
     * @param date    日期
     * @param pattern 格式，如：DateUtils.DATE_TIME_PATTERN
     * @return 返回yyyy-MM-dd格式日期
     */
    public static String format(Date date, String pattern) {
        if (date != null) {
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            return df.format(date);
        }
        return null;
    }

    /**
     * 字符串转换成日期
     *
     * @param strDate 日期字符串
     * @param pattern 日期的格式，如：DateUtils.DATE_TIME_PATTERN
     */
    public static Date stringToDate(String strDate, String pattern) {
        if (StringUtils.isBlank(strDate)) {
            return null;
        }

        DateTimeFormatter fmt = DateTimeFormat.forPattern(pattern);
        return fmt.parseLocalDateTime(strDate).toDate();
    }

    /**
     * 根据周数，获取开始日期、结束日期
     *
     * @param week 周期  0本周，-1上周，-2上上周，1下周，2下下周
     * @return 返回date[0]开始日期、date[1]结束日期
     */
    public static Date[] getWeekStartAndEnd(int week) {
        DateTime dateTime = new DateTime();
        LocalDate date = new LocalDate(dateTime.plusWeeks(week));

        date = date.dayOfWeek().withMinimumValue();
        Date beginDate = date.toDate();
        Date endDate = date.plusDays(6).toDate();
        return new Date[]{beginDate, endDate};
    }

    /**
     * 对日期的【秒】进行加/减
     *
     * @param date    日期
     * @param seconds 秒数，负数为减
     * @return 加/减几秒后的日期
     */
    public static Date addDateSeconds(Date date, int seconds) {
        DateTime dateTime = new DateTime(date);
        return dateTime.plusSeconds(seconds).toDate();
    }

    /**
     * 对日期的【分钟】进行加/减
     *
     * @param date    日期
     * @param minutes 分钟数，负数为减
     * @return 加/减几分钟后的日期
     */
    public static Date addDateMinutes(Date date, int minutes) {
        DateTime dateTime = new DateTime(date);
        return dateTime.plusMinutes(minutes).toDate();
    }

    /**
     * 对日期的【小时】进行加/减
     *
     * @param date  日期
     * @param hours 小时数，负数为减
     * @return 加/减几小时后的日期
     */
    public static Date addDateHours(Date date, int hours) {
        DateTime dateTime = new DateTime(date);
        return dateTime.plusHours(hours).toDate();
    }

    /**
     * 对日期的【天】进行加/减
     *
     * @param date 日期
     * @param days 天数，负数为减
     * @return 加/减几天后的日期
     */
    public static Date addDateDays(Date date, int days) {
        DateTime dateTime = new DateTime(date);
        return dateTime.plusDays(days).toDate();
    }

    /**
     * 对日期的【周】进行加/减
     *
     * @param date  日期
     * @param weeks 周数，负数为减
     * @return 加/减几周后的日期
     */
    public static Date addDateWeeks(Date date, int weeks) {
        DateTime dateTime = new DateTime(date);
        return dateTime.plusWeeks(weeks).toDate();
    }

    /**
     * 对日期的【月】进行加/减
     *
     * @param date   日期
     * @param months 月数，负数为减
     * @return 加/减几月后的日期
     */
    public static Date addDateMonths(Date date, int months) {
        DateTime dateTime = new DateTime(date);
        return dateTime.plusMonths(months).toDate();
    }

    /**
     * 对日期的【年】进行加/减
     *
     * @param date  日期
     * @param years 年数，负数为减
     * @return 加/减几年后的日期
     */
    public static Date addDateYears(Date date, int years) {
        DateTime dateTime = new DateTime(date);
        return dateTime.plusYears(years).toDate();
    }

    /**
     * 比较日期的年月日大小，date1小于date2返回-1， date1大于date2返回1， date1等于date2返回0
     *
     * @param date1
     * @param date2
     * @return
     * @throws ParseException
     */
    public static int compareDay(Date date1, Date date2) throws ParseException {
        DateFormat df = new SimpleDateFormat(DATE_PATTERN);
        date1 = df.parse(df.format(date1));
        date2 = df.parse(df.format(date2));
        if (date1.getTime() < date2.getTime()) {
            return -1;
        } else if (date1.getTime() > date2.getTime()) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * 获取月的第一天和最后一天
     *
     * @param i 0为本月，1为下月，-1为上月
     * @return
     */
    public static Date[] getMonthStartAndEnd(int i) {
        DateTime dateTime = new DateTime();
        LocalDate date = new LocalDate(dateTime.plusMonths(i));
        Date beginDate = date.dayOfMonth().withMinimumValue().toDate();
        Date endDate = date.dayOfMonth().withMaximumValue().toDate();
        return new Date[]{beginDate, endDate};
    }

    /**
     * 获取一个月的日期字符集合
     *
     * @param i 0为本月，1为下月，-1为上月
     * @return
     */
    public static List<String> getDaysOfMonth(int i) {
        List<String> dayOfMonthList = new ArrayList<>();
        DateTime dateTime = new DateTime();
        LocalDate date = new LocalDate(dateTime.plusMonths(i));
        Date beginDate = date.dayOfMonth().withMinimumValue().toDate();
        Date endDate = date.dayOfMonth().withMaximumValue().toDate();
        int dd = Integer.parseInt(format(endDate, "dd"));
        for (int j = 0; j < dd; j++) {
            Date day = addDateDays(beginDate, j);
            dayOfMonthList.add(format(day, DATE_PATTERN));
        }
        return dayOfMonthList;
    }

    /**
     * 获取一个周的日期字符集合
     *
     * @param i 0为本周，1为下周，-1为上周
     * @return
     */
    public static List<String> getDaysOfWeek(int i) {
        List<String> dayOfWeekList = new ArrayList<>();
        DateTime dateTime = new DateTime();
        LocalDate date = new LocalDate(dateTime.plusMonths(i));
        Date beginDate = date.dayOfWeek().withMinimumValue().toDate();
        for (int j = 0; j < 7; j++) {
            Date day = addDateDays(beginDate, j);
            dayOfWeekList.add(format(day, DATE_PATTERN));
        }
        return dayOfWeekList;
    }

    /**
     * 获取一年的月份字符集合
     *
     * @param i 0为本年，1为下年，-1为去年
     * @return
     */
    public static List<String> getMonthsOfYear(int i) {
        List<String> monsOfYearList = new ArrayList<>();
        DateTime dateTime = new DateTime();
        LocalDate date = new LocalDate(dateTime.plusYears(i));
        Date beginDate = date.dayOfYear().withMinimumValue().toDate();
        for (int j = 0; j < 12; j++) {
            Date day = addDateMonths(beginDate, j);
            monsOfYearList.add(format(day, "yyyy-MM"));
        }
        return monsOfYearList;
    }

    /**
     * 获取自定义时间区间的日期字符串集合
     * @param startDate
     * @param endDate
     * @return
     */
    public static List<String> getDaysOfCustom(Date startDate, Date endDate) {
        if (startDate == null || endDate == null) {
            throw new RRException("时间参数异常");
        }
        List<String> dayOfWeekList = new ArrayList<>();

        Date day = startDate;
        for (int j = 0; day.getTime() < endDate.getTime(); j++) {
            day = addDateDays(startDate, j);
            dayOfWeekList.add(format(day, DATE_PATTERN));
        }
        return dayOfWeekList;
    }
    
    
    /**
    * 计算两个日期间隔天数（逻辑日历的天数）
    * @param localDateTime
    * @return
    */
	public static int logicDaysBetweenDate(Date startDate, Date endDate) {
		java.time.LocalDate localStartDate = dateToLocalDate(startDate);
		java.time.LocalDate localEndDate = dateToLocalDate(endDate);
		java.time.Period period = java.time.Period.between(localStartDate, localEndDate);
		return period.getDays();
	}
	
	
	/**
	* 计算两个日期间隔天数（24个小时的日期，（持续时间），TODO 可以使用Duration类：）
	*/
	public static long timeDaysBetweenDate(Date startDate, Date endDate) {
		java.time.LocalDateTime localStartDate = dateToLocalDateTime(startDate);
		java.time.LocalDateTime localEndDate = dateToLocalDateTime(endDate);
		Duration duration = Duration.between(localStartDate, localEndDate);
		return duration.toDays();
	}
	
	
	/**
	* 将LocalDate 转换成 Date
	* @param localDate
	* @return
	*/
	public static Date localDateToDate(java.time.LocalDate localDate) {
		ZoneId zoneId = ZoneId.systemDefault();
		ZonedDateTime zdt = localDate.atStartOfDay(zoneId);
		return Date.from(zdt.toInstant());
	}
    
	/**
	* 将 Date 转换成LocalDate
	* atZone()方法返回在指定时区从此Instant生成的ZonedDateTime。
	* @param date
	* @return
	*/
	public static java.time.LocalDate dateToLocalDate(Date date) {
		Instant instant = date.toInstant();
		ZoneId zoneId = ZoneId.systemDefault();
		return instant.atZone(zoneId).toLocalDate();
	}
	
	/**
	* 将LocalDateTime 转换成 Date
	* @param localDateTime
	* @return
	*/
	public static Date localDateTimeToDate(LocalDateTime localDateTime) {
		ZoneId zoneId = ZoneId.systemDefault();
		ZonedDateTime zdt = localDateTime.atZone(zoneId);
		return Date.from(zdt.toInstant());
	}
	
	/**
	* 将 Date 转换成LocalDateTime
	* atZone()方法返回在指定时区从此Instant生成的ZonedDateTime。
	* @param date
	* @return
	*/
	public static LocalDateTime dateToLocalDateTime(Date date) {
		Instant instant = date.toInstant();
		ZoneId zoneId = ZoneId.systemDefault();
		return instant.atZone(zoneId).toLocalDateTime();
	}
	
}
