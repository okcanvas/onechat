package io.bizbee.onechat.server.util;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class DateTimeUtils extends DateUtils {

  public static final String FULL_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
  public static final String FULL_DATE_FORMAT_CN = "yyyy년MM월dd일 HH시mm분ss초";
  public static final String PART_DATE_FORMAT = "yyyy-MM-dd";
  public static final String PART_DATE_FORMAT_TWO = "yyyy/MM/dd";
  public static final String PART_DATE_FORMAT_CN = "yyyy년MM월dd일";
  public static final String PARTDATEFORMAT = "yyyyMMdd";
  public static final String YEAR_DATE_FORMAT = "yyyy";
  public static final String MONTH_DATE_FORMAT = "MM";
  public static final String DAY_DATE_FORMAT = "dd";
  public static final String WEEK_DATE_FORMAT = "week";

  private final static int[] dayArr = new int[]{20, 19, 21, 20, 21, 22, 23, 23, 23, 24, 23, 22};
  private final static String[] constellationArr = new String[]{"염소자리", "물병자리", "물고기자리",
      "양자리", "황소자리", "쌍둥이자리", "게자리",
      "사자자리", "처녀자리", "저울자리", "전갈자리", "사수자리", "염소자리"};

  /**
   * @param date    
   * @param xFormat 
   * @return
   */
  public static String getFormatDate(Date date, String xFormat) {
    date = date == null ? new Date() : date;
    xFormat = StringUtils.isNotEmpty(xFormat) == true ? xFormat : FULL_DATE_FORMAT;
    SimpleDateFormat sdf = new SimpleDateFormat(xFormat);
    return sdf.format(date);
  }

  /**
   * @param dateX
   * @param dateY
   * @return x < y return [-1]; x = y return [0] ; x > y return [1] ;
   */
  public static int compareDate(Date dateX, Date dateY) {
    return dateX.compareTo(dateY);
  }

  /**
   * @param xDate
   * @param xFormat 
   * @return
   */
  public static Date parseString2Date(String xDate, String xFormat) {
    while (!isNotDate(xDate, xFormat)) {
      xFormat = StringUtils.isEmpty(xFormat) == true ? PART_DATE_FORMAT : xFormat;
      SimpleDateFormat sdf = new SimpleDateFormat(xFormat);
      Date date = null;
      try {
        date = sdf.parse(xDate);
      } catch (ParseException e) {
        e.printStackTrace();
        return null;
      }
      return date;
    }
    return null;
  }

  /**
   * @param xDate
   * @return
   */
  public static boolean isNotDate(String xDate, String format) {
    SimpleDateFormat sdf;
    try {
      if (StringUtils.isEmpty(format)) {
        sdf = new SimpleDateFormat(PART_DATE_FORMAT);
      } else {
        sdf = new SimpleDateFormat(format);
      }
      if (StringUtils.isEmpty(xDate)) {
        return true;
      }
      sdf.parse(xDate);
      return false;
    } catch (ParseException e) {
      e.printStackTrace();
      return true;
    }
  }

  public static boolean isDate(String xDate) {
    return !isDate(xDate);
  }

  /**
   * @param dateX
   * @param dateY
   * @return
   */
  public static int getDiffDays(Date dateX, Date dateY) {
    if ((dateX == null) || (dateY == null)) {
      return 0;
    }

    long dayX = dateX.getTime();
    long dayY = dateY.getTime();

    return dayX > dayY ? (int) ((dayX - dayY) / (60 * 60 * 1000 * 24)) : (int) ((dayY - dayX)
        / (60 * 60 * 1000 * 24));
  }

  /**
   * @param dateX
   * @param dateY
   * @return
   */
  public static int getDiffHours(Date dateX, Date dateY) {
    if ((dateX == null) || (dateY == null)) {
      return 0;
    }

    long dayX = dateX.getTime();
    long dayY = dateY.getTime();

    return dayX > dayY ? (int) ((dayX - dayY) / (60 * 60 * 1000))
        : (int) ((dayY - dayX) / (60 * 60 * 1000));
  }

  /**
   * @param dateX
   * @param dateY
   * @return
   */
  public static int getDiffMinute(Date dateX, Date dateY) {
    if ((dateX == null) || (dateY == null)) {
      return 0;
    }

    long dayX = dateX.getTime();
    long dayY = dateY.getTime();

    return dayX > dayY ? (int) ((dayX - dayY) / (60 * 1000)) : (int) ((dayY - dayX) / (60 * 1000));
  }

  /**
   * @param date    
   * @param after   
   * @param xFormat
   * @return
   */
  public static String getAfterCountDate(Date date, int after, String xFormat) {
    date = date == null ? new Date() : date;
    xFormat = StringUtils.isNotEmpty(xFormat) ? xFormat : PART_DATE_FORMAT;
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    calendar.add(Calendar.DAY_OF_MONTH, after);
    return getFormatDate(calendar.getTime(), xFormat);
  }

  /**
   * @param date    
   * @param xFormat 
   * @return
   */
  public static String getBeforeCountDate(Date date, int before, String xFormat) {
    date = date == null ? new Date() : date;
    xFormat = StringUtils.isNotEmpty(xFormat) == true ? xFormat : PART_DATE_FORMAT;
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    calendar.add(Calendar.DAY_OF_MONTH, -before);
    return getFormatDate(calendar.getTime(), xFormat);
  }

  /**
   * @param xDate   
   * @param xFormat 
   */
  public static int getDateTimeParam(Object xDate, String xFormat) {
    xDate = xDate == null ? new Date() : xDate;
    Date date = null;
    if (xDate instanceof String) {
      date = parseString2Date(xDate.toString(), null);
    } else if (xDate instanceof Date) {
      date = (Date) xDate;
    } else {
      date = new Date();
    }
    date = date == null ? new Date() : date;
    if (StringUtils.isNotEmpty(xFormat) && (xFormat.equals(YEAR_DATE_FORMAT) || xFormat.equals(
        MONTH_DATE_FORMAT)
        || xFormat.equals(DAY_DATE_FORMAT))) {
      return Integer.parseInt(getFormatDate(date, xFormat));
    } else if (StringUtils.isNotEmpty(xFormat) && (WEEK_DATE_FORMAT.equals(xFormat))) {
      Calendar cal = Calendar.getInstance();
      cal.setTime(date);
      int week = cal.get(Calendar.DAY_OF_WEEK) - 1 == 0 ? 7 : cal.get(Calendar.DAY_OF_WEEK) - 1;
      return week;
    } else {
      return 0;
    }
  }

  /**
   * @param time
   * @param format
   * @return
   */
  public static Long getLongTime(String time, String format) {
    SimpleDateFormat sdf = new SimpleDateFormat(format);
    Date date = null;
    try {
      date = sdf.parse(time);
      return (date.getTime() / 1000);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * @param xDate
   * @return
   */
  public static String getWeekString(Object xDate) {
    int week = getDateTimeParam(xDate, WEEK_DATE_FORMAT);
    switch (week) {
      case 1:
        return "월요일";
      case 2:
        return "화요일";
      case 3:
        return "수요일";
      case 4:
        return "목요일";
      case 5:
        return "금요일";
      case 6:
        return "토요일";
      case 7:
        return "일요일";
      default:
        return "";
    }
  }

  public static Long getTenBitTimestamp() {
    return System.currentTimeMillis() / 1000;
  }

  public static Date getDateEnd(Date date) {
    return new Date(date.getTime() + (86400 - 1) * 1000);
  }

  /**
   * @param time
   * @param format
   * @return
   */
  public static Long getLongDateTime(String time, String format) {
    SimpleDateFormat sdf = new SimpleDateFormat(format);
    Date date = null;
    try {
      date = sdf.parse(time);
      return date.getTime();
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return null;
  }

  public static Long getStartTimestamp(Date date) {

    Calendar calendar = Calendar.getInstance();
    date = date == null ? new Date() : date;
    calendar.setTime(date);

    calendar.set(Calendar.HOUR_OF_DAY, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MILLISECOND, 0);

    return calendar.getTime().getTime() / 1000;
  }

  public static Long getEndTimestamp(Date date) {

    Calendar calendar = Calendar.getInstance();
    date = date == null ? new Date() : date;
    calendar.setTime(date);

    calendar.set(Calendar.HOUR_OF_DAY, 23);
    calendar.set(Calendar.MINUTE, 59);
    calendar.set(Calendar.SECOND, 59);
    calendar.set(Calendar.MILLISECOND, 999);

    return calendar.getTime().getTime() / 1000;
  }

  /**
   * @param date
   * @return
   */
  public static Date getYesterday(Date date) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    calendar.add(Calendar.DAY_OF_MONTH, -1);

    calendar.set(Calendar.HOUR_OF_DAY, 9);
    calendar.set(Calendar.MINUTE, 59);
    calendar.set(Calendar.SECOND, 59);
    calendar.set(Calendar.MILLISECOND, 999);
    date = calendar.getTime();
    return date;
  }

  /**
   * @param date
   * @return
   */
  public static Date getTomorrowday(Date date) {
    Calendar c = Calendar.getInstance();
    c.setTime(date);
    c.add(Calendar.DAY_OF_YEAR, +1);
    return c.getTime();
  }

  /*
   * @param time
   * @return
   */
  public static String timestampToString(Integer time, String format) {
    long temp = (long) time * 1000;
    Timestamp ts = new Timestamp(temp);
    String tsStr = "";
    DateFormat dateFormat = new SimpleDateFormat(format);
    try {
      tsStr = dateFormat.format(ts);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return tsStr;
  }

  public static Date getStartTime(Date date) {

    Calendar calendar = Calendar.getInstance();
    date = date == null ? new Date() : date;
    calendar.setTime(date);

    calendar.set(Calendar.HOUR_OF_DAY, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MILLISECOND, 0);

    return calendar.getTime();
  }

  public static Date getEndTime(Date date) {

    Calendar calendar = Calendar.getInstance();
    date = date == null ? new Date() : date;
    calendar.setTime(date);

    calendar.set(Calendar.HOUR_OF_DAY, 23);
    calendar.set(Calendar.MINUTE, 59);
    calendar.set(Calendar.SECOND, 59);
    calendar.set(Calendar.MILLISECOND, 999);

    return calendar.getTime();
  }

  /**
   * @param time
   * @return
   */
  public static Integer DateToTimestamp(Date time) {
    Timestamp ts = new Timestamp(time.getTime());

    return (int) ((ts.getTime()) / 1000);
  }

  /**
   * @param minute
   * @return
   */
  public static String getMinuteToString(int minute, Date time) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(time);
    calendar.add(Calendar.MINUTE, minute);
    return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTime());
  }

  /**
   * @param minute
   * @return
   */
  public static Date getMinuteToTime(int minute, Date time) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(time);
    calendar.add(Calendar.MINUTE, minute);
    return calendar.getTime();
  }

  public static String timestampToString(Timestamp timestamp, String format) {
    return new SimpleDateFormat(format).format(timestamp);
  }

  public static String timestampDefaultFormat(Timestamp timestamp) {
    if (timestamp == null) {
      return null;
    }
    return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(timestamp);
  }

  public static String dateDefaultFormat(Date date) {
    if (date == null) {
      return null;
    }
    return getFormatDate(date, "yyyy-MM-dd HH:mm:ss");
  }

  public static Date getTimeByDay(int day) {
    return getTimeByDay(new Date(), day);
  }

  public static Date getTimeByDay(Date d1, int day) {
    long temp1 = d1.getTime();
    temp1 = temp1 + day * 0x5265c00L;
    return (new Date(temp1));
  }

  public static long getMinuteSpace(Date date1, Date date2) {
    Calendar calendar1 = Calendar.getInstance();
    Calendar calendar2 = Calendar.getInstance();
    calendar1.setTime(date1);
    calendar2.setTime(date2);
    long milliseconds1 = calendar1.getTimeInMillis();
    long milliseconds2 = calendar2.getTimeInMillis();
    long diff = milliseconds2 - milliseconds1;
    long diffDays = diff / (60 * 1000);
    return Math.abs(diffDays);
  }

  public static Integer getHourOfDate(Date date) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    return cal.get(Calendar.HOUR_OF_DAY);
  }

  public static Integer getDayOfMonth(Date date) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    return cal.get(Calendar.DAY_OF_MONTH);
  }

  /**
   * @param date1
   * @param date2
   * @return
   */
  public static long getDateSpace(Date date1, Date date2) {
    Calendar calendar1 = Calendar.getInstance();
    Calendar calendar2 = Calendar.getInstance();
    calendar1.setTime(date1);
    calendar2.setTime(date2);
    long milliseconds1 = calendar1.getTimeInMillis();
    long milliseconds2 = calendar2.getTimeInMillis();
    long diff = milliseconds2 - milliseconds1;
    long diffDays = diff / (24 * 60 * 60 * 1000);
    return Math.abs(diffDays);
  }

  public static int getMiao(int num) {
    Calendar curDate = Calendar.getInstance();
    Calendar tommorowDate = new GregorianCalendar(curDate
        .get(Calendar.YEAR), curDate.get(Calendar.MONTH), curDate.get(Calendar.DATE) + 1, num, 0,
        0);
    return (int) (tommorowDate.getTimeInMillis() - curDate.getTimeInMillis()) / 1000;
  }

  public static long secondsDifferen(Date d1, Date d2) {
    long temp1 = d1.getTime();
    long temp2 = d2.getTime();
    return (long) ((temp2 - temp1) / 1000);
  }

  public static Date getTimeByMonth(int month) {
    return getTimeByMonth(new Date(), month);
  }

  public static Date getTimeByMonth(Date m1, int month) {
    Calendar c = Calendar.getInstance();
    c.setTime(m1);
    c.add(Calendar.MONTH, month);
    Date m = c.getTime();
    return m;
  }

  public static int dayConvertSecond(int day) {
    int secondOfDay = 60 * 60 * 24;
    return day * secondOfDay;
  }

  /**
   * @param ymd
   * @return
   */
  public static boolean checkYearMonthDay(String ymd) {
    if (ymd == null || ymd.length() == 0) {
      return false;
    }
    String s = ymd.replaceAll("[/\\- ]", "/");
    SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
    try {
      Date date = format.parse(s);
      return true;
    } catch (ParseException e) {
      return false;
    }
  }

  /**
   * @param birthday
   * @return
   */
  public static String getConstellation(String birthday) {
    int month = 0;
    int day = 0;
    Date birthdayDateTime = DateTimeUtils.parseString2Date(birthday, "yyyy-MM-dd");
    if (null != birthdayDateTime) {
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(birthdayDateTime);
      month = calendar.get(Calendar.MONTH) + 1;
      day = calendar.get(Calendar.DAY_OF_MONTH);
    }
    return day < dayArr[month - 1] ? constellationArr[month - 1] : constellationArr[month];
  }

  /**
   * @param year
   * @return
   */
  public static String getYear(int year) {
    if (year < 1900) {
      return "unknown";
    }
    int start = 1900;
    String[] years = new String[]{"鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊", "猴", "鸡", "狗", "猪"};
    return years[(year - start) % years.length];
  }

  /**
   * @param s1 
   * @param s2
   * @return boolean true  false 
   */
  public static boolean compareVehicle(String s1, String s2) {
    boolean flag = true;
    SimpleDateFormat sf = new SimpleDateFormat("HH:mm");
    try {
      Date date1 = sf.parse(s1);
      Date date2 = sf.parse(s2);
      if (date1.getTime() > date2.getTime()) {
        flag = true;
      } else {
        flag = false;
      }
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return flag;
  }

  /**
   * @param date
   * @return
   */
  public static String getEndOfDay(Date date) {
    Calendar calendarEnd = Calendar.getInstance();
    calendarEnd.setTime(date);
    calendarEnd.set(Calendar.HOUR_OF_DAY, 23);
    calendarEnd.set(Calendar.MINUTE, 59);
    calendarEnd.set(Calendar.SECOND, 59);
    calendarEnd.set(Calendar.MILLISECOND, 0);

    return dateDefaultFormat(calendarEnd.getTime());
  }

  /**
   * @param date
   * @return
   */
  public static String getFirstOfDay(Date date) {
    Calendar calendarStart = Calendar.getInstance();
    calendarStart.setTime(date);
    calendarStart.set(Calendar.HOUR_OF_DAY, 0);
    calendarStart.set(Calendar.MINUTE, 0);
    calendarStart.set(Calendar.SECOND, 0);
    calendarStart.set(Calendar.MILLISECOND, 0);
    return dateDefaultFormat(calendarStart.getTime());
  }

  /**
   * @param str1  1 -01-01 12:00:00
   * @param str2  2 -01-01 12:00:00
   * @return long[] {天, 时, 分, 秒}
   */
  public static long[] getDistanceTimes(Date str1, Date str2) {
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Date one = str1;
    Date two = str2;
    long day = 0;
    long hour = 0;
    long min = 0;
    long sec = 0;
    long time1 = one.getTime();
    long time2 = two.getTime();
    long diff;
    if (time1 < time2) {
      diff = time2 - time1;
    } else {
      diff = time1 - time2;
    }
    day = diff / (24 * 60 * 60 * 1000);
    hour = (diff / (60 * 60 * 1000) - day * 24);
    min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
    sec = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
    long[] times = {day, hour, min, sec};
    return times;
  }

  /**
   * 获取多少秒之后的日期
   *
   * @param date
   * @param second
   * @return
   */
  public static Date getLastDateBySecond(Date date, Integer second) {
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 24小时制
    // SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");//12小时制
    if (date == null) {
      return null;
    }
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    cal.add(Calendar.SECOND, second);// 24小时制
    date = cal.getTime();
    cal = null;
    return date;
  }

  /**
   * 根据年龄获取年份
   *
   * @param age
   * @return
   */
  public static String getCalculateAge(int age) {
    String toDay = DateTimeUtils.getFormatDate(new Date(), DateTimeUtils.YEAR_DATE_FORMAT);
    if (age > Integer.valueOf(toDay)) {
      return null;
    }
    Integer year = Integer.valueOf(toDay) - age;
    return year.toString();
  }

  /**
   * 获取N天前的字符串格式日期
   *
   * @param format 如:yyyy-MM-dd
   * @param nDay
   * @return
   */
  public static String getNDayAgoByDay(String format, Integer nDay) {
    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat sdf = new SimpleDateFormat(format);
    calendar.add(Calendar.DATE, nDay);
    String ndaysAgo = sdf.format(calendar.getTime());
    return ndaysAgo;
  }

  /**
   * 获取时间差，不考虑时分秒
   *
   * @param dateBefore
   * @param dateAfter
   * @return int 1、昨天 2、近7天 3、近15天 4、近30天
   */
  public static Integer getTimeScope(Date dateBefore, Date dateAfter) {
    int daysDifferent = compareDays(dateBefore, dateAfter);// 1、昨天 2、近7天 3、近15天 4、近30天
    if (daysDifferent == 1) {
      return 1;
    } else if (daysDifferent >= 2 && daysDifferent <= 7) {
      return 2;
    } else if (daysDifferent >= 8 && daysDifferent <= 15) {
      return 3;
    }
    return 4;
  }

  public static int compareDays(Date datebefore, Date dateAfter) {
    Calendar calendar1 = Calendar.getInstance();
    Calendar calendar2 = Calendar.getInstance();
    calendar1.setTime(datebefore);
    calendar2.setTime(dateAfter);
    int day1 = calendar1.get(Calendar.DAY_OF_YEAR);
    int day2 = calendar2.get(Calendar.DAY_OF_YEAR);
    int year1 = calendar1.get(Calendar.YEAR);
    int year2 = calendar2.get(Calendar.YEAR);
    if (year1 > year2) {
      int tempyear = year1;
      int tempday = day1;
      day1 = day2;
      day2 = tempday;
      year1 = year2;
      year2 = tempyear;
    }
    if (year1 == year2) {
      return day2 - day1;
    } else {
      int DayCount = 0;
      for (int i = year1; i < year2; i++) {
        if (i % 4 == 0 && i % 100 != 0 || i % 400 == 0) {
          DayCount += 366;
        } else {
          DayCount += 365;
        }
      }
      return DayCount + (day2 - day1);
    }
  }

  public static Date asDate(LocalDateTime localDateTime) {
    return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
  }

  /**
   * 小于10 追加0
   *
   * @param obj
   * @return
   */
  public static String appendZero(Integer obj) {
    if (obj < 10) {
      return '0' + String.valueOf(obj);
    } else {
      return String.valueOf(obj);
    }
  }

  /*
   * 功能描述: <br> 获取时间与当前时间相差分钟数
   * @param: time
   * @Return: int
   * @Author: 97342
   * @Date: 2020/8/22 12:40
   */
  public static int getMinuteByNow(Date time) throws Exception {
    Date date = new Date();
    long millsecond = date.getTime() - time.getTime();
    if (millsecond < 0) {
      throw new Exception("时间超前异常");
    }
    int minute = (int) millsecond / 60000;
    return minute;
  }

  /*
   * 功能描述: <br> 时间戳转LocalDateTime
   * @param: time
   * @Return: java.time.LocalDateTime
   * @Author: 97342
   * @Date: 2020/8/27 16:17
   */
  public static LocalDateTime longToLocalDateTime(Long time) {
    LocalDateTime localDateTime = new Date(time).toInstant().atOffset(ZoneOffset.of("+08:00"))
        .toLocalDateTime();
    return localDateTime;
  }

  /**
   * 获取 一天的开始时间
   *
   * @param localDateTime
   * @return java.time.LocalDateTime
   * @author BNMZY
   */
  public static LocalDateTime getTimeBegin(LocalDateTime localDateTime) {
    String format = DateUtil.formatLocalDateTime(localDateTime);
    Date date = DateUtil.parse(format);
    DateTime dateTime = DateUtil.beginOfDay(date);
    return DateUtil.toLocalDateTime(dateTime);
  }

  /**
   * 获取 一天结束的时间
   *
   * @param localDateTime
   * @return java.time.LocalDateTime
   * @author BNMZY
   */
  public static LocalDateTime getTimeEnd(LocalDateTime localDateTime) {
    String format = DateUtil.formatLocalDateTime(localDateTime);
    Date date = DateUtil.parse(format);
    DateTime dateTime = DateUtil.endOfDay(date);
    return DateUtil.toLocalDateTime(dateTime);
  }

  /**
   * 指定日期所在月的第一天
   *
   * @param localDateTime
   * @return
   */
  public static LocalDateTime getMonthStart(LocalDateTime localDateTime) {
    String format = DateUtil.formatLocalDateTime(localDateTime);
    Date date = DateUtil.parse(format);
    DateTime dateTime = DateUtil.beginOfMonth(date);
    return DateUtil.toLocalDateTime(dateTime);
  }

  /**
   * 功能描述: <br> LocalDateTime 转String 格式 yyyy-MM-dd HH:mm:ss
   *
   * @param: time
   * @Return: java.lang.String
   * @Author: 97342
   * @Date: 2020/9/21 15:11
   */
  public static String LocalDateTimeToString(LocalDateTime time) {

    String format = time.format(DateTimeFormatter.ofPattern(FULL_DATE_FORMAT));
    return format;

  }

  /**
   * 获取上周一的日期
   *
   * @param localDateTime
   * @return
   */
  public static LocalDateTime getLastWeekMonday(LocalDateTime localDateTime) {
    String format = DateUtil.formatLocalDateTime(localDateTime);
    Date date = DateUtil.parse(format);
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    cal.add(Calendar.DAY_OF_WEEK, -7);
    date = cal.getTime();
    DateTime dateTime = DateUtil.beginOfWeek(date);
    return DateUtil.toLocalDateTime(dateTime);
  }

  /**
   * 获取本周一的日期
   *
   * @param localDateTime
   * @return
   */
  public static LocalDateTime getThisWeekMonday(LocalDateTime localDateTime) {
    String format = DateUtil.formatLocalDateTime(localDateTime);
    Date date = DateUtil.parse(format);
    DateTime dateTime = DateUtil.beginOfWeek(date);
    return DateUtil.toLocalDateTime(dateTime);
  }

  /**
   * 获取上周最后一天的日期
   *
   * @param localDateTime
   * @return
   */
  public static LocalDateTime getLastWeekSunday(LocalDateTime localDateTime) {
    String format = DateUtil.formatLocalDateTime(localDateTime);
    Date date = DateUtil.parse(format);
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    cal.add(Calendar.DAY_OF_WEEK, -7);
    date = cal.getTime();
    DateTime dateTime = DateUtil.endOfWeek(date);
    return DateUtil.toLocalDateTime(dateTime);
  }

  /**
   * 获取本月第一天的日期
   *
   * @return
   */
  public static LocalDateTime getFirstDayOfMonth(LocalDateTime localDateTime) {
    String format = DateUtil.formatLocalDateTime(localDateTime);
    Date date = DateUtil.parse(format);
    DateTime dateTime = DateUtil.beginOfMonth(date);
    return DateUtil.toLocalDateTime(dateTime);
  }

  /**
   * 获取上月第一天的日期
   *
   * @return
   */
  public static LocalDateTime getFirstDayLastMonth(LocalDateTime localDateTime) {
    String format = DateUtil.formatLocalDateTime(localDateTime);
    Date date = DateUtil.parse(format);
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    cal.add(Calendar.MONTH, -1);
    date = cal.getTime();
    DateTime dateTime = DateUtil.beginOfMonth(date);
    return DateUtil.toLocalDateTime(dateTime);
  }

  /**
   * 获取上月最后一天的日期
   *
   * @return
   */
  public static LocalDateTime getLastDayLastMonth(LocalDateTime localDateTime) {
    String format = DateUtil.formatLocalDateTime(localDateTime);
    Date date = DateUtil.parse(format);
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    cal.add(Calendar.MONTH, -1);
    date = cal.getTime();
    DateTime dateTime = DateUtil.endOfMonth(date);
    return DateUtil.toLocalDateTime(dateTime);
  }

  /**
   * @param startDay 开始日期
   * @param endDay   结束日期
   * @return 返回包含开始结束日期的集合
   */
  public static List<LocalDate> getDays(LocalDate startDay, LocalDate endDay) {
    List<LocalDate> days = new ArrayList<>();
    LocalDate start = ObjectUtil.clone(startDay);
    LocalDate end = ObjectUtil.clone(endDay);
    days.add(start);
    while (start.isBefore(end)) {
      start = start.plusDays(1L);
      days.add(start);
    }
    Collections.reverse(days);
    return days;
  }

}
