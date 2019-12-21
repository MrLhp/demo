package com.example.demo;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.util.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class CalendarUtil {
    public static String FORMAT_YYYY_MM_DD = "yyyy-MM-dd";
    public static String FORMAT_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    private static final DateFormat dateFormat;
    private static final DateFormat simpleDateFormat;

    static {
        dateFormat = new SimpleDateFormat(CalendarUtil.FORMAT_YYYY_MM_DD_HH_MM_SS);
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    }

    public static String toLocalDateString(final Date date) {
        return CalendarUtil.toLocalString(date, CalendarUtil.FORMAT_YYYY_MM_DD);
    }

    public static Date parseLocalString(final String date) throws ParseException {
        return CalendarUtil.parseLocalString(date, CalendarUtil.FORMAT_YYYY_MM_DD);
    }

    public static String toLocalDateTimeString(final Date date) {
        return CalendarUtil.toLocalString(date, CalendarUtil.FORMAT_YYYY_MM_DD_HH_MM_SS);
    }

    public static Date parseLocalDateTimeString(final String date) throws ParseException {
        return CalendarUtil.parseLocalString(date, CalendarUtil.FORMAT_YYYY_MM_DD_HH_MM_SS);
    }

    public static String toLocalString(final Date date, final String format) {
        final Locale locale = LocaleContextHolder.getLocale();
        return (new SimpleDateFormat(format, locale)).format(date);
    }

    public static Date parseLocalString(final String date, final String format) throws ParseException {
        final Locale locale = LocaleContextHolder.getLocale();
        return (new SimpleDateFormat(format, locale)).parse(date);
    }

    public static synchronized Date parse(final String dateStr) {
        try {
            return CalendarUtil.dateFormat.parse(dateStr);
        } catch (final ParseException arg1) {
            return CalendarUtil.buildDate(1970, 1, 1);
        }
    }

    public static synchronized String format(final Date date) {
        return date == null ? "" : CalendarUtil.dateFormat.format(date);
    }

    public static synchronized String simpleFormat(final Date date) {
        return date == null ? "" : CalendarUtil.simpleDateFormat.format(date);
    }

    public static synchronized Date simpleParse(final String dateStr) {
        try {
            return StringUtils.isEmpty(dateStr) ? null : CalendarUtil.simpleDateFormat.parse(dateStr);
        } catch (final ParseException arg1) {
            return CalendarUtil.buildDate(1970, 1, 1);
        }
    }

    public static boolean isWeekend(final Date date) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        final int dayOfWeek = calendar.get(7);
        return (dayOfWeek == 1) || (dayOfWeek == 7);
    }

    public static Date buildDate(final int year, final int month, final int date) {
        final GregorianCalendar calendar = new GregorianCalendar();
        calendar.set(5, date);
        calendar.set(2, month - 1);
        calendar.set(1, year);
        calendar.set(11, 0);
        calendar.set(12, 0);
        calendar.set(13, 0);
        calendar.set(14, 0);
        return calendar.getTime();
    }

    public static Date buildDate(final int year, final int month, final int date, final int hour, final int minute,
            final int second) {
        final GregorianCalendar calendar = new GregorianCalendar();
        calendar.set(5, date);
        calendar.set(2, month - 1);
        calendar.set(1, year);
        calendar.set(13, second);
        calendar.set(12, minute);
        calendar.set(11, hour);
        return calendar.getTime();
    }

    public static Date buildDate(final int hour, final int minute) {
        final GregorianCalendar calendar = new GregorianCalendar();
        calendar.set(14, 0);
        calendar.set(13, 0);
        calendar.set(12, minute);
        calendar.set(11, hour);
        return calendar.getTime();
    }

    public static Date getZeroTimeOfDay(final Date date) {
        if (date == null) {
            return date;
        } else {
            final GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            calendar.set(11, 0);
            calendar.set(12, 0);
            calendar.set(13, 0);
            calendar.set(14, 0);
            final Date result = calendar.getTime();
            return result;
        }
    }

    public static Date getLastTimeOfDay(final Date date) {
        if (date == null) {
            return date;
        } else {
            final GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            calendar.set(11, 23);
            calendar.set(12, 59);
            calendar.set(13, 59);
            return calendar.getTime();
        }
    }

    public static Date addYears(final Date time, final Integer years) {
        if (time == null) {
            return time;
        } else {
            final GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTime(time);
            calendar.add(1, years.intValue());
            return calendar.getTime();
        }
    }

    public static Date addMonths(final Date time, final Integer months) {
        if (time == null) {
            return time;
        } else {
            final GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTime(time);
            calendar.add(2, months.intValue());
            return calendar.getTime();
        }
    }

    public static Date addDays(final Date time, final Integer days) {
        if (time == null) {
            return time;
        } else {
            final GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTime(time);
            calendar.add(6, days.intValue());
            return calendar.getTime();
        }
    }

    public static Date addHours(final Date time, final Integer hours) {
        if (time == null) {
            return time;
        } else {
            final GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTime(time);
            calendar.add(10, hours.intValue());
            return calendar.getTime();
        }
    }

    public static Date addMinutes(final Date time, final Integer minutes) {
        if (time == null) {
            return time;
        } else {
            final GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTime(time);
            calendar.add(12, minutes.intValue());
            return calendar.getTime();
        }
    }

    public static Date addSeconds(final Date time, final Integer seconds) {
        if (time == null) {
            return time;
        } else {
            final GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTime(time);
            calendar.add(13, seconds.intValue());
            return calendar.getTime();
        }
    }

    public static Date addMilliSeconds(final Date time, final Integer milliSeconds) {
        if (time == null) {
            return time;
        } else {
            final GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTime(time);
            calendar.add(14, milliSeconds.intValue());
            return calendar.getTime();
        }
    }

    public static Date getZeroTimeOfMonth(final Date date) {
        if (date == null) {
            return date;
        } else {
            final GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            calendar.set(5, 1);
            calendar.set(11, 0);
            calendar.set(12, 0);
            calendar.set(13, 0);
            calendar.set(14, 0);
            return calendar.getTime();
        }
    }

    public static Date getLastTimeOfMonth(final Date date) {
        return date == null ? date : CalendarUtil.getLastTimeOfDay(
                CalendarUtil.addDays(CalendarUtil.getZeroTimeOfMonth(CalendarUtil.addMonths(date, Integer.valueOf(1))),
                        Integer.valueOf(-1)));
    }

    public static Date getZeroTimeOfYear(final Date date) {
        if (date == null) {
            return date;
        } else {
            final GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            calendar.set(2, 0);
            calendar.set(5, 1);
            calendar.set(11, 0);
            calendar.set(12, 0);
            calendar.set(13, 0);
            calendar.set(14, 0);
            return calendar.getTime();
        }
    }

    public static Date getLastTimeOfYear(final Date date) {
        if (date == null) {
            return date;
        } else {
            final GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            calendar.set(2, 11);
            calendar.set(5, 1);
            return CalendarUtil.getLastTimeOfMonth(calendar.getTime());
        }
    }

    public static int computeDays(final Date startDate, final Date endDate) {
        final int days = (int) ((endDate.getTime() - startDate.getTime()) / 86400000L);
        return days;
    }

    public static int getWeekday(final Date date) {
        final GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        int weekday = calendar.get(7);
        --weekday;
        if (weekday == 0) {
            weekday = 7;
        }

        return weekday;
    }

    public static void main(final String[] args) {
        System.out.println(TimeZone.getDefault());
        System.out.println(Locale.getDefault());
        final SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd HHmmss");
        System.out.println(format.format(CalendarUtil.buildDate(2099, 12, 31)));
        System.out.println(CalendarUtil.getWeekday(CalendarUtil.buildDate(2013, 10, 6)));
        System.out.println(CalendarUtil.getWeekday(CalendarUtil.buildDate(2013, 10, 7)));
        System.out.println(CalendarUtil.getWeekday(CalendarUtil.buildDate(2013, 10, 11)));
        System.out.println(CalendarUtil.getWeekday(CalendarUtil.buildDate(2013, 10, 12)));
        System.out.println(CalendarUtil.getWeekday(CalendarUtil.buildDate(2013, 10, 13)));
        System.out.println(CalendarUtil.getWeekday(CalendarUtil.buildDate(2013, 10, 14)));
        System.out.println(CalendarUtil.getZeroTimeOfMonth(CalendarUtil.buildDate(2013, 9, 14)));
        System.out.println(CalendarUtil.getLastTimeOfMonth(CalendarUtil.buildDate(2013, 9, 14)));
        System.out.println(CalendarUtil.getZeroTimeOfYear(CalendarUtil.buildDate(2013, 9, 14)));
        System.out.println(CalendarUtil.getLastTimeOfYear(CalendarUtil.buildDate(2013, 9, 14)));
    }
}
