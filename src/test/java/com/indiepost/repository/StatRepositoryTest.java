package com.indiepost.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.indiepost.NewIndiepostApplication;
import com.indiepost.dto.stat.PeriodDto;
import com.indiepost.dto.stat.ShareStatResult;
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
import java.time.Period;
import java.util.Date;
import java.util.List;

import static com.indiepost.utils.DateUtils.getPeriod;
import static com.indiepost.utils.DateUtils.newDate;

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
    public void testRetrieveTotalMobileAppVisitors() throws JsonProcessingException {
        PeriodDto dto = getOneDayPeriod();
        Date since = dto.getSince();
        Date until = dto.getUntil();
        Long result = statRepository.getTotalVisitors(since, until, Types.ClientType.INDIEPOST_LEGACY_MOBILE_APP);

        printPeriod(dto);
        System.out.println("Total Mobile App Visitors: " + result);
    }

    @Test
    public void testRetrieveYearlyPageviewTrend() throws JsonProcessingException {
        PeriodDto dto = getYearlyPeriod();
        Date since = dto.getSince();
        Date until = dto.getUntil();
        Period period = getPeriod(since, until);
        List<TimeDomainStat> pageviewTrend = statRepository.getPageviewTrend(since, until, period);
        testSerializeAndPrintStats(pageviewTrend, dto, "Yearly Pageview Trend");
    }

    @Test
    public void testRetrieveMonthlyPageviewTrend() throws JsonProcessingException {
        PeriodDto dto = getMonthlyPeriod();
        Date since = dto.getSince();
        Date until = dto.getUntil();
        Period period = getPeriod(since, until);
        List<TimeDomainStat> pageviewTrend = statRepository.getPageviewTrend(since, until, period);
        testSerializeAndPrintStats(pageviewTrend, dto, "Monthly Pageview Trend");
    }

    @Test
    public void testRetrieveDailyPageviewTrend() throws JsonProcessingException {
        PeriodDto dto = getDailyPeriod();
        Date since = dto.getSince();
        Date until = dto.getUntil();
        Period period = getPeriod(since, until);
        List<TimeDomainStat> pageviewTrend = statRepository.getPageviewTrend(since, until, period);
        testSerializeAndPrintStats(pageviewTrend, dto, "Daily Pageview Trend");
    }


    @Test
    public void testRetrieveOneDayPageviewTrend() throws JsonProcessingException {
        PeriodDto dto = getOneDayPeriod();
        Date since = dto.getSince();
        Date until = dto.getUntil();
        Period period = getPeriod(since, until);
        List<TimeDomainStat> pageviewTrend = statRepository.getPageviewTrend(since, until, period);
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
    public void testGetTopPagesWebapp() throws JsonProcessingException {
        PeriodDto dto = getOneDayPeriod();
        List<ShareStatResult> share = statRepository.getTopPages(dto.getSince(), dto.getUntil(), 10L, Types.ClientType.INDIEPOST_WEBAPP);
        testSerializeAndPrintStats(share, dto, "Top Pages (Webapp)");
    }

    @Test
    public void testGetTopPagesMobile() throws JsonProcessingException {
        PeriodDto dto = getOneDayPeriod();
        List<ShareStatResult> share = statRepository.getTopPages(dto.getSince(), dto.getUntil(), 10L, Types.ClientType.INDIEPOST_LEGACY_MOBILE_APP);
        testSerializeAndPrintStats(share, dto, "Top pages (Mobile)");
    }

    @Test
    public void testGetTopPostWebapp() throws JsonProcessingException {
        PeriodDto dto = getOneDayPeriod();
        List<ShareStatResult> share = statRepository.getTopPosts(dto.getSince(), dto.getUntil(), 10L, Types.ClientType.INDIEPOST_WEBAPP);
        testSerializeAndPrintStats(share, dto, "Top Posts (Webapp)");
    }

    @Test
    public void testGetTopPostMobile() throws JsonProcessingException {
        PeriodDto dto = getOneDayPeriod();
        List<ShareStatResult> share = statRepository.getTopPosts(dto.getSince(), dto.getUntil(), 10L, Types.ClientType.INDIEPOST_LEGACY_MOBILE_APP);
        testSerializeAndPrintStats(share, dto, "Top Posts (Mobile)");
    }

    @Test
    public void testGetSecondaryViewedPagesWebapp() throws JsonProcessingException {
        PeriodDto dto = getOneDayPeriod();
        List<ShareStatResult> share = statRepository.getSecondaryViewedPages(dto.getSince(), dto.getUntil(), 10L, Types.ClientType.INDIEPOST_WEBAPP);
        testSerializeAndPrintStats(share, dto, "Secondly Pages (Webapp)");
    }

    @Test
    public void testGetSecondaryViewedPagesMobile() throws JsonProcessingException {
        PeriodDto dto = getOneDayPeriod();
        List<ShareStatResult> share = statRepository.getSecondaryViewedPages(dto.getSince(), dto.getUntil(), 10L, Types.ClientType.INDIEPOST_LEGACY_MOBILE_APP);
        testSerializeAndPrintStats(share, dto, "Secondly Pages (Mobile)");
    }

    @Test
    public void testGetSecondaryViewedPostsWebapp() throws JsonProcessingException {
        PeriodDto dto = getOneDayPeriod();
        List<ShareStatResult> share = statRepository.getSecondaryViewedPosts(dto.getSince(), dto.getUntil(), 10L, Types.ClientType.INDIEPOST_WEBAPP);
        testSerializeAndPrintStats(share, dto, "Secondly Posts (Webapp)");
    }

    @Test
    public void testGetSecondaryViewedPostsMobile() throws JsonProcessingException {
        PeriodDto dto = getOneDayPeriod();
        List<ShareStatResult> share = statRepository.getSecondaryViewedPosts(dto.getSince(), dto.getUntil(), 10L, Types.ClientType.INDIEPOST_LEGACY_MOBILE_APP);
        testSerializeAndPrintStats(share, dto, "Secondly Posts (Mobile)");
    }

    @Test
    public void testGetTopLadingPagesWebapp() throws JsonProcessingException {
        PeriodDto dto = getOneDayPeriod();
        List<ShareStatResult> share = statRepository.getTopLandingPages(dto.getSince(), dto.getUntil(), 10L, Types.ClientType.INDIEPOST_WEBAPP);
        testSerializeAndPrintStats(share, dto, "Top Landing Pages (Webapp)");
    }

    @Test
    public void testGetTopLadingPagesMobile() throws JsonProcessingException {
        PeriodDto dto = getOneDayPeriod();
        List<ShareStatResult> share = statRepository.getTopLandingPages(dto.getSince(), dto.getUntil(), 10L, Types.ClientType.INDIEPOST_LEGACY_MOBILE_APP);
        testSerializeAndPrintStats(share, dto, "Top Landing Pages (Mobile)");
    }

    @Test
    public void testGetTopLadingPostsWebapp() throws JsonProcessingException {
        PeriodDto dto = getOneDayPeriod();
        List<ShareStatResult> share = statRepository.getTopLandingPosts(dto.getSince(), dto.getUntil(), 10L, Types.ClientType.INDIEPOST_WEBAPP);
        testSerializeAndPrintStats(share, dto, "Top Landing Posts (Webapp)");
    }

    @Test
    public void testGetTopLadingPostsMobile() throws JsonProcessingException {
        PeriodDto dto = getOneDayPeriod();
        List<ShareStatResult> share = statRepository.getTopLandingPosts(dto.getSince(), dto.getUntil(), 10L, Types.ClientType.INDIEPOST_LEGACY_MOBILE_APP);
        testSerializeAndPrintStats(share, dto, "Top Landing Posts (Mobile)");
    }

    @Test
    public void testGetTopChannel() throws JsonProcessingException {
        PeriodDto dto = getOneDayPeriod();
        List<ShareStatResult> share = statRepository.getTopChannel(dto.getSince(), dto.getUntil(), 10L);
        testSerializeAndPrintStats(share, dto, "Top Channel");
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
        Date since = newDate(2015, 4, 20, 0, 0);
        Date until = newDate(2017, 4, 26, 23, 59, 59);
        return new PeriodDto(since, until);
    }

    private PeriodDto getMonthlyPeriod() {
        Date since = newDate(2017, 2, 20, 0, 0);
        Date until = newDate(2017, 4, 25, 23, 59, 59);
        return new PeriodDto(since, until);
    }

    private PeriodDto getDailyPeriod() {
        Date since = newDate(2017, 4, 24, 0, 0);
        Date until = newDate(2017, 4, 26, 23, 59, 59);
        return new PeriodDto(since, until);
    }

    private PeriodDto getOneDayPeriod() {
        Date since = newDate(2017, 4, 26, 0, 0, 0);
        Date until = newDate(2017, 4, 26, 23, 59, 59);
        return new PeriodDto(since, until);
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
