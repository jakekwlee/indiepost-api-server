package com.indiepost.utils

import com.indiepost.dto.analytics.TimeDomainDoubleStat
import com.indiepost.dto.analytics.TimeDomainStat
import java.time.*
import java.util.*

/**
 * Created by jake on 17. 4. 28.
 */
object DateUtil {

    fun localDateTimeToInstant(localDateTime: LocalDateTime): Instant {
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant()
    }

    fun instantToLocalDateTime(instant: Instant): LocalDateTime {
        return instant.atZone(OffsetDateTime.now().offset).toLocalDateTime()
    }

    fun dateToLocalDateTime(date: Date): LocalDateTime {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
    }

    fun dateToLocalDate(date: Date): LocalDate {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
    }

    fun localDateTimeToDate(localDateTime: LocalDateTime): Date {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant())
    }

    fun localDateToDate(localDate: LocalDate): Date {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant())
    }

    fun newDate(year: Int, month: Int, day: Int): Date {
        val localDate = LocalDate.of(year, month, day)
        return localDateToDate(localDate)
    }

    fun newDate(year: Int, month: Int, day: Int, hour: Int): Date {
        val localDateTime = LocalDateTime.of(year, month, day, hour, 0)
        return localDateTimeToDate(localDateTime)
    }

    fun newDate(year: Int, month: Int, day: Int, hour: Int, minute: Int): Date {
        val localDateTime = LocalDateTime.of(year, month, day, hour, minute)
        return localDateTimeToDate(localDateTime)
    }

    fun newDate(year: Int, month: Int, day: Int, hour: Int, minute: Int, second: Int): Date {
        val localDateTime = LocalDateTime.of(year, month, day, hour, minute, second)
        return localDateTimeToDate(localDateTime)
    }

    fun normalizeTimeDomainDoubleStats(list: List<TimeDomainDoubleStat>, startDate: LocalDate, endDate: LocalDate): List<TimeDomainDoubleStat> {
        if (startDate.year != endDate.year || startDate.monthValue != endDate.monthValue) {
            return list
        }
        val since = startDate.atStartOfDay()
        val until = endDate.atTime(23, 59, 59)
        val duration = Duration.between(since, until)
        val hours = duration.toHours()
        if (hours > 48 || hours == list.size.toLong()) {
            return list
        }

        var expectedHours = 24
        if (!startDate.isEqual(endDate)) {
            expectedHours = 48
        }

        val year = startDate.year
        val month = startDate.month
        val day = startDate.dayOfMonth
        val localDate = LocalDate.of(year, month, day)

        val results = ArrayList<TimeDomainDoubleStat>()
        for (h in 0 until expectedHours) {
            val ldt = localDate.atStartOfDay().plusHours(h.toLong())
            val doubleStat = TimeDomainDoubleStat(ldt, 0L, 0L)
            results.add(doubleStat)
        }

        for ((statDateTime, value1, value2) in list) {
            val statDate = statDateTime!!.toLocalDate()
            var h = statDateTime.hour
            if (!statDate.isEqual(localDate)) {
                h = statDateTime.hour + 24
            }
            results[h].value1 = value1
            results[h].value2 = value2
        }
        return results
    }

    fun normalizeTimeDomainStats(list: List<TimeDomainStat>, startDate: LocalDate, endDate: LocalDate): List<TimeDomainStat> {
        if (startDate.year != endDate.year || startDate.monthValue != endDate.monthValue) {
            return list
        }
        val since = startDate.atStartOfDay()
        val until = endDate.atTime(23, 59, 59)
        val duration = Duration.between(since, until)
        val hours = duration.toHours()
        if (hours > 48 || hours == list.size.toLong()) {
            return list
        }

        var expectedHours = 24
        if (!startDate.isEqual(endDate)) {
            expectedHours = 48
        }

        val year = startDate.year
        val month = startDate.month
        val day = startDate.dayOfMonth
        val localDate = LocalDate.of(year, month, day)

        val results = ArrayList<TimeDomainStat>()
        for (h in 0 until expectedHours) {
            val ldt = localDate.atStartOfDay().plusHours(h.toLong())
            val timeDomainStat = TimeDomainStat(ldt, 0L)
            results.add(timeDomainStat)
        }

        for (stat in list) {
            val statDateTime = stat.statDateTime
            val statDate = statDateTime!!.toLocalDate()
            var h = statDateTime.hour
            if (!statDate.isEqual(localDate)) {
                h = statDateTime.hour + 24
            }
            results[h].statValue = stat.statValue
        }
        return results
    }

    fun normalizeHoursOFTimeDomainStats(list: List<TimeDomainStat>, startDate: LocalDate, endDate: LocalDate): List<TimeDomainStat> {
        val period = Period.between(startDate, endDate)
        val expectedHours = ((period.days + 1) * 24).toLong()

        val results = ArrayList<TimeDomainStat>()
        // Zerofill
        for (h in 0 until expectedHours) {
            val ldt = startDate.atStartOfDay().plusHours(h)
            val timeDomainStat = TimeDomainStat(ldt, 0L)
            results.add(timeDomainStat)
        }

        for (stat in list) {
            val statDateTime = stat.statDateTime
            val statDate = statDateTime!!.toLocalDate()
            val days = Period.between(startDate, statDate).days
            val h = statDateTime.hour + 24 * days
            results[h].statValue = stat.statValue
        }
        return results
    }
}
