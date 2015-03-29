package nomouse.android.core.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间工具类 线程安全
 *
 * @author nomouse
 */
public class TimeUtils {


    /**
     * 将字符串转换时间格式，如果转换失败返回null
     *
     * @param sdf
     * @param time
     * @return
     */
    public static Date parseDate(SimpleDateFormat sdf, String time) {
        Date result = null;
        if (StringUtils.isNotEmpty(time)) {
            try {
                result = sdf.parse(time);
            } catch (ParseException e) {
            }
        }
        return result;
    }


    /**
     * 返回当前时间
     *
     * @return
     */
    public static long getTime() {
        return System.currentTimeMillis();
    }

    /**
     * 返回当前时间
     *
     * @return
     */
    public static Timestamp getTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }

    /**
     * 传入年月日，返回当天0点的毫秒数
     *
     * @param year
     * @param month 1-12
     * @param day
     * @return
     */
    public static long getTimeDayBegin(int year, int month, int day) {
        Calendar calendar = calendarThreadLocal.get();
        return setDate(calendar, year, month, day);
    }

    /**
     * 传入时间，返回当天0点的毫秒数
     *
     * @param time
     * @return 时间戳
     */
    public static long getTimeDayBegin(long time) {
        Calendar calendar = calendarThreadLocal.get();
        calendar.setTimeInMillis(time);
        setTime(calendar, 0, 0, 0, 0);
        return calendar.getTimeInMillis();
    }

    /**
     * 返回当天0点的毫秒数
     *
     * @return 时间戳
     */
    public static long getTimeDayBegin() {
        Calendar calendar = calendarThreadLocal.get();
        calendar.setTimeInMillis(System.currentTimeMillis());
        setTime(calendar, 0, 0, 0, 0);
        return calendar.getTimeInMillis();
    }

    /**
     * 传入时间，返回第二天0点的毫秒数
     *
     * @param time
     * @return
     */
    public static long getTimeDayEnd(long time) {
        Calendar calendar = calendarThreadLocal.get();
        calendar.setTimeInMillis(time);
        setTime(calendar, 0, 0, 0, 0);
        addDays(calendar, 1);
        return calendar.getTimeInMillis();
    }

    /**
     * 返回第二天0点的毫秒数
     *
     * @return 时间戳
     */
    public static long getTimeDayEnd() {
        Calendar calendar = calendarThreadLocal.get();
        calendar.setTimeInMillis(System.currentTimeMillis());
        setTime(calendar, 0, 0, 0, 0);
        addDays(calendar, 1);
        return calendar.getTimeInMillis();
    }


    /**
     * 传入时间和增加的天数，返回计算后的时间
     *
     * @param time 开始时间
     * @param days 增加的天数，负数代表减
     * @return 时间戳
     */
    public static long getTimeDayAdded(long time, int days) {
        Calendar calendar = calendarThreadLocal.get();
        calendar.setTimeInMillis(time);
        calendar.add(Calendar.DAY_OF_YEAR, days);
        return calendar.getTimeInMillis();
    }

    /**
     * 返回差几天
     *
     * @param time1 开始时间
     * @param time2 结束时间
     * @return 时间戳
     */
    public static int getDayDiff(long time1, long time2) {
        return (int) ((time2 - time1) / 1000 / DAY_SECONDS);
    }

    private static long setDate(Calendar calendar, int year, int month, int day) {
        calendar.clear();
        calendar.set(year, month - 1, day);
        return calendar.getTimeInMillis();
    }

    private static long setTime(Calendar calendar, int hourOfDay, int minute, int second, int millisecond) {
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);
        calendar.set(Calendar.MILLISECOND, millisecond);
        return calendar.getTimeInMillis();
    }

    private static void addDays(Calendar calendar, int days) {
        calendar.add(Calendar.DAY_OF_YEAR, days);
    }


    public static void main(String[] args) {
        // Date now = new Date();
        // long nowTime = now.getTime();
        //
        // System.out.println("当前时间：" + DATETIME_TO_MINUTE.format(now));
        // System.out.println("当前：" + getTimeForNewsFeed(nowTime));
        // System.out.println("昨天此时："
        // + getTimeForNewsFeed(nowTime - DAY_SECONDS * 1000));
        // System.out.println("12小时前："
        // + getTimeForNewsFeed(nowTime - DAY_SECONDS / 2 * 1000));
        // System.out.println("前天此时："
        // + getTimeForNewsFeed(nowTime - DAY_SECONDS * 2 * 1000));
        // System.out.println("三天前此时："
        // + getTimeForNewsFeed(nowTime - DAY_SECONDS * 3 * 1000));
        // System.out.println("一周前此时："
        // + getTimeForNewsFeed(nowTime - DAY_SECONDS * 7 * 1000));

        System.out.println(getRemainHours(60));
    }

    /**
     * 给定截止日期，返回剩余期限
     *
     * @param nowtime 当前时间
     * @param endtime 任务截止时间
     * @return
     */
    public static String getRemainDays(Date nowtime, long endtime) {

        String result;
        // 当前时间
        long nowTime = nowtime.getTime();

        // 时间差
        long difference = (nowTime - endtime) / 1000;

        if (difference >= 0) {
            result = "任务已过期";
        } else {
            long dbNum = -(difference / DAY_SECONDS);
            if (dbNum != 0) {
                result = "还剩" + (dbNum) + "天到期";
            } else {
                result = "今天到期";
            }

        }
        return result;
    }

    /**
     * 获取距离现在的时间
     *
     * @param date
     * @return
     */
    public static String getTimeRemain(Date date) {
        return getTimeRemain(date == null ? 0 : date.getTime());
    }

    /**
     * 获取距离现在的时间
     *
     * @param time
     * @return
     */
    public static String getTimeRemain(long time) {

        String result;
        if (time == 0) {
            result = UNKNOWN;
        } else {
            // 当前时间
            long currentTime = new Date().getTime();

            // 时间差
            long difference = (currentTime - time) / 1000;

            if (difference < 0) {
                result = UNKNOWN;
            } else if (difference < MINUTE_SECONDS) {
                result = "刚刚";
            } else if (difference < HOUR_SECONDS) {
                result = (difference / MINUTE_SECONDS) + "分钟前";
            } else if (difference < DAY_SECONDS) {
                result = (difference / HOUR_SECONDS) + "小时前";
            } else if (difference < THREE_DAY_SECONDS) {

                long dbNum = difference / DAY_SECONDS;
                // if (dbNum == 1) {
                // result = "昨天";
                // } else if (dbNum == 2) {
                // result = "前天";
                // } else {
                // result = dbNum + "天前";
                // }
                if (dbNum > 0) {
                    result = dbNum + "天前";
                } else {
                    result = UNKNOWN;
                }
            } else if (difference >= THREE_DAY_SECONDS) {
                result = DATE_TO_DAY.format(new Date(time));
            } else {
                result = UNKNOWN;
            }
        }

        return result;
    }

    public static String getTimeFreindly(Date date) {
        return getTimeFreindly(date == null ? 0 : date.getTime());
    }

    /**
     * 获得时间提示
     *
     * @param time
     * @return
     */
    public static String getTimeFreindly(long time) {
        String result;
        if (time == 0) {
            result = UNKNOWN;
        } else {
            Date targetDate = new Date(time);

            // 今天0点的时间
            Calendar c = Calendar.getInstance();
            c.set(Calendar.HOUR_OF_DAY, 0);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
            long firstTimeOfToday = c.getTimeInMillis();

            // 时间差
            long difference = (firstTimeOfToday - time) / 1000;

            if (difference <= 0) {
                // 今天
                result = getTimeName(time) + TIME_TO_MINITE.format(targetDate);
            } else if (difference < DAY_SECONDS) {
                // 昨天
                result = ONE_DAY_BEFORE + getTimeName(time);
            } else if (difference < TWO_DAY_SECONDS) {
                // 前天
                result = TWO_DAY_BEFORE + getTimeName(time);
            } else {
                // 本周第一天0点的时间
                c.set(Calendar.DAY_OF_WEEK, 1);
                long firstTimeOfWeek = c.getTimeInMillis();

                difference = (firstTimeOfWeek - time) / 1000;

                if (difference <= 0) {
                    // 本周
                    result = getWeekday(time) + " " + getTimeName(time);
                } else {
                    // 本年第一天0点的时间
                    c.set(Calendar.DAY_OF_YEAR, 1);
                    long firstTimeOfYear = c.getTimeInMillis();

                    difference = (firstTimeOfYear - time) / 1000;

                    if (difference <= 0) {
                        // 在本年内
                        result = DATE_MONTH_DAY.format(targetDate);
                    } else {
                        // 不在本年内
                        result = DATE_YEAR_MONTH_DAY.format(targetDate);
                    }
                }
            }
        }

        return result;
    }

    public static String getTimeForChat(Date date) {
        return getTimeForChat(date == null ? 0 : date.getTime());
    }

    /**
     * 获取聊天时间
     *
     * @param time
     * @return
     */
    public static String getTimeForChat(long time) {

        String result;
        if (time == 0) {
            result = UNKNOWN;
        } else {
            Date targetDate = new Date(time);

            // 今天0点的时间
            Calendar c = Calendar.getInstance();
            c.set(Calendar.HOUR_OF_DAY, 0);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
            long firstTimeOfToday = c.getTimeInMillis();

            // 时间差
            long difference = (firstTimeOfToday - time) / 1000;

            if (difference <= 0) {
                // 今天
                result = getTimeName(time) + TIME_TO_MINITE.format(targetDate);
            } else if (difference < DAY_SECONDS) {
                // 昨天
                result = ONE_DAY_BEFORE + getTimeName(time)
                        + TIME_TO_MINITE.format(targetDate);
            } else if (difference < TWO_DAY_SECONDS) {
                // 前天
                result = TWO_DAY_BEFORE + getTimeName(time)
                        + TIME_TO_MINITE.format(targetDate);
            } else {
                // 本周第一天0点的时间
                c.set(Calendar.DAY_OF_WEEK, 1);
                long firstTimeOfWeek = c.getTimeInMillis();

                difference = (firstTimeOfWeek - time) / 1000;

                if (difference <= 0) {
                    // 本周
                    result = getWeekday(time) + " " + getTimeName(time)
                            + TIME_TO_MINITE.format(targetDate);
                } else {
                    // 本年第一天0点的时间
                    c.set(Calendar.DAY_OF_YEAR, 1);
                    long firstTimeOfYear = c.getTimeInMillis();

                    difference = (firstTimeOfYear - time) / 1000;

                    if (difference <= 0) {
                        // 在本年内
                        result = DATE_MONTH_DAY.format(targetDate)
                                + getTimeName(time)
                                + TIME_TO_MINITE.format(targetDate);
                    } else {
                        // 不在本年内
                        result = DATE_YEAR_MONTH_DAY.format(targetDate)
                                + getTimeName(time)
                                + TIME_TO_MINITE.format(targetDate);
                    }
                }
            }
        }

        return result;
    }

    /**
     * 获取星期几
     *
     * @param time
     * @return
     */
    private static String getWeekday(long time) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) {
            w = 0;
        }
        return WEEKDAYS[w];
    }

    /**
     * 获取当前时间段
     *
     * @param time
     * @return
     */
    private static String getTimeName(long time) {
        String result = "";
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        int hourOfDay = cal.get(Calendar.HOUR_OF_DAY);
        if (hourOfDay >= 0 && hourOfDay < 6) {
            result = DAWN;
        } else if (hourOfDay >= 6 && hourOfDay < 12) {
            result = AM;
        } else if (hourOfDay >= 12 && hourOfDay < 18) {
            result = PM;
        } else if (hourOfDay >= 18 && hourOfDay < 0) {
            result = NIGHT;
        }
        return result;
    }

    public static String getRemainHours(long time) {
        if (time <= 0)
            return "";

        String result = "";
        long hour = 0;
        long minite = 0;
        hour = time / HOUR_SECONDS;

        long remainsecond = time;

        if (hour > 0) {
            remainsecond = time - hour * HOUR_SECONDS;
            result += hour + "小时";
        }
        if (remainsecond > 0) {
            minite = remainsecond / MINUTE_SECONDS;
            result += minite + "分钟";
            remainsecond = remainsecond - minite * MINUTE_SECONDS;
        }

        if (remainsecond > 0) {
            result += remainsecond + "秒";
        }

        return result;
    }

    public static final class DefaultCalendarThreadLocal extends ThreadLocal<Calendar> {
        @Override
        protected Calendar initialValue() {
            return Calendar.getInstance();
        }
    }

    private static ThreadLocal<Calendar> calendarThreadLocal = new DefaultCalendarThreadLocal();

    /**
     * 日期格式精简模式（20100909090909）
     */
    public static final SimpleDateFormat DATETIME_SIMPLIFY = new SimpleDateFormat(
            "yyyyMMddHHmmss");

    /**
     * 日期格式到秒-（2010/9/9 9:9:9）
     */
    public static final SimpleDateFormat DATETIME_TO_SECOND = new SimpleDateFormat(
            "yyyy/M/d H:m:s");

    /**
     * 日期格式到秒-（2010/9/9 09:09:09）
     */
    public static final SimpleDateFormat DATETIME_TO_SECOND2 = new SimpleDateFormat(
            "yyyy/M/d HH:mm:ss");

    /**
     * 日期格式到分-（2010/09/09 09:09）
     */
    public static final SimpleDateFormat DATETIME_TO_MINUTE = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm");

    /**
     * 日期格式月日-（9月9日）
     */
    public static final SimpleDateFormat DATE_MONTH_DAY = new SimpleDateFormat(
            "M月d日");

    /**
     * 日期格式年月日-（2004年9月9日）
     */
    public static final SimpleDateFormat DATE_YEAR_MONTH_DAY = new SimpleDateFormat(
            "yyyy年M月d日");

    /**
     * 日期格式到日期-（2010-09-09）
     */
    public static final SimpleDateFormat DATE_TO_DAY = new SimpleDateFormat(
            "yyyy-MM-dd");

    /**
     * 时间格式到分（9:09）
     */
    public static final SimpleDateFormat TIME_TO_MINITE = new SimpleDateFormat(
            "h:mm");

    // 60秒
    private final static int MINUTE_SECONDS = 60;
    // 60分钟
    private final static int HOUR_SECONDS = 3600;
    // 24小时
    private final static int DAY_SECONDS = 86400;
    // 2天
    private final static int TWO_DAY_SECONDS = 86400 * 2;
    // 3天
    private final static int THREE_DAY_SECONDS = 86400 * 3;

    // 一周
    // private final static int WEEK_SECONDS = 86400 * 7;

    // 昨天
    private final static String ONE_DAY_BEFORE = "昨天";

    // 昨天
    private final static String TWO_DAY_BEFORE = "前天";

    // 凌晨
    private final static String DAWN = "凌晨";

    // 晚上
    private final static String NIGHT = "晚上";

    // 早上
    private final static String AM = "早上";

    // 下午
    private final static String PM = "下午";

    // 未知时间
    private final static String UNKNOWN = "";

    // 星期几
    private final static String[] WEEKDAYS = {"星期日", "星期一", "星期二", "星期三",
            "星期四", "星期五", "星期六"};


    /**
     * 获取两个时间的天数间隔，每个时间都舍去时分秒
     */
    public static int getDiffDay(Timestamp timestamp1, Timestamp timestamp2) {
        Calendar c1 = Calendar.getInstance();
        c1.setTimeInMillis(timestamp1.getTime());
        c1.set(Calendar.HOUR, 0);
        c1.set(Calendar.MINUTE, 0);
        c1.set(Calendar.SECOND, 0);
        c1.set(Calendar.MILLISECOND, 0);

        Calendar c2 = Calendar.getInstance();
        c2.setTimeInMillis(timestamp2.getTime());
        c2.set(Calendar.HOUR, 0);
        c2.set(Calendar.MINUTE, 0);
        c2.set(Calendar.SECOND, 0);
        c2.set(Calendar.MILLISECOND, 0);

        long days = 0;
        if (c1.before(c2)) {
            days = (c2.getTimeInMillis() - c1.getTimeInMillis()) / (24 * 60 * 60 * 1000);
        } else {
            days = (c1.getTimeInMillis() - c2.getTimeInMillis()) / (24 * 60 * 60 * 1000);
        }
        return (int) days;
    }

    /**
     * 获取一个时间加若干天后的凌晨时间
     */
    public static Timestamp addDays(Timestamp timestamp, int days) {
        Calendar c1 = Calendar.getInstance();
        c1.add(Calendar.DAY_OF_YEAR, days);
        c1.set(Calendar.HOUR_OF_DAY, 23);
        c1.set(Calendar.MINUTE, 59);
        c1.set(Calendar.SECOND, 59);
        c1.set(Calendar.MILLISECOND, 0);


        return new Timestamp(c1.getTimeInMillis());
    }
}
