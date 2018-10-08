package com.indiepost.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.indiepost.NewIndiepostApplication;
import com.indiepost.dto.analytics.PeriodDto;
import com.indiepost.dto.analytics.ShareStat;
import com.indiepost.dto.analytics.TimeDomainDoubleStat;
import com.indiepost.dto.analytics.TimeDomainStat;
import com.indiepost.enums.Types.ClientType;
import com.indiepost.enums.Types.TimeDomainDuration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

import static com.indiepost.repository.RepositoryTestUtil.*;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by jake on 8/9/17.
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = NewIndiepostApplication.class)
@WebAppConfiguration
@Transactional
public class StatRepositoryTests {

    @Inject
    private StatRepository statRepository;

    @Test
    public void testRetrieveYearlyPageviewTrend() throws JsonProcessingException {
        PeriodDto dto = getYearlyPeriod();
        LocalDateTime since = dto.getStartDate().atStartOfDay();
        LocalDateTime until = dto.getEndDate().atTime(23, 59, 59);
        List<TimeDomainStat> pageviewTrend = statRepository.getPageviewTrend(since, until, TimeDomainDuration.YEARLY);
        testSerializeAndPrintStats(pageviewTrend, dto, "Yearly Pageview Trend");
        assertThat(sumOfTimeDomainStat(pageviewTrend)).isNotEqualTo(0);
    }

    @Test
    public void testRetrieveMonthlyPageviewTrend() throws JsonProcessingException {
        PeriodDto dto = getMonthlyPeriod();
        LocalDateTime since = dto.getStartDate().atStartOfDay();
        LocalDateTime until = dto.getEndDate().atTime(23, 59, 59);
        List<TimeDomainStat> pageviewTrend = statRepository.getPageviewTrend(since, until, TimeDomainDuration.MONTHLY);
        testSerializeAndPrintStats(pageviewTrend, dto, "Monthly Pageview Trend");
        assertThat(sumOfTimeDomainStat(pageviewTrend)).isNotEqualTo(0);
    }

    @Test
    public void testRetrieveDailyPageviewTrend() throws JsonProcessingException {
        PeriodDto dto = getDailyPeriod();
        LocalDateTime since = dto.getStartDate().atStartOfDay();
        LocalDateTime until = dto.getEndDate().atTime(23, 59, 59);
        List<TimeDomainStat> pageviewTrend = statRepository.getPageviewTrend(since, until, TimeDomainDuration.DAILY);
        testSerializeAndPrintStats(pageviewTrend, dto, "Daily Pageview Trend");
        assertThat(pageviewTrend.size()).isNotEqualTo(5);
        assertThat(sumOfTimeDomainStat(pageviewTrend)).isNotEqualTo(0);
    }

    @Test
    public void testRetrieveOneDayPageviewTrend() throws JsonProcessingException {
        PeriodDto dto = getOneDayPeriod();
        LocalDateTime since = dto.getStartDate().atStartOfDay();
        LocalDateTime until = dto.getEndDate().atTime(23, 59, 59);
        List<TimeDomainStat> pageviewTrend = statRepository.getPageviewTrend(since, until, TimeDomainDuration.HOURLY);
        testSerializeAndPrintStats(pageviewTrend, dto, "One Day Pageview Trend");
        assertThat(pageviewTrend.size()).isEqualTo(24);
        assertThat(sumOfTimeDomainStat(pageviewTrend)).isEqualTo(2184);
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
        assertThat(totalPageviewActual).isEqualTo(totalPageviewExpected);
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
        assertThat(totalPageviewActual).isEqualTo(totalPageviewExpected);
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
        assertThat(pageviewTrend.size()).isEqualTo(5);
        assertThat(totalPageviewActual).isEqualTo(totalPageviewExpected);
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
        assertThat(totalPageviewActual).isEqualTo(totalPageviewExpected);
        assertThat(pageviewTrend.size()).isEqualTo(24);
    }

    @Test
    public void testRetrieveYearlyTotalPageview() throws JsonProcessingException {
        Long expected = 306742L;
        PeriodDto dto = getYearlyPeriod();
        Long result = testRetrieveTotals(dto);
        assertThat(result).isGreaterThanOrEqualTo(expected);
    }

    @Test
    public void testRetrieveMonthlyTotalPageview() throws JsonProcessingException {
        Long expected = 234267L;
        PeriodDto dto = getMonthlyPeriod();
        Long result = testRetrieveTotals(dto);
        assertThat(result).isEqualTo(result);
    }

    @Test
    public void testRetrieveDailyTotalPageview() throws JsonProcessingException {
        Long expected = 13405L;
        PeriodDto dto = getDailyPeriod();
        Long result = testRetrieveTotals(dto);
        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void testRetrieveOneDayTotalPageview() throws JsonProcessingException {
        Long expected = 2184L;
        PeriodDto dto = getOneDayPeriod();
        Long result = testRetrieveTotals(dto);
        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void testRetrieveTotalPostview() throws JsonProcessingException {
        PeriodDto dto = getOneDayPeriod();
        LocalDateTime since = dto.getStartDate().atStartOfDay();
        LocalDateTime until = dto.getEndDate().atTime(23, 59, 59);
        Long result = statRepository.getTotalPageviews(since, until);

        printPeriod(dto);
        System.out.println("Total Postviews: " + result);
        assertThat(result.longValue()).isEqualTo(2184);
    }

    @Test
    public void testGetPageviewsByCategory() throws JsonProcessingException {
        PeriodDto dto = getOneDayPeriod();
        LocalDateTime since = dto.getStartDate().atStartOfDay();
        LocalDateTime until = dto.getEndDate().atTime(23, 59, 59);
        List<ShareStat> share = statRepository.getPageviewsByCategory(since, until, 30);
        testSerializeAndPrintStats(share, dto, "Pageview By Category");
        assertThat(share.size()).isGreaterThan(0);
    }

    @Test
    public void testGetPageviewsByAuthor() throws JsonProcessingException {
        PeriodDto dto = getOneDayPeriod();
        LocalDateTime since = dto.getStartDate().atStartOfDay();
        LocalDateTime until = dto.getEndDate().atTime(23, 59, 59);
        List<ShareStat> share = statRepository.getPageviewsByAuthor(since, until, 30);
        testSerializeAndPrintStats(share, dto, "Pageview By Author");
        assertThat(share.size()).isGreaterThan(0);
    }

    @Test
    public void testGetTopPagesWebapp() throws JsonProcessingException {
        PeriodDto dto = getOneDayPeriod();
        LocalDateTime since = dto.getStartDate().atStartOfDay();
        LocalDateTime until = dto.getEndDate().atTime(23, 59, 59);
        List<ShareStat> share = statRepository.getTopPages(since, until, 10, ClientType.INDIEPOST_WEBAPP.toString());
        testSerializeAndPrintStats(share, dto, "Top Pages (Webapp)");
        assertThat(share.size()).isGreaterThan(0);
    }

    @Test
    public void testGetTopPagesMobile() throws JsonProcessingException {
        PeriodDto dto = getOneDayPeriod();
        LocalDateTime since = dto.getStartDate().atStartOfDay();
        LocalDateTime until = dto.getEndDate().atTime(23, 59, 59);
        List<ShareStat> share = statRepository.getTopPages(since, until, 10, ClientType.INDIEPOST_LEGACY_MOBILE_APP.toString());
        testSerializeAndPrintStats(share, dto, "Top pages (Mobile)");
        assertThat(share.size()).isGreaterThan(0);
    }

    @Test
    public void testGetTopPostWebapp() throws JsonProcessingException {
        PeriodDto dto = getOneDayPeriod();
        LocalDateTime since = dto.getStartDate().atStartOfDay();
        LocalDateTime until = dto.getEndDate().atTime(23, 59, 59);
        List<ShareStat> share = statRepository.getTopPosts(since, until, 10, ClientType.INDIEPOST_WEBAPP.toString());
        testSerializeAndPrintStats(share, dto, "Top Posts (Webapp)");
        assertThat(share.size()).isGreaterThan(0);
    }

    @Test
    public void testGetTopPostMobile() throws JsonProcessingException {
        PeriodDto dto = getOneDayPeriod();
        LocalDateTime since = dto.getStartDate().atStartOfDay();
        LocalDateTime until = dto.getEndDate().atTime(23, 59, 59);
        List<ShareStat> share = statRepository.getTopPosts(since, until, 10, ClientType.INDIEPOST_LEGACY_MOBILE_APP.toString());
        testSerializeAndPrintStats(share, dto, "Top Posts (Mobile)");
        assertThat(share.size()).isGreaterThan(0);
    }

    @Test
    public void testGetTopLadingPagesWebapp() throws JsonProcessingException {
        PeriodDto dto = getOneDayPeriod();
        LocalDateTime since = dto.getStartDate().atStartOfDay();
        LocalDateTime until = dto.getEndDate().atTime(23, 59, 59);
        List<ShareStat> share = statRepository.getTopLandingPages(since, until, 10, ClientType.INDIEPOST_WEBAPP.toString());
        testSerializeAndPrintStats(share, dto, "Top Landing Pages (Webapp)");
        assertThat(share.size()).isGreaterThan(0);
    }

    @Test
    public void testGetTopLadingPagesMobile() throws JsonProcessingException {
        PeriodDto dto = getOneDayPeriod();
        LocalDateTime since = dto.getStartDate().atStartOfDay();
        LocalDateTime until = dto.getEndDate().atTime(23, 59, 59);
        List<ShareStat> share = statRepository.getTopLandingPages(since, until, 10, ClientType.INDIEPOST_LEGACY_MOBILE_APP.toString());
        testSerializeAndPrintStats(share, dto, "Top Landing Pages (Mobile)");
        assertThat(share.size()).isGreaterThan(0);
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
        List<ShareStat> share = statRepository.getTopTags(since, until, 10);
        testSerializeAndPrintStats(share, dto, "Top Tags");
        assertThat(share.size()).isGreaterThan(0);
    }
}
