package com.indiepost.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.indiepost.NewIndiepostApplication;
import com.indiepost.dto.stat.PeriodDto;
import com.indiepost.dto.stat.ShareStatResult;
import com.indiepost.dto.stat.TimeDomainStatResult;
import com.indiepost.enums.Types;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

/**
 * Created by jake on 17. 4. 27.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NewIndiepostApplication.class)
@WebAppConfiguration
@Transactional
public class StatRepositoryTest {
    @Autowired
    private StatRepository statRepository;

    @Test
    public void testRetrieveYearlyTotalPageview() throws JsonProcessingException {
        Long expected = 4841L;
        PeriodDto dto = getYearlyPeriod();
        Long result = testRetrieveTotals(dto);

        Assert.assertEquals(expected, result);
    }

    @Test
    public void testRetrieveMonthlyTotalPageview() throws JsonProcessingException {
        Long expected = 3571L;
        PeriodDto dto = getMonthlyPeriod();
        Long result = testRetrieveTotals(dto);

        Assert.assertEquals(expected, result);
    }

    @Test
    public void testRetrieveDailyTotalPageview() throws JsonProcessingException {
        Long expected = 4637L;
        PeriodDto dto = getDailyPeriod();
        Long result = testRetrieveTotals(dto);

        Assert.assertEquals(expected, result);
    }

    @Test
    public void testRetrieveOneDayTotalPageview() throws JsonProcessingException {
        Long expected = 1270L;
        PeriodDto dto = getOneDayPeriod();
        Long result = testRetrieveTotals(dto);

        Assert.assertEquals(expected, result);
    }

    @Test
    public void testRetrieveTotalPostview() throws JsonProcessingException {
        PeriodDto dto = getOneDayPeriod();
        Date since = dto.getSince();
        Date until = dto.getUntil();
        Long result = statRepository.getTotalPageviews(since, until, Types.StatType.POST);

        printPeriod(dto);
        System.out.println("Total Postviews: " + result);
    }

    @Test
    public void testRetrieveTotalVisitors() throws JsonProcessingException {
        PeriodDto dto = getOneDayPeriod();
        Date since = dto.getSince();
        Date until = dto.getUntil();
        Long result = statRepository.getTotalVisitors(since, until);

        printPeriod(dto);
        System.out.println("Total Visitors: " + result);
    }

    @Test
    public void testRetrieveYearlyPageviewTrend() throws JsonProcessingException {
        PeriodDto dto = getYearlyPeriod();
        Date since = dto.getSince();
        Date until = dto.getUntil();
        Period period = getPeriod(since, until);
        List<TimeDomainStatResult> pageviewTrend = statRepository.getPageviewTrend(since, until, period);
        testSerializeAndPrintStats(pageviewTrend, dto, "Yearly Pageview Trend");
    }

    @Test
    public void testRetrieveMonthlyPageviewTrend() throws JsonProcessingException {
        PeriodDto dto = getMonthlyPeriod();
        Date since = dto.getSince();
        Date until = dto.getUntil();
        Period period = getPeriod(since, until);
        List<TimeDomainStatResult> pageviewTrend = statRepository.getPageviewTrend(since, until, period);
        testSerializeAndPrintStats(pageviewTrend, dto, "Monthly Pageview Trend");
    }

    @Test
    public void testRetrieveDailyPageviewTrend() throws JsonProcessingException {
        PeriodDto dto = getDailyPeriod();
        Date since = dto.getSince();
        Date until = dto.getUntil();
        Period period = getPeriod(since, until);
        List<TimeDomainStatResult> pageviewTrend = statRepository.getPageviewTrend(since, until, period);
        testSerializeAndPrintStats(pageviewTrend, dto, "Daily Pageview Trend");
    }


    @Test
    public void testRetrieveOneDayPageviewTrend() throws JsonProcessingException {
        PeriodDto dto = getOneDayPeriod();
        Date since = dto.getSince();
        Date until = dto.getUntil();
        Period period = getPeriod(since, until);
        List<TimeDomainStatResult> pageviewTrend = statRepository.getPageviewTrend(since, until, period);
        testSerializeAndPrintStats(pageviewTrend, dto, "One Day Pageview Trend");
    }

    @Test
    public void testGetPageviewsByCategory() throws JsonProcessingException {
        PeriodDto dto = getOneDayPeriod();
        List<ShareStatResult> share = statRepository.getPageviewsByCategory(dto.getSince(), dto.getUntil());
        testSerializeAndPrintStats(share, dto, "Pageview By Category");
    }

    @Test
    public void testGetPageviewsByAuthor() throws JsonProcessingException {
        PeriodDto dto = getOneDayPeriod();
        List<ShareStatResult> share = statRepository.getPageviewByAuthor(dto.getSince(), dto.getUntil());
        testSerializeAndPrintStats(share, dto, "Pageview By Author");
    }

    @Test
    public void testGetMostViewedPages() throws JsonProcessingException {
        PeriodDto dto = getOneDayPeriod();
        List<ShareStatResult> share = statRepository.getMostViewedPages(dto.getSince(), dto.getUntil(), 10L);
        testSerializeAndPrintStats(share, dto, "Most Viewed Pages");
    }

    @Test
    public void testGetMostViewedPosts() throws JsonProcessingException {
        PeriodDto dto = getOneDayPeriod();
        List<ShareStatResult> share = statRepository.getMostViewedPosts(dto.getSince(), dto.getUntil(), 10L);
        testSerializeAndPrintStats(share, dto, "Most Viewed Posts");
    }

    @Test
    public void testGetSecondlyViewedPages() throws JsonProcessingException {
        PeriodDto dto = getOneDayPeriod();
        List<ShareStatResult> share = statRepository.getSecondlyViewedPages(dto.getSince(), dto.getUntil(), 10L);
        testSerializeAndPrintStats(share, dto, "Secondly Viewed Pages");
    }

    @Test
    public void testGetSecondlyViewedPosts() throws JsonProcessingException {
        PeriodDto dto = getOneDayPeriod();
        Date since = dto.getSince();
        Date until = dto.getUntil();
        List<ShareStatResult> share = statRepository.getSecondlyViewedPosts(since, until, 10L);
        testSerializeAndPrintStats(share, dto, "Secondly Viewed Posts");
    }

    @Test
    public void testGetTopChannel() throws JsonProcessingException {
        PeriodDto dto = getOneDayPeriod();
        List<ShareStatResult> share = statRepository.getTopChannel(dto.getSince(), dto.getUntil(), 10L);
        testSerializeAndPrintStats(share, dto, "Top Channel");
    }

    @Test
    public void testGetTopLadingPages() throws JsonProcessingException {
        PeriodDto dto = getOneDayPeriod();
        List<ShareStatResult> share = statRepository.getTopLandingPages(dto.getSince(), dto.getUntil(), 10L);
        testSerializeAndPrintStats(share, dto, "Top Landing Pages");
    }

    @Test
    public void testGetTopLadingPosts() throws JsonProcessingException {
        PeriodDto dto = getOneDayPeriod();
        List<ShareStatResult> share = statRepository.getTopLandingPosts(dto.getSince(), dto.getUntil(), 10L);
        testSerializeAndPrintStats(share, dto, "Top Landing Posts");
    }

    @Test
    public void testGetTopReferrer() throws JsonProcessingException {
        PeriodDto dto = getOneDayPeriod();
        List<ShareStatResult> share = statRepository.getTopReferrers(dto.getSince(), dto.getUntil(), 10L);
        testSerializeAndPrintStats(share, dto, "Top Referrer");
    }

    @Test
    public void testGetTopOs() throws JsonProcessingException {
        PeriodDto dto = getOneDayPeriod();
        List<ShareStatResult> share = statRepository.getTopOs(dto.getSince(), dto.getUntil(), 10L);
        testSerializeAndPrintStats(share, dto, "Top Os");
    }

    @Test
    public void testGetTopBrowsers() throws JsonProcessingException {
        PeriodDto dto = getOneDayPeriod();
        List<ShareStatResult> share = statRepository.getTopWebBrowsers(dto.getSince(), dto.getUntil(), 10L);
        testSerializeAndPrintStats(share, dto, "Top Web Browsers");
    }

    @Test
    public void testGetTopTags() throws JsonProcessingException {
        PeriodDto dto = getOneDayPeriod();
        List<ShareStatResult> share = statRepository.getTopTags(dto.getSince(), dto.getUntil(), 10L);
        testSerializeAndPrintStats(share, dto, "Top Tags");
    }


    private PeriodDto getYearlyPeriod() {
        Date since = Date.from(LocalDateTime.of(2015, 4, 20, 0, 0).atZone(ZoneId.systemDefault()).toInstant());
        Date until = Date.from(LocalDateTime.of(2017, 4, 26, 23, 59, 59).atZone(ZoneId.systemDefault()).toInstant());
        return new PeriodDto(since, until);
    }

    private PeriodDto getMonthlyPeriod() {
        Date since = Date.from(LocalDateTime.of(2017, 2, 20, 0, 0).atZone(ZoneId.systemDefault()).toInstant());
        Date until = Date.from(LocalDateTime.of(2017, 4, 25, 23, 59, 59).atZone(ZoneId.systemDefault()).toInstant());
        return new PeriodDto(since, until);
    }

    private PeriodDto getDailyPeriod() {
        Date since = Date.from(LocalDateTime.of(2017, 4, 24, 0, 0).atZone(ZoneId.systemDefault()).toInstant());
        Date until = Date.from(LocalDateTime.of(2017, 4, 26, 23, 59, 59).atZone(ZoneId.systemDefault()).toInstant());
        return new PeriodDto(since, until);
    }

    private PeriodDto getOneDayPeriod() {
        Date since = Date.from(LocalDateTime.of(2017, 4, 26, 0, 0, 0).atZone(ZoneId.systemDefault()).toInstant());
        Date until = Date.from(LocalDateTime.of(2017, 4, 26, 23, 59, 59).atZone(ZoneId.systemDefault()).toInstant());
        return new PeriodDto(since, until);
    }

    private Period getPeriod(Date since, Date until) {
        LocalDate start = since.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate end = until.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return Period.between(start, end);
    }

    private Long testRetrieveTotals(PeriodDto dto) {
        Date since = dto.getSince();
        Date until = dto.getUntil();
        Long result = statRepository.getTotalPageviews(since, until);
        System.out.println("Date since: " + since);
        System.out.println("Date until: " + until);
        System.out.println("Total Pageviews: " + result);
        return result;
    }

    private void testSerializeAndPrintStats(List list, PeriodDto dto, String name) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println("\n\n*** Start serialize " + name + " ***\n\n");
        String result = objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true)
                .writeValueAsString(list);
        printPeriod(dto);
        System.out.println("Size of results: " + (result.getBytes().length / 1024.0) + " kb");
        System.out.println("Length of results: " + list.size());
        System.out.println(result);
    }

    private void printPeriod(PeriodDto dto) {
        Date since = dto.getSince();
        Date until = dto.getUntil();
        System.out.println("Date since: " + since);
        System.out.println("Date until: " + until);
    }
}
