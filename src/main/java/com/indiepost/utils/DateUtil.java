package com.indiepost.utils;

import com.indiepost.dto.stat.TimeDomainStat;

import java.time.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by jake on 17. 4. 28.
 */
public class DateUtil {
    public static Instant localDateTimeToInstant(LocalDateTime localDateTime) {
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant();
    }

    public static LocalDateTime instantToLocalDateTime(Instant instant) {
        return instant.atZone(OffsetDateTime.now().getOffset()).toLocalDateTime();
    }

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

    public static List<TimeDomainStat> normalizeTimeDomainStats(List<TimeDomainStat> list, LocalDate startDate, LocalDate endDate) {
        if (startDate.getYear() != endDate.getYear() || startDate.getMonthValue() != endDate.getMonthValue()) {
            return list;
        }
        LocalDateTime since = startDate.atStartOfDay();
        LocalDateTime until = endDate.atTime(23, 59, 59);
        Duration duration = Duration.between(since, until);
        long hours = duration.toHours();
        if (hours > 48 || hours == list.size()) {
            return list;
        }

        int expectedHours = 24;
        if (!startDate.isEqual(endDate)) {
            expectedHours = 48;
        }

        int year = startDate.getYear();
        Month month = startDate.getMonth();
        int day = startDate.getDayOfMonth();
        LocalDate localDate = LocalDate.of(year, month, day);

        List<TimeDomainStat> results = new ArrayList<>();
        for (long h = 0; h < expectedHours; ++h) {
            LocalDateTime ldt = localDate.atStartOfDay().plusHours(h);
            TimeDomainStat timeDomainStat = new TimeDomainStat(ldt, 0L);
            results.add(timeDomainStat);
        }

        for (TimeDomainStat stat : list) {
            LocalDateTime statDateTime = stat.getStatDateTime();
            LocalDate statDate = statDateTime.toLocalDate();
            int h = statDateTime.getHour();
            if (!statDate.isEqual(localDate)) {
                h = statDateTime.getHour() + 24;
            }
            results.get(h).setStatValue(stat.getStatValue());
        }
        return results;
    }

    public static List<TimeDomainStat> normalizeHoursOFTimeDomainStats(List<TimeDomainStat> list, LocalDate startDate, LocalDate endDate) {
        Period period = Period.between(startDate, endDate);
        long expectedHours = (period.getDays() + 1) * 24;

        List<TimeDomainStat> results = new ArrayList<>();
        // Zerofill
        for (long h = 0; h < expectedHours; ++h) {
            LocalDateTime ldt = startDate.atStartOfDay().plusHours(h);
            TimeDomainStat timeDomainStat = new TimeDomainStat(ldt, 0L);
            results.add(timeDomainStat);
        }

        for (TimeDomainStat stat : list) {
            LocalDateTime statDateTime = stat.getStatDateTime();
            LocalDate statDate = statDateTime.toLocalDate();
            int days = Period.between(startDate, statDate).getDays();
            int h = statDateTime.getHour() + (24 * days);
            results.get(h).setStatValue(stat.getStatValue());
        }
        return results;
    }
}
