package com.indiepost.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.indiepost.NewIndiepostApplication;
import com.indiepost.dto.stat.PeriodDto;
import com.indiepost.dto.stat.ShareStat;
import com.indiepost.dto.stat.TimeDomainDoubleStat;
import com.indiepost.dto.stat.TimeDomainStat;
import com.indiepost.enums.Types.ClientType;
import com.indiepost.enums.Types.TimeDomainDuration;
import com.indiepost.repository.StatRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

import static com.indiepost.repository.helper.RepositoryTestHelper.*;

/**
 * Created by jake on 8/9/17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NewIndiepostApplication.class)
@WebAppConfiguration
@Transactional
public class StatRepositoryTests {

    @Autowired
    private StatRepository statRepository;

    @Test
    public void testRetrieveYearlyPageviewTrend() throws JsonProcessingException {
        PeriodDto dto = getYearlyPeriod();
        LocalDateTime since = dto.getStartDate().atStartOfDay();
        LocalDateTime until = dto.getEndDate().atTime(23, 59, 59);
        List<TimeDomainStat> pageviewTrend = statRepository.getPageviewTrend(since, until, TimeDomainDuration.YEARLY);
        testSerializeAndPrintStats(pageviewTrend, dto, "Yearly Pageview Trend");
        Assert.assertNotEquals("Sum of pageviewTrend should not 0", sumOfTimeDomainStat(pageviewTrend), 0);
    }

    @Test
    public void testRetrieveMonthlyPageviewTrend() throws JsonProcessingException {
        PeriodDto dto = getMonthlyPeriod();
        LocalDateTime since = dto.getStartDate().atStartOfDay();
        LocalDateTime until = dto.getEndDate().atTime(23, 59, 59);
        List<TimeDomainStat> pageviewTrend = statRepository.getPageviewTrend(since, until, TimeDomainDuration.MONTHLY);
        testSerializeAndPrintStats(pageviewTrend, dto, "Monthly Pageview Trend");
        Assert.assertNotEquals("Sum of pageviewTrend should not 0", sumOfTimeDomainStat(pageviewTrend), 0);
    }

    @Test
    public void testRetrieveDailyPageviewTrend() throws JsonProcessingException {
        PeriodDto dto = getDailyPeriod();
        LocalDateTime since = dto.getStartDate().atStartOfDay();
        LocalDateTime until = dto.getEndDate().atTime(23, 59, 59);
        List<TimeDomainStat> pageviewTrend = statRepository.getPageviewTrend(since, until, TimeDomainDuration.DAILY);
        testSerializeAndPrintStats(pageviewTrend, dto, "Daily Pageview Trend");
        Assert.assertEquals("PageviewTrend(Daily) should contain 5 day of results", 5, pageviewTrend.size());
        Assert.assertNotEquals("Sum of pageviewTrend should not 0", sumOfTimeDomainStat(pageviewTrend), 0);
    }

    @Test
    public void testRetrieveOneDayPageviewTrend() throws JsonProcessingException {
        PeriodDto dto = getOneDayPeriod();
        LocalDateTime since = dto.getStartDate().atStartOfDay();
        LocalDateTime until = dto.getEndDate().atTime(23, 59, 59);
        List<TimeDomainStat> pageviewTrend = statRepository.getPageviewTrend(since, until, TimeDomainDuration.HOURLY);
        testSerializeAndPrintStats(pageviewTrend, dto, "One Day Pageview Trend");
        Assert.assertEquals("PageviewTrend(Hourly) should contain 24 hours of results", 24, pageviewTrend.size());
        Assert.assertNotEquals("Sum of pageviewTrend should not 0", sumOfTimeDomainStat(pageviewTrend), 0);
    }

    @Test
    public void testRetrieveYearlyOldAndNewPageviewTrend() throws JsonProcessingException {
        PeriodDto dto = getYearlyPeriod();
        LocalDateTime since = dto.getStartDate().atStartOfDay();
        LocalDateTime until = dto.getEndDate().atTime(23, 59, 59);
        List<TimeDomainDoubleStat> pageviewTrend = statRepository.getRecentAndOldPageviewTrend(since, until, TimeDomainDuration.YEARLY);
        testSerializeAndPrintStats(pageviewTrend, dto, "Yearly Old And New Pageview Trend");
        Long totalPageviewExpected = statRepository.getTotalPostviews(since, until);
        Long totalPageviewActual = pageviewTrend.stream()
                .map(s -> s.getValue1())
                .mapToLong(value -> value.longValue())
                .sum();
        Assert.assertEquals("Method should return correct value", totalPageviewExpected, totalPageviewActual);
    }

    @Test
    public void testRetrieveMonthlyOldAndNewPageviewTrend() throws JsonProcessingException {
        PeriodDto dto = getMonthlyPeriod();
        LocalDateTime since = dto.getStartDate().atStartOfDay();
        LocalDateTime until = dto.getEndDate().atTime(23, 59, 59);
        List<TimeDomainDoubleStat> pageviewTrend = statRepository.getRecentAndOldPageviewTrend(since, until, TimeDomainDuration.MONTHLY);
        testSerializeAndPrintStats(pageviewTrend, dto, "Monthly Old And New Pageview Trend");
        Long totalPageviewExpected = statRepository.getTotalPostviews(since, until);
        Long totalPageviewActual = pageviewTrend.stream()
                .map(s -> s.getValue1())
                .mapToLong(value -> value.longValue())
                .sum();
        testSerializeAndPrintStats(pageviewTrend, dto, "Monthly Pageview Trend");
        Assert.assertEquals("Method should return correct value", totalPageviewExpected, totalPageviewActual);
    }

    @Test
    public void testRetrieveDailyOldAndNewPageviewTrend() throws JsonProcessingException {
        PeriodDto dto = getDailyPeriod();
        LocalDateTime since = dto.getStartDate().atStartOfDay();
        LocalDateTime until = dto.getEndDate().atTime(23, 59, 59);
        List<TimeDomainDoubleStat> pageviewTrend = statRepository.getRecentAndOldPageviewTrend(since, until, TimeDomainDuration.DAILY);
        testSerializeAndPrintStats(pageviewTrend, dto, "Daily Old And New Pageview Trend");
        Long totalPageviewExpected = statRepository.getTotalPostviews(since, until);
        Long totalPageviewActual = pageviewTrend.stream()
                .map(s -> s.getValue1())
                .mapToLong(value -> value.longValue())
                .sum();
        testSerializeAndPrintStats(pageviewTrend, dto, "Daily Pageview Trend");
        Assert.assertEquals("PageviewTrend(Daily) should contain 5 day of results", 5, pageviewTrend.size());
        Assert.assertEquals("Method should return correct value", totalPageviewExpected, totalPageviewActual);
    }

    @Test
    public void testRetrieveOneDayOldAndNewPageviewTrend() throws JsonProcessingException {
        PeriodDto dto = getOneDayPeriod();
        LocalDateTime since = dto.getStartDate().atStartOfDay();
        LocalDateTime until = dto.getEndDate().atTime(23, 59, 59);
        List<TimeDomainDoubleStat> pageviewTrend = statRepository.getRecentAndOldPageviewTrend(since, until, TimeDomainDuration.HOURLY);
        testSerializeAndPrintStats(pageviewTrend, dto, "One Day Old And New Pageview Trend");
        Long totalPageviewExpected = statRepository.getTotalPostviews(since, until);
        Long totalPageviewActual = pageviewTrend.stream()
                .map(s -> s.getValue1())
                .mapToLong(value -> value.longValue())
                .sum();
        Assert.assertEquals("Method should return correct value", totalPageviewExpected, totalPageviewActual);
        Assert.assertEquals("PageviewTrend(Hourly) should contain 24 hours of results", 24, pageviewTrend.size());
    }

    @Test
    public void testRetrieveYearlyTotalPageview() throws JsonProcessingException {
        Long expected = 306747L;
        PeriodDto dto = getYearlyPeriod();
        Long result = testRetrieveTotals(dto);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testRetrieveMonthlyTotalPageview() throws JsonProcessingException {
        Long expected = 234267L;
        PeriodDto dto = getMonthlyPeriod();
        Long result = testRetrieveTotals(dto);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testRetrieveDailyTotalPageview() throws JsonProcessingException {
        Long expected = 13405L;
        PeriodDto dto = getDailyPeriod();
        Long result = testRetrieveTotals(dto);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testRetrieveOneDayTotalPageview() throws JsonProcessingException {
        Long expected = 2184L;
        PeriodDto dto = getOneDayPeriod();
        Long result = testRetrieveTotals(dto);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testRetrieveTotalPostview() throws JsonProcessingException {
        PeriodDto dto = getOneDayPeriod();
        LocalDateTime since = dto.getStartDate().atStartOfDay();
        LocalDateTime until = dto.getEndDate().atTime(23, 59, 59);
        Long result = statRepository.getTotalPageviews(since, until);

        printPeriod(dto);
        System.out.println("Total Postviews: " + result);
        Assert.assertNotEquals("Total postviews should not 0", result.longValue(), 0);
    }

    @Test
    public void testGetPageviewsByCategory() throws JsonProcessingException {
        PeriodDto dto = getOneDayPeriod();
        LocalDateTime since = dto.getStartDate().atStartOfDay();
        LocalDateTime until = dto.getEndDate().atTime(23, 59, 59);
        List<ShareStat> share = statRepository.getPageviewsByCategory(since, until, 30L);
        testSerializeAndPrintStats(share, dto, "Pageview By Category");
        Assert.assertTrue("Repository should return resultset", share.size() > 0);
    }

    @Test
    public void testGetPageviewsByAuthor() throws JsonProcessingException {
        PeriodDto dto = getOneDayPeriod();
        LocalDateTime since = dto.getStartDate().atStartOfDay();
        LocalDateTime until = dto.getEndDate().atTime(23, 59, 59);
        List<ShareStat> share = statRepository.getPageviewByAuthor(since, until, 30L);
        testSerializeAndPrintStats(share, dto, "Pageview By Author");
        Assert.assertTrue("Repository should return resultset", share.size() > 0);
    }

    @Test
    public void testGetTopPagesWebapp() throws JsonProcessingException {
        PeriodDto dto = getOneDayPeriod();
        LocalDateTime since = dto.getStartDate().atStartOfDay();
        LocalDateTime until = dto.getEndDate().atTime(23, 59, 59);
        List<ShareStat> share = statRepository.getTopPages(since, until, 10L, ClientType.INDIEPOST_WEBAPP.toString());
        testSerializeAndPrintStats(share, dto, "Top Pages (Webapp)");
        Assert.assertTrue("Repository should return resultset", share.size() > 0);
    }

    @Test
    public void testGetTopPagesMobile() throws JsonProcessingException {
        PeriodDto dto = getOneDayPeriod();
        LocalDateTime since = dto.getStartDate().atStartOfDay();
        LocalDateTime until = dto.getEndDate().atTime(23, 59, 59);
        List<ShareStat> share = statRepository.getTopPages(since, until, 10L, ClientType.INDIEPOST_LEGACY_MOBILE_APP.toString());
        testSerializeAndPrintStats(share, dto, "Top pages (Mobile)");
        Assert.assertTrue("Repository should return resultset", share.size() > 0);
    }

    @Test
    public void testGetTopPostWebapp() throws JsonProcessingException {
        PeriodDto dto = getOneDayPeriod();
        LocalDateTime since = dto.getStartDate().atStartOfDay();
        LocalDateTime until = dto.getEndDate().atTime(23, 59, 59);
        List<ShareStat> share = statRepository.getTopPosts(since, until, 10L, ClientType.INDIEPOST_WEBAPP.toString());
        testSerializeAndPrintStats(share, dto, "Top Posts (Webapp)");
        Assert.assertTrue("Repository should return resultset", share.size() > 0);
    }

    @Test
    public void testGetTopPostMobile() throws JsonProcessingException {
        PeriodDto dto = getOneDayPeriod();
        LocalDateTime since = dto.getStartDate().atStartOfDay();
        LocalDateTime until = dto.getEndDate().atTime(23, 59, 59);
        List<ShareStat> share = statRepository.getTopPosts(since, until, 10L, ClientType.INDIEPOST_LEGACY_MOBILE_APP.toString());
        testSerializeAndPrintStats(share, dto, "Top Posts (Mobile)");
        Assert.assertTrue("Repository should return resultset", share.size() > 0);
    }

    @Test
    public void testGetTopLadingPagesWebapp() throws JsonProcessingException {
        PeriodDto dto = getOneDayPeriod();
        LocalDateTime since = dto.getStartDate().atStartOfDay();
        LocalDateTime until = dto.getEndDate().atTime(23, 59, 59);
        List<ShareStat> share = statRepository.getTopLandingPages(since, until, 10L, ClientType.INDIEPOST_WEBAPP.toString());
        testSerializeAndPrintStats(share, dto, "Top Landing Pages (Webapp)");
        Assert.assertTrue("Repository should return resultset", share.size() > 0);
    }

    @Test
    public void testGetTopLadingPagesMobile() throws JsonProcessingException {
        PeriodDto dto = getOneDayPeriod();
        LocalDateTime since = dto.getStartDate().atStartOfDay();
        LocalDateTime until = dto.getEndDate().atTime(23, 59, 59);
        List<ShareStat> share = statRepository.getTopLandingPages(since, until, 10L, ClientType.INDIEPOST_LEGACY_MOBILE_APP.toString());
        testSerializeAndPrintStats(share, dto, "Top Landing Pages (Mobile)");
        Assert.assertTrue("Repository should return resultset", share.size() > 0);
    }

    public Long testRetrieveTotals(PeriodDto dto) {
        LocalDateTime since = dto.getStartDate().atStartOfDay();
        LocalDateTime until = dto.getEndDate().atTime(23, 59, 59);
        Long result = statRepository.getTotalPageviews(since, until);
        System.out.println("Date since: " + since);
        System.out.println("Date until: " + until);
        System.out.println("Total Pageviews: " + result);
        return result;
    }

    @Test
    public void testGetTopTags() throws JsonProcessingException {
        PeriodDto dto = getOneDayPeriod();
        LocalDateTime since = dto.getStartDate().atStartOfDay();
        LocalDateTime until = dto.getEndDate().atTime(23, 59, 59);
        List<ShareStat> share = statRepository.getTopTags(since, until, 10L);
        testSerializeAndPrintStats(share, dto, "Top Tags");
        Assert.assertTrue("Repository should return resultset", share.size() > 0);
    }
}
