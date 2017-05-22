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
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZonedDateTime;
import java.util.List;

import static com.indiepost.utils.DateUtils.getPeriod;

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
        LocalDateTime since = dto.getSince().toLocalDateTime();
        LocalDateTime until = dto.getUntil().toLocalDateTime();
        Long result = statRepositoryNativeSql.getTotalPageviews(since, until, Types.StatType.POST);

        printPeriod(dto);
        System.out.println("Total Postviews: " + result);
    }

    @Test
    public void testRetrieveTotalVisitors() throws JsonProcessingException {
        PeriodDto dto = getOneDayPeriod();
        LocalDateTime since = dto.getSince().toLocalDateTime();
        LocalDateTime until = dto.getUntil().toLocalDateTime();
        Long result = statRepositoryNativeSql.getTotalVisitors(since, until);

        printPeriod(dto);
        System.out.println("Total Visitors: " + result);
    }

    @Test
    public void testRetrieveTotalMobileAppVisitors() throws JsonProcessingException {
        PeriodDto dto = getOneDayPeriod();
        LocalDateTime since = dto.getSince().toLocalDateTime();
        LocalDateTime until = dto.getUntil().toLocalDateTime();
        Long result = statRepositoryNativeSql.getTotalVisitors(since, until, Types.ClientType.INDIEPOST_LEGACY_MOBILE_APP);

        printPeriod(dto);
        System.out.println("Total Mobile App Visitors: " + result);
    }

    @Test
    public void testRetrieveYearlyPageviewTrend() throws JsonProcessingException {
        PeriodDto dto = getYearlyPeriod();
        LocalDateTime since = dto.getSince().toLocalDateTime();
        LocalDateTime until = dto.getUntil().toLocalDateTime();
        List<TimeDomainStat> pageviewTrend = statRepositoryNativeSql.getPageviewTrend(since, until);
        testSerializeAndPrintStats(pageviewTrend, dto, "Yearly Pageview Trend");
    }

    @Test
    public void testRetrieveMonthlyPageviewTrend() throws JsonProcessingException {
        PeriodDto dto = getMonthlyPeriod();
        LocalDateTime since = dto.getSince().toLocalDateTime();
        LocalDateTime until = dto.getUntil().toLocalDateTime();
        Period period = getPeriod(since, until);
        List<TimeDomainStat> pageviewTrend = statRepositoryNativeSql.getPageviewTrend(since, until);
        testSerializeAndPrintStats(pageviewTrend, dto, "Monthly Pageview Trend");
    }

    @Test
    public void testRetrieveDailyPageviewTrend() throws JsonProcessingException {
        PeriodDto dto = getDailyPeriod();
        LocalDateTime since = dto.getSince().toLocalDateTime();
        LocalDateTime until = dto.getUntil().toLocalDateTime();
        List<TimeDomainStat> pageviewTrend = statRepositoryNativeSql.getPageviewTrend(since, until);
        testSerializeAndPrintStats(pageviewTrend, dto, "Daily Pageview Trend");
    }


    @Test
    public void testRetrieveOneDayPageviewTrend() throws JsonProcessingException {
        PeriodDto dto = getOneDayPeriod();
        LocalDateTime since = dto.getSince().toLocalDateTime();
        LocalDateTime until = dto.getUntil().toLocalDateTime();
        List<TimeDomainStat> pageviewTrend = statRepositoryNativeSql.getPageviewTrend(since, until);
        testSerializeAndPrintStats(pageviewTrend, dto, "One Day Pageview Trend");
    }

    @Test
    public void testGetPageviewsByCategory() throws JsonProcessingException {
        PeriodDto dto = getOneDayPeriod();
        List<ShareStat> share = statRepositoryNativeSql.getPageviewsByCategory(dto.getSince().toLocalDateTime(), dto.getUntil().toLocalDateTime());
        testSerializeAndPrintStats(share, dto, "Pageview By Category");
    }

    @Test
    public void testGetPageviewsByAuthor() throws JsonProcessingException {
        PeriodDto dto = getOneDayPeriod();
        List<ShareStat> share = statRepositoryNativeSql.getPageviewByAuthor(dto.getSince().toLocalDateTime(), dto.getUntil().toLocalDateTime());
        testSerializeAndPrintStats(share, dto, "Pageview By Author");
    }

    @Test
    public void testGetTopPagesWebapp() throws JsonProcessingException {
        PeriodDto dto = getOneDayPeriod();
        List<ShareStat> share = statRepositoryNativeSql.getTopPages(dto.getSince().toLocalDateTime(), dto.getUntil().toLocalDateTime(), 10L, Types.ClientType.INDIEPOST_WEBAPP);
        testSerializeAndPrintStats(share, dto, "Top Pages (Webapp)");
    }

    @Test
    public void testGetTopPagesMobile() throws JsonProcessingException {
        PeriodDto dto = getOneDayPeriod();
        List<ShareStat> share = statRepositoryNativeSql.getTopPages(dto.getSince().toLocalDateTime(), dto.getUntil().toLocalDateTime(), 10L, Types.ClientType.INDIEPOST_LEGACY_MOBILE_APP);
        testSerializeAndPrintStats(share, dto, "Top pages (Mobile)");
    }

    @Test
    public void testGetTopPostWebapp() throws JsonProcessingException {
        PeriodDto dto = getOneDayPeriod();
        List<ShareStat> share = statRepositoryNativeSql.getTopPosts(dto.getSince().toLocalDateTime(), dto.getUntil().toLocalDateTime(), 10L, Types.ClientType.INDIEPOST_WEBAPP);
        testSerializeAndPrintStats(share, dto, "Top Posts (Webapp)");
    }

    @Test
    public void testGetTopPostMobile() throws JsonProcessingException {
        PeriodDto dto = getOneDayPeriod();
        List<ShareStat> share = statRepositoryNativeSql.getTopPosts(dto.getSince().toLocalDateTime(), dto.getUntil().toLocalDateTime(), 10L, Types.ClientType.INDIEPOST_LEGACY_MOBILE_APP);
        testSerializeAndPrintStats(share, dto, "Top Posts (Mobile)");
    }

    @Test
    public void testGetSecondaryViewedPagesWebapp() throws JsonProcessingException {
        PeriodDto dto = getOneDayPeriod();
        List<ShareStat> share = statRepositoryNativeSql.getSecondaryViewedPages(dto.getSince().toLocalDateTime(), dto.getUntil().toLocalDateTime(), 10L, Types.ClientType.INDIEPOST_WEBAPP);
        testSerializeAndPrintStats(share, dto, "Secondly Pages (Webapp)");
    }

    @Test
    public void testGetSecondaryViewedPagesMobile() throws JsonProcessingException {
        PeriodDto dto = getOneDayPeriod();
        List<ShareStat> share = statRepositoryNativeSql.getSecondaryViewedPages(dto.getSince().toLocalDateTime(), dto.getUntil().toLocalDateTime(), 10L, Types.ClientType.INDIEPOST_LEGACY_MOBILE_APP);
        testSerializeAndPrintStats(share, dto, "Secondly Pages (Mobile)");
    }

    @Test
    public void testGetSecondaryViewedPostsWebapp() throws JsonProcessingException {
        PeriodDto dto = getOneDayPeriod();
        List<ShareStat> share = statRepositoryNativeSql.getSecondaryViewedPosts(dto.getSince().toLocalDateTime(), dto.getUntil().toLocalDateTime(), 10L, Types.ClientType.INDIEPOST_WEBAPP);
        testSerializeAndPrintStats(share, dto, "Secondly Posts (Webapp)");
    }

    @Test
    public void testGetSecondaryViewedPostsMobile() throws JsonProcessingException {
        PeriodDto dto = getOneDayPeriod();
        List<ShareStat> share = statRepositoryNativeSql.getSecondaryViewedPosts(dto.getSince().toLocalDateTime(), dto.getUntil().toLocalDateTime(), 10L, Types.ClientType.INDIEPOST_LEGACY_MOBILE_APP);
        testSerializeAndPrintStats(share, dto, "Secondly Posts (Mobile)");
    }

    @Test
    public void testGetTopLadingPagesWebapp() throws JsonProcessingException {
        PeriodDto dto = getOneDayPeriod();
        List<ShareStat> share = statRepositoryNativeSql.getTopLandingPages(dto.getSince().toLocalDateTime(), dto.getUntil().toLocalDateTime(), 10L, Types.ClientType.INDIEPOST_WEBAPP);
        testSerializeAndPrintStats(share, dto, "Top Landing Pages (Webapp)");
    }

    @Test
    public void testGetTopLadingPagesMobile() throws JsonProcessingException {
        PeriodDto dto = getOneDayPeriod();
        List<ShareStat> share = statRepositoryNativeSql.getTopLandingPages(dto.getSince().toLocalDateTime(), dto.getUntil().toLocalDateTime(), 10L, Types.ClientType.INDIEPOST_LEGACY_MOBILE_APP);
        testSerializeAndPrintStats(share, dto, "Top Landing Pages (Mobile)");
    }

    @Test
    public void testGetTopLadingPostsWebapp() throws JsonProcessingException {
        PeriodDto dto = getOneDayPeriod();
        List<ShareStat> share = statRepositoryNativeSql.getTopLandingPosts(dto.getSince().toLocalDateTime(), dto.getUntil().toLocalDateTime(), 10L, Types.ClientType.INDIEPOST_WEBAPP);
        testSerializeAndPrintStats(share, dto, "Top Landing Posts (Webapp)");
    }

    @Test
    public void testGetTopLadingPostsMobile() throws JsonProcessingException {
        PeriodDto dto = getOneDayPeriod();
        List<ShareStat> share = statRepositoryNativeSql.getTopLandingPosts(dto.getSince().toLocalDateTime(), dto.getUntil().toLocalDateTime(), 10L, Types.ClientType.INDIEPOST_LEGACY_MOBILE_APP);
        testSerializeAndPrintStats(share, dto, "Top Landing Posts (Mobile)");
    }

    @Test
    public void testGetTopChannel() throws JsonProcessingException {
        PeriodDto dto = getOneDayPeriod();
        List<ShareStat> share = statRepositoryNativeSql.getTopChannel(dto.getSince().toLocalDateTime(), dto.getUntil().toLocalDateTime(), 10L);
        testSerializeAndPrintStats(share, dto, "Top Channel");
    }

    @Test
    public void testGetTopReferrer() throws JsonProcessingException {
        PeriodDto dto = getOneDayPeriod();
        List<ShareStat> share = statRepositoryNativeSql.getTopReferrers(dto.getSince().toLocalDateTime(), dto.getUntil().toLocalDateTime(), 10L);
        testSerializeAndPrintStats(share, dto, "Top Referrer");
    }

    @Test
    public void testGetTopOs() throws JsonProcessingException {
        PeriodDto dto = getOneDayPeriod();
        List<ShareStat> share = statRepositoryNativeSql.getTopOs(dto.getSince().toLocalDateTime(), dto.getUntil().toLocalDateTime(), 10L);
        testSerializeAndPrintStats(share, dto, "Top Os");
    }

    @Test
    public void testGetTopBrowsers() throws JsonProcessingException {
        PeriodDto dto = getOneDayPeriod();
        List<ShareStat> share = statRepositoryNativeSql.getTopWebBrowsers(dto.getSince().toLocalDateTime(), dto.getUntil().toLocalDateTime(), 10L);
        testSerializeAndPrintStats(share, dto, "Top Web Browsers");
    }

    @Test
    public void testGetTopTags() throws JsonProcessingException {
        PeriodDto dto = getOneDayPeriod();
        List<ShareStat> share = statRepositoryNativeSql.getTopTags(dto.getSince().toLocalDateTime(), dto.getUntil().toLocalDateTime(), 10L);
        testSerializeAndPrintStats(share, dto, "Top Tags");
    }


    private PeriodDto getYearlyPeriod() {
        LocalDateTime since = LocalDateTime.of(2015, 4, 20, 0, 0);
        LocalDateTime until = LocalDateTime.of(2017, 4, 26, 23, 59, 59);
        return new PeriodDto(since.atZone(ZonedDateTime.now().getOffset()), until.atZone(ZonedDateTime.now().getOffset()));
    }

    private PeriodDto getMonthlyPeriod() {
        LocalDateTime since = LocalDateTime.of(2017, 2, 20, 0, 0);
        LocalDateTime until = LocalDateTime.of(2017, 4, 25, 23, 59, 59);
        return new PeriodDto(since.atZone(ZonedDateTime.now().getOffset()), until.atZone(ZonedDateTime.now().getOffset()));
    }

    private PeriodDto getDailyPeriod() {
        LocalDateTime since = LocalDateTime.of(2017, 4, 24, 0, 0);
        LocalDateTime until = LocalDateTime.of(2017, 4, 26, 23, 59, 59);
        return new PeriodDto(since.atZone(ZonedDateTime.now().getOffset()), until.atZone(ZonedDateTime.now().getOffset()));
    }

    private PeriodDto getOneDayPeriod() {
        LocalDateTime since = LocalDateTime.of(2017, 4, 26, 0, 0, 0);
        LocalDateTime until = LocalDateTime.of(2017, 4, 26, 23, 59, 59);
        return new PeriodDto(since.atZone(ZonedDateTime.now().getOffset()), until.atZone(ZonedDateTime.now().getOffset()));
    }

    private Long testRetrieveTotals(PeriodDto dto) {
        LocalDateTime since = dto.getSince().toLocalDateTime();
        LocalDateTime until = dto.getUntil().toLocalDateTime();
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
        LocalDateTime since = dto.getSince().toLocalDateTime();
        LocalDateTime until = dto.getUntil().toLocalDateTime();
        System.out.println("Date since: " + since);
        System.out.println("Date until: " + until);
    }
}
