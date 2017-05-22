package com.indiepost.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.indiepost.NewIndiepostApplication;
import com.indiepost.dto.stat.PeriodDto;
import com.indiepost.dto.stat.ShareStat;
import com.indiepost.dto.stat.TimeDomainStat;
import com.indiepost.enums.Types;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.transaction.Transactional;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private StatRepositoryNativeSql statRepositoryNativeSql;

    @Test
    public void testRetrieveYearlyTotalPageview() throws JsonProcessingException {
        Long expected = 4992L;
        PeriodDto dto = getYearlyPeriod();
        Long result = testRetrieveTotals(dto);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testRetrieveMonthlyTotalPageview() throws JsonProcessingException {
        Long expected = 24487L;
        PeriodDto dto = getMonthlyPeriod();
        Long result = testRetrieveTotals(dto);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testRetrieveDailyTotalPageview() throws JsonProcessingException {
        Long expected = 4787L;
        PeriodDto dto = getDailyPeriod();
        Long result = testRetrieveTotals(dto);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testRetrieveOneDayTotalPageview() throws JsonProcessingException {
        Long expected = 1316L;
        PeriodDto dto = getOneDayPeriod();
        Long result = testRetrieveTotals(dto);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testRetrieveTotalPostview() throws JsonProcessingException {
        PeriodDto dto = getOneDayPeriod();
        LocalDateTime since = dto.getStartDate().atStartOfDay();
        LocalDateTime until = dto.getEndDate().atTime(23, 59, 59);
        Long result = statRepositoryNativeSql.getTotalPageviews(since, until, Types.StatType.POST);

        printPeriod(dto);
        System.out.println("Total Postviews: " + result);
        Assert.assertNotEquals("Total postviews should not 0", result.longValue(), 0);
    }

    @Test
    public void testRetrieveTotalVisitors() throws JsonProcessingException {
        PeriodDto dto = getOneDayPeriod();
        LocalDateTime since = dto.getStartDate().atStartOfDay();
        LocalDateTime until = dto.getEndDate().atTime(23, 59, 59);
        Long result = statRepositoryNativeSql.getTotalVisitors(since, until);

        printPeriod(dto);
        System.out.println("Total Visitors: " + result);
        Assert.assertNotEquals("Total visitors should not 0", result.longValue(), 0);
    }

    @Test
    public void testRetrieveTotalMobileAppVisitors() throws JsonProcessingException {
        PeriodDto dto = getOneDayPeriod();
        LocalDateTime since = dto.getStartDate().atStartOfDay();
        LocalDateTime until = dto.getEndDate().atTime(23, 59, 59);
        Long result = statRepositoryNativeSql.getTotalVisitors(since, until, Types.ClientType.INDIEPOST_LEGACY_MOBILE_APP);
        printPeriod(dto);
        System.out.println("Total Mobile App Visitors: " + result);
        Assert.assertNotEquals("Total mobile app visitors should not 0", result.longValue(), 0);
    }

    @Test
    public void testRetrieveYearlyPageviewTrend() throws JsonProcessingException {
        PeriodDto dto = getYearlyPeriod();
        LocalDateTime since = dto.getStartDate().atStartOfDay();
        LocalDateTime until = dto.getEndDate().atTime(23, 59, 59);
        List<TimeDomainStat> pageviewTrend = statRepositoryNativeSql.getPageviewTrend(since, until);
        testSerializeAndPrintStats(pageviewTrend, dto, "Yearly Pageview Trend");
        Assert.assertNotEquals("Sum of pageviewTrend should not 0", sumOfTimeDomainStat(pageviewTrend), 0);
    }

    @Test
    public void testRetrieveMonthlyPageviewTrend() throws JsonProcessingException {
        PeriodDto dto = getMonthlyPeriod();
        LocalDateTime since = dto.getStartDate().atStartOfDay();
        LocalDateTime until = dto.getEndDate().atTime(23, 59, 59);
        List<TimeDomainStat> pageviewTrend = statRepositoryNativeSql.getPageviewTrend(since, until);
        testSerializeAndPrintStats(pageviewTrend, dto, "Monthly Pageview Trend");
        Assert.assertNotEquals("Sum of pageviewTrend should not 0", sumOfTimeDomainStat(pageviewTrend), 0);
    }

    @Test
    public void testRetrieveDailyPageviewTrend() throws JsonProcessingException {
        PeriodDto dto = getDailyPeriod();
        LocalDateTime since = dto.getStartDate().atStartOfDay();
        LocalDateTime until = dto.getEndDate().atTime(23, 59, 59);
        List<TimeDomainStat> pageviewTrend = statRepositoryNativeSql.getPageviewTrend(since, until);
        testSerializeAndPrintStats(pageviewTrend, dto, "Daily Pageview Trend");
        Assert.assertEquals("Three days pageviewTrend should contain 3 day of results", 3, pageviewTrend.size());
        Assert.assertNotEquals("Sum of pageviewTrend should not 0", sumOfTimeDomainStat(pageviewTrend), 0);
    }


    @Test
    public void testRetrieveOneDayPageviewTrend() throws JsonProcessingException {
        PeriodDto dto = getOneDayPeriod();
        LocalDateTime since = dto.getStartDate().atStartOfDay();
        LocalDateTime until = dto.getEndDate().atTime(23, 59, 59);
        List<TimeDomainStat> pageviewTrend = statRepositoryNativeSql.getPageviewTrend(since, until);
        testSerializeAndPrintStats(pageviewTrend, dto, "One Day Pageview Trend");
        Assert.assertEquals("One day pageviewTrend should contain 24 hours of results", 24, pageviewTrend.size());
        Assert.assertNotEquals("Sum of pageviewTrend should not 0", sumOfTimeDomainStat(pageviewTrend), 0);
    }

    @Test
    public void testGetPageviewsByCategory() throws JsonProcessingException {
        PeriodDto dto = getOneDayPeriod();
        LocalDateTime since = dto.getStartDate().atStartOfDay();
        LocalDateTime until = dto.getEndDate().atTime(23, 59, 59);
        List<ShareStat> share = statRepositoryNativeSql.getPageviewsByCategory(since, until);
        testSerializeAndPrintStats(share, dto, "Pageview By Category");
        Assert.assertTrue("Repository should return resultset", share.size() > 0);
    }

    @Test
    public void testGetPageviewsByAuthor() throws JsonProcessingException {
        PeriodDto dto = getOneDayPeriod();
        LocalDateTime since = dto.getStartDate().atStartOfDay();
        LocalDateTime until = dto.getEndDate().atTime(23, 59, 59);
        List<ShareStat> share = statRepositoryNativeSql.getPageviewByAuthor(since, until);
        testSerializeAndPrintStats(share, dto, "Pageview By Author");
        Assert.assertTrue("Repository should return resultset", share.size() > 0);
    }

    @Test
    public void testGetTopPagesWebapp() throws JsonProcessingException {
        PeriodDto dto = getOneDayPeriod();
        LocalDateTime since = dto.getStartDate().atStartOfDay();
        LocalDateTime until = dto.getEndDate().atTime(23, 59, 59);
        List<ShareStat> share = statRepositoryNativeSql.getTopPages(since, until, 10L, Types.ClientType.INDIEPOST_WEBAPP);
        testSerializeAndPrintStats(share, dto, "Top Pages (Webapp)");
        Assert.assertTrue("Repository should return resultset", share.size() > 0);
    }

    @Test
    public void testGetTopPagesMobile() throws JsonProcessingException {
        PeriodDto dto = getOneDayPeriod();
        LocalDateTime since = dto.getStartDate().atStartOfDay();
        LocalDateTime until = dto.getEndDate().atTime(23, 59, 59);
        List<ShareStat> share = statRepositoryNativeSql.getTopPages(since, until, 10L, Types.ClientType.INDIEPOST_LEGACY_MOBILE_APP);
        testSerializeAndPrintStats(share, dto, "Top pages (Mobile)");
        Assert.assertTrue("Repository should return resultset", share.size() > 0);
    }

    @Test
    public void testGetTopPostWebapp() throws JsonProcessingException {
        PeriodDto dto = getOneDayPeriod();
        LocalDateTime since = dto.getStartDate().atStartOfDay();
        LocalDateTime until = dto.getEndDate().atTime(23, 59, 59);
        List<ShareStat> share = statRepositoryNativeSql.getTopPosts(since, until, 10L, Types.ClientType.INDIEPOST_WEBAPP);
        testSerializeAndPrintStats(share, dto, "Top Posts (Webapp)");
        Assert.assertTrue("Repository should return resultset", share.size() > 0);
    }

    @Test
    public void testGetTopPostMobile() throws JsonProcessingException {
        PeriodDto dto = getOneDayPeriod();
        LocalDateTime since = dto.getStartDate().atStartOfDay();
        LocalDateTime until = dto.getEndDate().atTime(23, 59, 59);
        List<ShareStat> share = statRepositoryNativeSql.getTopPosts(since, until, 10L, Types.ClientType.INDIEPOST_LEGACY_MOBILE_APP);
        testSerializeAndPrintStats(share, dto, "Top Posts (Mobile)");
        Assert.assertTrue("Repository should return resultset", share.size() > 0);
    }

    @Test
    public void testGetSecondaryViewedPagesWebapp() throws JsonProcessingException {
        PeriodDto dto = getOneDayPeriod();
        LocalDateTime since = dto.getStartDate().atStartOfDay();
        LocalDateTime until = dto.getEndDate().atTime(23, 59, 59);
        List<ShareStat> share = statRepositoryNativeSql.getSecondaryViewedPages(since, until, 10L, Types.ClientType.INDIEPOST_WEBAPP);
        testSerializeAndPrintStats(share, dto, "Secondly Pages (Webapp)");
        Assert.assertTrue("Repository should return resultset", share.size() > 0);
    }

    @Test
    public void testGetSecondaryViewedPagesMobile() throws JsonProcessingException {
        PeriodDto dto = getOneDayPeriod();
        LocalDateTime since = dto.getStartDate().atStartOfDay();
        LocalDateTime until = dto.getEndDate().atTime(23, 59, 59);
        List<ShareStat> share = statRepositoryNativeSql.getSecondaryViewedPages(since, until, 10L, Types.ClientType.INDIEPOST_LEGACY_MOBILE_APP);
        testSerializeAndPrintStats(share, dto, "Secondly Pages (Mobile)");
        Assert.assertTrue("Repository should return resultset", share.size() > 0);
    }

    @Test
    public void testGetSecondaryViewedPostsWebapp() throws JsonProcessingException {
        PeriodDto dto = getOneDayPeriod();
        LocalDateTime since = dto.getStartDate().atStartOfDay();
        LocalDateTime until = dto.getEndDate().atTime(23, 59, 59);
        List<ShareStat> share = statRepositoryNativeSql.getSecondaryViewedPosts(since, until, 10L, Types.ClientType.INDIEPOST_WEBAPP);
        testSerializeAndPrintStats(share, dto, "Secondly Posts (Webapp)");
        Assert.assertTrue("Repository should return resultset", share.size() > 0);
    }

    @Test
    public void testGetSecondaryViewedPostsMobile() throws JsonProcessingException {
        PeriodDto dto = getOneDayPeriod();
        LocalDateTime since = dto.getStartDate().atStartOfDay();
        LocalDateTime until = dto.getEndDate().atTime(23, 59, 59);
        List<ShareStat> share = statRepositoryNativeSql.getSecondaryViewedPosts(since, until, 10L, Types.ClientType.INDIEPOST_LEGACY_MOBILE_APP);
        testSerializeAndPrintStats(share, dto, "Secondly Posts (Mobile)");
        Assert.assertTrue("Repository should return resultset", share.size() > 0);
    }

    @Test
    public void testGetTopLadingPagesWebapp() throws JsonProcessingException {
        PeriodDto dto = getOneDayPeriod();
        LocalDateTime since = dto.getStartDate().atStartOfDay();
        LocalDateTime until = dto.getEndDate().atTime(23, 59, 59);
        List<ShareStat> share = statRepositoryNativeSql.getTopLandingPages(since, until, 10L, Types.ClientType.INDIEPOST_WEBAPP);
        testSerializeAndPrintStats(share, dto, "Top Landing Pages (Webapp)");
        Assert.assertTrue("Repository should return resultset", share.size() > 0);
    }

    @Test
    public void testGetTopLadingPagesMobile() throws JsonProcessingException {
        PeriodDto dto = getOneDayPeriod();
        LocalDateTime since = dto.getStartDate().atStartOfDay();
        LocalDateTime until = dto.getEndDate().atTime(23, 59, 59);
        List<ShareStat> share = statRepositoryNativeSql.getTopLandingPages(since, until, 10L, Types.ClientType.INDIEPOST_LEGACY_MOBILE_APP);
        testSerializeAndPrintStats(share, dto, "Top Landing Pages (Mobile)");
        Assert.assertTrue("Repository should return resultset", share.size() > 0);
    }

    @Test
    public void testGetTopLadingPostsWebapp() throws JsonProcessingException {
        PeriodDto dto = getOneDayPeriod();
        LocalDateTime since = dto.getStartDate().atStartOfDay();
        LocalDateTime until = dto.getEndDate().atTime(23, 59, 59);
        List<ShareStat> share = statRepositoryNativeSql.getTopLandingPosts(since, until, 10L, Types.ClientType.INDIEPOST_WEBAPP);
        testSerializeAndPrintStats(share, dto, "Top Landing Posts (Webapp)");
        Assert.assertTrue("Repository should return resultset", share.size() > 0);
    }

    @Test
    public void testGetTopLadingPostsMobile() throws JsonProcessingException {
        PeriodDto dto = getOneDayPeriod();
        LocalDateTime since = dto.getStartDate().atStartOfDay();
        LocalDateTime until = dto.getEndDate().atTime(23, 59, 59);
        List<ShareStat> share = statRepositoryNativeSql.getTopLandingPosts(since, until, 10L, Types.ClientType.INDIEPOST_LEGACY_MOBILE_APP);
        testSerializeAndPrintStats(share, dto, "Top Landing Posts (Mobile)");
        Assert.assertTrue("Repository should return resultset", share.size() > 0);
    }

    @Test
    public void testGetTopChannel() throws JsonProcessingException {
        PeriodDto dto = getOneDayPeriod();
        LocalDateTime since = dto.getStartDate().atStartOfDay();
        LocalDateTime until = dto.getEndDate().atTime(23, 59, 59);
        List<ShareStat> share = statRepositoryNativeSql.getTopChannel(since, until, 10L);
        testSerializeAndPrintStats(share, dto, "Top Channel");
        Assert.assertTrue("Repository should return resultset", share.size() > 0);
    }

    @Test
    public void testGetTopReferrer() throws JsonProcessingException {
        PeriodDto dto = getOneDayPeriod();
        LocalDateTime since = dto.getStartDate().atStartOfDay();
        LocalDateTime until = dto.getEndDate().atTime(23, 59, 59);
        List<ShareStat> share = statRepositoryNativeSql.getTopReferrers(since, until, 10L);
        testSerializeAndPrintStats(share, dto, "Top Referrer");
        Assert.assertTrue("Repository should return resultset", share.size() > 0);
    }

    @Test
    public void testGetTopOs() throws JsonProcessingException {
        PeriodDto dto = getOneDayPeriod();
        LocalDateTime since = dto.getStartDate().atStartOfDay();
        LocalDateTime until = dto.getEndDate().atTime(23, 59, 59);
        List<ShareStat> share = statRepositoryNativeSql.getTopOs(since, until, 10L);
        testSerializeAndPrintStats(share, dto, "Top Os");
        Assert.assertTrue("Repository should return resultset", share.size() > 0);
    }

    @Test
    public void testGetTopBrowsers() throws JsonProcessingException {
        PeriodDto dto = getOneDayPeriod();
        LocalDateTime since = dto.getStartDate().atStartOfDay();
        LocalDateTime until = dto.getEndDate().atTime(23, 59, 59);
        List<ShareStat> share = statRepositoryNativeSql.getTopWebBrowsers(since, until, 10L);
        testSerializeAndPrintStats(share, dto, "Top Web Browsers");
        Assert.assertTrue("Repository should return resultset", share.size() > 0);
    }

    @Test
    public void testGetTopTags() throws JsonProcessingException {
        PeriodDto dto = getOneDayPeriod();
        LocalDateTime since = dto.getStartDate().atStartOfDay();
        LocalDateTime until = dto.getEndDate().atTime(23, 59, 59);
        List<ShareStat> share = statRepositoryNativeSql.getTopTags(since, until, 10L);
        testSerializeAndPrintStats(share, dto, "Top Tags");
        Assert.assertTrue("Repository should return resultset", share.size() > 0);
    }


    private PeriodDto getYearlyPeriod() {
        LocalDate since = LocalDate.of(2015, 4, 20);
        LocalDate until = LocalDate.of(2017, 4, 26);
        return new PeriodDto(since, until);
    }

    private PeriodDto getMonthlyPeriod() {
        LocalDate since = LocalDate.of(2017, 4, 25);
        LocalDate until = LocalDate.of(2017, 5, 10);
        return new PeriodDto(since, until);
    }

    private PeriodDto getDailyPeriod() {
        LocalDate since = LocalDate.of(2017, 4, 24);
        LocalDate until = LocalDate.of(2017, 4, 26);
        return new PeriodDto(since, until);
    }

    private PeriodDto getOneDayPeriod() {
        LocalDate since = LocalDate.of(2017, 4, 26);
        LocalDate until = LocalDate.of(2017, 4, 26);
        return new PeriodDto(since, until);
    }

    private Long testRetrieveTotals(PeriodDto dto) {
        LocalDateTime since = dto.getStartDate().atStartOfDay();
        LocalDateTime until = dto.getEndDate().atTime(23, 59, 59);
        Long result = statRepositoryNativeSql.getTotalPageviews(since, until);
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
        LocalDateTime since = dto.getStartDate().atStartOfDay();
        LocalDateTime until = dto.getEndDate().atTime(23, 59, 59);
        System.out.println("Date since: " + since);
        System.out.println("Date until: " + until);
    }

    private int sumOfTimeDomainStat(List<TimeDomainStat> timeDomainStats) {
        return timeDomainStats.stream()
                .map(TimeDomainStat::getStatCount)
                .mapToInt(BigInteger::intValue)
                .sum();
    }
}
