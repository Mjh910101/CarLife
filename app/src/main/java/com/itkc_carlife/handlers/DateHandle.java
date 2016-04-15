package com.itkc_carlife.handlers;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateHandle {
	public final static String DATESTYP_10 = "MM月dd日 HH:mm";
	public final static String DATESTYP_2 = "yyyyMMddHHmmss";
	public final static String DATESTYP_4 = "yyyy-MM-dd";
	public final static String DATESTYP_8 = "HH:mm";
	public final static String DATESTYP_9 = "MM-dd HH:mm";
	public final static String DATESTYP_11 = "yyyy-MM-dd HH:mm";
	public final static long MS_OF_ONE_DAY = 1000 * 60 * 60 * 24;

	public static long getTime() {
		return System.currentTimeMillis() / 1000;
	}

	public static String getTimeString(Date date, String type) {
		SimpleDateFormat dateformat = new SimpleDateFormat(type);
		return dateformat.format(date);
	}

	public static String format(long time, String type) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date(time));
		return getTimeString(calendar.getTime(), type);
	}

	public static boolean isToday(Calendar calendar) {
		Calendar today = getToday();
		return getYear(calendar) == getYear(today)
				&& getMonth(calendar) == getMonth(today)
				&& getDay(calendar) == getDay(today);
	}

	public static String getIsTodayFormat(long time) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date(time));
		if (isToday(calendar)) {
			return format(time, DATESTYP_8);
		} else {
			return format(time, DATESTYP_9);
		}
	}

	public static Calendar getToday() {
		return Calendar.getInstance();
	}

	public static int getYear(Calendar calendar) {
		return calendar.get(Calendar.YEAR);
	}

	public static int getMonth(Calendar calendar) {
		return calendar.get(Calendar.MONTH);
	}

	public static int getDay(Calendar calendar) {
		return calendar.get(Calendar.DAY_OF_MONTH);
	}

	public static int getHour(Calendar calendar) {
		return calendar.get(Calendar.HOUR_OF_DAY);
	}

	public static boolean isTheSameDay(long d1, long d2) {
		// float l1 = d1 / MS_OF_ONE_DAY;
		// float l2 = d2 / MS_OF_ONE_DAY;
		// Log.e("", l1 + "====" + l2);
		// return l1 == l2;
		Calendar c1 = Calendar.getInstance();
		c1.setTime(new Date(d1));

		Calendar c2 = Calendar.getInstance();
		c2.setTime(new Date(d2));

		return getYear(c1) == getYear(c2) && getMonth(c1) == getMonth(c2)
				&& getDay(c1) == getDay(c2);
	}

}
