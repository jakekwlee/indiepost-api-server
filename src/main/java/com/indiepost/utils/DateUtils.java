package com.indiepost.utils;

import com.indiepost.dto.stat.TimeDomainStat;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
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

    public static List<TimeDomainStat> normalizeTimeDomainStats(List<TimeDomainStat> list, LocalDateTime since, LocalDateTime until) {
        Period period = getPeriod(since, until);
        int days = period.getDays() + 1;
        int length = list.size();
        if (days > 2 || length / days == 24) {
            return list;
        }

        BigInteger zero = BigInteger.valueOf(0L);

        List<TimeDomainStat> results = new ArrayList<>();

        for (int i = 0; i < days; ++i) {
            LocalDateTime localDateTime;
            if (i == 0) {
                localDateTime = since;
            } else {
                localDateTime = until;
            }
            int year = localDateTime.getYear();
            int month = localDateTime.getMonthValue();
            int day = localDateTime.getDayOfMonth();

            for (int hour = 0; hour < 24; ++hour) {
                LocalDateTime ldt = LocalDateTime.of(year, month, day, hour, 0);
                results.add(new TimeDomainStat(ldt, zero));
            }
        }
        for (TimeDomainStat tdr : list) {
            LocalDateTime ldt = tdr.getStatDatetime();
            int n = Math.toIntExact(ChronoUnit.HOURS.between(since, ldt)) / 24;
            int index = ldt.getHour() + n * 24;
            TimeDomainStat result = results.get(index);
            result.setStatCount(tdr.getStatCount());
            results.set(index, result);
        }
        return results;
    }

    public static Period getPeriod(LocalDateTime since, LocalDateTime until) {
        return Period.between(since.toLocalDate(), until.toLocalDate());
    }
}
