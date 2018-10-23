package com.indiepost.repository

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.indiepost.dto.analytics.PeriodDto
import com.indiepost.dto.analytics.TimeDomainStat
import com.indiepost.model.analytics.Campaign
import com.indiepost.model.analytics.Click
import com.indiepost.model.analytics.Link

import java.time.LocalDate
import java.time.LocalDateTime

/**
 * Created by jake on 8/10/17.
 */
fun getYearlyPeriod(): PeriodDto {
    val since = LocalDate.of(2015, 4, 20)
    val until = LocalDate.of(2017, 8, 31)
    return PeriodDto(since, until)
}

fun getMonthlyPeriod(): PeriodDto {
    val since = LocalDate.of(2017, 6, 1)
    val until = LocalDate.of(2017, 8, 31)
    return PeriodDto(since, until)
}

fun getDailyPeriod(): PeriodDto {
    val since = LocalDate.of(2017, 8, 1)
    val until = LocalDate.of(2017, 8, 5)
    return PeriodDto(since, until)
}

fun getOneDayPeriod(): PeriodDto {
    val since = LocalDate.of(2017, 8, 5)
    val until = LocalDate.of(2017, 8, 5)
    return PeriodDto(since, until)
}

fun newTestCampaign(): Campaign {
    val campaign = Campaign()
    campaign.name = "test campaign"
    campaign.clientName = "test client"
    campaign.goal = 1000L
    campaign.startAt = LocalDateTime.now().minusHours(10L)
    campaign.endAt = LocalDateTime.now().plusHours(10L)
    return campaign
}

fun newTestLink(): Link {
    val link = Link()
    link.name = "test link"
    link.url = "https://google.com"
    link.uid = "aa332211"
    link.createdAt = LocalDateTime.now()
    return link
}

fun newTestClick(): Click {
    val click = Click()
    click.path = "http://www.indiepost.co.kr/link/ava"
    click.timestamp = LocalDateTime.now()
    return click
}

@Throws(JsonProcessingException::class)
fun testSerializeAndPrintStats(list: List<*>, dto: PeriodDto, name: String) {
    val objectMapper = ObjectMapper()
    println("\n\n*** Start serialize $name ***\n\n")
    val result = objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true)
            .writeValueAsString(list)
    printPeriod(dto)
    println("Size of results: " + result.toByteArray().size / 1024.0 + " kb")
    println("Length of results: " + list.size)
    println(result)
}

fun printPeriod(dto: PeriodDto) {
    val since = dto.startDate!!.atStartOfDay()
    val until = dto.endDate!!.atTime(23, 59, 59)
    println("Date since: $since")
    println("Date until: $until")
}

fun sumOfTimeDomainStat(timeDomainStats: List<TimeDomainStat>): Int {
    return timeDomainStats.stream()
            .map<Long> { it.statValue }
            .mapToInt { it.toInt() }
            .sum()
}
