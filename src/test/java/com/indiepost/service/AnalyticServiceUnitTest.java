package com.indiepost.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.indiepost.dto.stat.TimeDomainStat;
import com.indiepost.utils.DateUtils;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jake on 17. 4. 28.
 */
public class AnalyticServiceUnitTest {
    @Test
    public void normalizeTimeDomainStatsWorksCorrectly() throws JsonProcessingException {

        List<TimeDomainStat> input = new ArrayList<>();
        LocalDateTime d1 = LocalDateTime.of(2017, 1, 1, 4, 0, 0);
        LocalDateTime d2 = LocalDateTime.of(2017, 1, 1, 10, 0, 0);

        input.add(new TimeDomainStat(d1, 1000L));
        input.add(new TimeDomainStat(d2, 3000L));
        List<TimeDomainStat> output =
                DateUtils.normalizeTimeDomainStats(
                        input,
                        LocalDate.of(2017, 1, 1),
                        LocalDate.of(2017, 1, 1));
        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println("*** Start serialize List<TimeDomainStat> ***");
        System.out.println("Result Length: " + output.size());
        String result = objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true)
                .writeValueAsString(output);

        System.out.println(result);
    }
}
