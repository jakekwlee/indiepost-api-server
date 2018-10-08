package com.indiepost.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.indiepost.dto.analytics.TimeDomainStat;
import com.indiepost.utils.DateUtil;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by jake on 17. 4. 28.
 */
public class AnalyticsServiceUnitTests {

    @Test
    public void normalizeTimeDomainStatsWorksCorrectly() throws JsonProcessingException {

        List<TimeDomainStat> input = new ArrayList<>();
        LocalDateTime d1 = LocalDateTime.of(2017, 1, 1, 4, 0, 0);
        LocalDateTime d2 = LocalDateTime.of(2017, 1, 1, 10, 0, 0);

        Long d1Value = 1000L;
        Long d2Value = 3000L;

        input.add(new TimeDomainStat(d1, d1Value));
        input.add(new TimeDomainStat(d2, d2Value));
        List<TimeDomainStat> output =
                DateUtil.normalizeTimeDomainStats(
                        input,
                        LocalDate.of(2017, 1, 1),
                        LocalDate.of(2017, 1, 1));
        assertThat(output.size()).isEqualTo(24);
        for (int i = 0; i < output.size(); ++i) {
            TimeDomainStat stat = output.get(i);
            if (i == 4) {
                assertThat(stat.getStatValue()).isEqualTo(d1Value);
            } else if (i == 10) {
                assertThat(stat.getStatValue()).isEqualTo(d2Value);
            } else {
                assertThat(stat.getStatValue()).isEqualTo(0L);
            }
        }
    }
}
