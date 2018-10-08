package com.indiepost.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.indiepost.NewIndiepostApplication;
import com.indiepost.dto.analytics.PeriodDto;
import com.indiepost.dto.analytics.ShareStat;
import com.indiepost.enums.Types.ClientType;
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
 * Created by jake on 17. 5. 25.
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = NewIndiepostApplication.class)
@WebAppConfiguration
@Transactional
public class VisitorRepositoryTests {

    @Inject
    private VisitorRepository visitorRepository;

    @Test
    public void testRetrieveTotalVisitors() throws JsonProcessingException {
        PeriodDto dto = getOneDayPeriod();
        LocalDateTime since = dto.getStartDate().atStartOfDay();
        LocalDateTime until = dto.getEndDate().atTime(23, 59, 59);
        Long result = visitorRepository.getTotalVisitors(since, until);

        printPeriod(dto);
        System.out.println("Total Visitors: " + result);
        assertThat(result.longValue()).isGreaterThan(0);
    }

    @Test
    public void testRetrieveTotalMobileAppVisitors() throws JsonProcessingException {
        PeriodDto dto = getOneDayPeriod();
        LocalDateTime since = dto.getStartDate().atStartOfDay();
        LocalDateTime until = dto.getEndDate().atTime(23, 59, 59);
        Long result = visitorRepository.getTotalVisitors(since, until, ClientType.INDIEPOST_LEGACY_MOBILE_APP.toString());
        printPeriod(dto);
        System.out.println("Total Mobile App Visitors: " + result);
        assertThat(result.longValue()).isGreaterThan(0);
    }


    @Test
    public void testGetTopChannel() throws JsonProcessingException {
        PeriodDto dto = getOneDayPeriod();
        LocalDateTime since = dto.getStartDate().atStartOfDay();
        LocalDateTime until = dto.getEndDate().atTime(23, 59, 59);
        List<ShareStat> share = visitorRepository.getTopChannel(since, until, 10);
        testSerializeAndPrintStats(share, dto, "Top Channel");
        assertThat(share.size()).isGreaterThan(0);
    }

    @Test
    public void testGetTopReferrer() throws JsonProcessingException {
        PeriodDto dto = getOneDayPeriod();
        LocalDateTime since = dto.getStartDate().atStartOfDay();
        LocalDateTime until = dto.getEndDate().atTime(23, 59, 59);
        List<ShareStat> share = visitorRepository.getTopReferrers(since, until, 10);
        testSerializeAndPrintStats(share, dto, "Top Referrer");
        assertThat(share.size()).isGreaterThan(0);
    }

    @Test
    public void testGetTopOs() throws JsonProcessingException {
        PeriodDto dto = getOneDayPeriod();
        LocalDateTime since = dto.getStartDate().atStartOfDay();
        LocalDateTime until = dto.getEndDate().atTime(23, 59, 59);
        List<ShareStat> share = visitorRepository.getTopOs(since, until, 10);
        testSerializeAndPrintStats(share, dto, "Top Os");
        assertThat(share.size()).isGreaterThan(0);
    }

    @Test
    public void testGetTopBrowsers() throws JsonProcessingException {
        PeriodDto dto = getOneDayPeriod();
        LocalDateTime since = dto.getStartDate().atStartOfDay();
        LocalDateTime until = dto.getEndDate().atTime(23, 59, 59);
        List<ShareStat> share = visitorRepository.getTopWebBrowsers(since, until, 10);
        testSerializeAndPrintStats(share, dto, "Top Web Browsers");
        assertThat(share.size()).isGreaterThan(0);
    }
}
