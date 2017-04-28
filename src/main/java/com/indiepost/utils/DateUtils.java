package com.indiepost.utils;

import com.indiepost.dto.stat.TimeDomainStat;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by jake on 17. 4. 28.
 */
public class DateUtils {
    public static LocalDateTime dateToLocalDateTime(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static LocalDate dateToLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static Date localDateTimeToDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date localDateToDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    public static Date newDate(int year, int month, int day) {
        LocalDate localDate = LocalDate.of(year, month, day);
        return localDateToDate(localDate);
    }

    public static Date newDate(int year, int month, int day, int hour) {
        LocalDateTime localDateTime = LocalDateTime.of(year, month, day, hour, 0);
        return localDateTimeToDate(localDateTime);
    }

    public static Date newDate(int year, int month, int day, int hour, int minute) {
        LocalDateTime localDateTime = LocalDateTime.of(year, month, day, hour, minute);
        return localDateTimeToDate(localDateTime);
    }

    public static Date newDate(int year, int month, int day, int hour, int minute, int second) {
        LocalDateTime localDateTime = LocalDateTime.of(year, month, day, hour, minute, second);
        return localDateTimeToDate(localDateTime);
    }

    public static List<TimeDomainStat> normalizeTimeDomainStats(List<TimeDomainStat> list, Period period) {
        if (period.getDays() > 0 || list.size() == 24) {
            return list;
        }
        Date date = list.get(0).getStatDatetime();
        ZoneId zoneId = ZoneId.systemDefault();
        BigInteger zero = BigInteger.valueOf(0L);
        LocalDateTime localDateTime = date.toInstant().atZone(zoneId).toLocalDateTime();

        int year = localDateTime.getYear();
        int month = localDateTime.getMonthValue();
        int day = localDateTime.getDayOfMonth();

        List<TimeDomainStat> results = new ArrayList<>();

        for (int hour = 0; hour < 24; ++hour) {
            LocalDateTime ldt = LocalDateTime.of(year, month, day, hour, 0);
            Date newDate = Date.from(ldt.atZone(zoneId).toInstant());
            results.add(new TimeDomainStat(newDate, zero));
        }

        for (TimeDomainStat tdr : list) {
            Date d = tdr.getStatDatetime();
            int hour = d.toInstant().atZone(zoneId).toLocalDateTime().getHour();
            TimeDomainStat result = results.get(hour);
            result.setStatCount(tdr.getStatCount());
            results.set(hour, result);
        }
        return results;
    }

    public static Period getPeriod(Date since, Date until) {
        LocalDate start = since.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate end = until.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return Period.between(start, end);
    }
}
