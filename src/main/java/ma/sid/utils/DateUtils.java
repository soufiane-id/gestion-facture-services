package ma.sid.utils;

import java.time.*;
import java.time.temporal.WeekFields;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DateUtils {

    public static final String LABEL_START_WEEK = "startDate";
    public static final String LABEL_END_WEEK = "endDate";

    /**
     * Converti une date en LocalDate
     * @param date
     * @return
     */
    public static LocalDate toLocalDate(Date date){
        if(date == null) {
            date = new Date();
        }
        return Instant.ofEpochMilli(date.getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    public static int getWeekNumberOfYear(Date d) {
        /*
        Version 1
        Date d1 = new Date();
        Calendar cl = Calendar. getInstance();
        cl.setTime(d1);
        return cl.WEEK_OF_YEAR;*/

        if(d == null) {
            d = new Date();
        }

        LocalDate date = toLocalDate(d);
        // Or use a specific locale, or configure your own rules
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        return date.get(weekFields.weekOfWeekBasedYear());
    }

    /**
     *
     * @param date
     * @return Date de début et fin de semaine d'une date. Si la date est null, on prend la date daujourdhui.
     */
    public static Map<String, LocalDate> getStartEndWeekOfGivenDate(Date date) {
        HashMap intervalles = new HashMap();
        final ZonedDateTime input;
        if(date == null) {
            input = ZonedDateTime.now();
        } else {
            input = ZonedDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
        }
        final ZonedDateTime startOfWeek = input.with(DayOfWeek.MONDAY);
        final ZonedDateTime endOfWeek = startOfWeek.plusDays(6);

        intervalles.put(LABEL_START_WEEK, startOfWeek.toLocalDate());
        intervalles.put(LABEL_END_WEEK, endOfWeek.toLocalDate());

        return intervalles;
    }

    /**
     * Adds a number of months to a date returning a new object.
     * The original {@code Date} is unchanged.
     *
     * @param date  the date, not null
     * @param amount  the amount to add, may be negative
     * @return the new {@code Date} with the amount added
     */
    public static Date addMonths(final Date date, final int amount) {
        LocalDate localDate = asLocalDate(date);

        localDate = localDate.plusMonths(amount);

        return asDate(localDate);
    }


    /**
     * Adds a number of weeks to a date returning a new object.
     * The original {@code Date} is unchanged.
     *
     * @param date  the date, not null
     * @param amount  the amount to add, may be negative
     * @return the new {@code Date} with the amount added
     */
    public static Date addWeeks(final Date date, final int amount) {
        LocalDate localDate = asLocalDate(date);

        localDate = localDate.plusWeeks(amount);

        return asDate(localDate);
    }


    /**
     * Adds a number of days to a date returning a new object.
     * The original {@code Date} is unchanged.
     *
     * @param date  the date, not null
     * @param amount  the amount to add, may be negative
     * @return the new {@code Date} with the amount added
     */
    public static Date addDays(final Date date, final int amount) {
        LocalDate localDate = asLocalDate(date);

        localDate = localDate.plusDays(amount);

        return asDate(localDate);
    }

    /**
     * Adds a number of days, weeks, months to a date returning a new object.
     * The original {@code Date} is unchanged.
     *
     * @param date  the date, not null
     * @param days  the amount of days to add, may be negative
     * @param weeks  the amount of weeks to add, may be negative
     * @param months  the amount of months to add, may be negative
     * @return the new {@code Date} with the amount added
     */
    public static Date add(final Date date, final int days, final int weeks, final int months) {
        LocalDate localDate = asLocalDate(date);

        localDate = localDate.plusDays(days).plusWeeks(weeks).plusMonths(months);

        return asDate(localDate);
    }

    public static Date asDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    public static LocalDate asLocalDate(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
    }



}
