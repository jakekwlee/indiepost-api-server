package com.indiepost.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.indiepost.dto.stat.TimeDomainStat;
import com.indiepost.utils.DateUtils;
import org.junit.Test;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.indiepost.utils.DateUtils.newDate;

/**
 * Created by jake on 17. 4. 28.
 */
public class AnalyticServiceUnitTest {
    @Test
    public void normalizeTimeDomainStatsWorksCorrectly() throws JsonProcessingException {

        List<TimeDomainStat> input = new ArrayList<>();
        Date d1 = newDate(2017, 1, 1, 4, 0, 0);
        Date d2 = newDate(2017, 1, 1, 10, 0, 0);

        input.add(new TimeDomainStat(d1, BigInteger.valueOf(1000L)));
        input.add(new TimeDomainStat(d2, BigInteger.valueOf(3000L)));
        List<TimeDomainStat> output =
                DateUtils.normalizeTimeDomainStats(input, newDate(2017, 1, 1), newDate(2017, 1, 1));
        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println("*** Start serialize List<TimeDomainStat> ***");
        System.out.println("Result Length: " + output.size());
        String result = objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true)
                .writeValueAsString(output);

        System.out.println(result);
    }
}
