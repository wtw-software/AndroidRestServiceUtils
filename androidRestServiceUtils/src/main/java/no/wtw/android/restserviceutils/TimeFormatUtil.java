package no.wtw.android.restserviceutils;

import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;

public class TimeFormatUtil {

    private static DateTimeFormatter RFC_822_DATE_TIME_FORMAT = new DateTimeFormatterBuilder()
            .appendYear(4, 4).appendLiteral('-').appendMonthOfYear(2).appendLiteral('-').appendDayOfMonth(2)
            .appendLiteral('T').appendHourOfDay(2).appendLiteral(':').appendMinuteOfHour(2).appendLiteral(':').appendSecondOfMinute(2)
            .appendTimeZoneOffset("Z", true, 2, 2)
            .toFormatter();

    private static DateTimeFormatter RFC_822_LOCAL_DATE_FORMAT = new DateTimeFormatterBuilder()
            .appendYear(4, 4).appendLiteral('-').appendMonthOfYear(2).appendLiteral('-').appendDayOfMonth(2)
            .toFormatter();

    public static DateTimeFormatter getRFC822DateTimeFormatter() {
        return RFC_822_DATE_TIME_FORMAT;
    }

    public static DateTimeFormatter getRFC822LocalDateFormatter() {
        return RFC_822_LOCAL_DATE_FORMAT;
    }

}
