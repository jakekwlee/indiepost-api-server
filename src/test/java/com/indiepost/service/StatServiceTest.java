package com.indiepost.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.indiepost.NewIndiepostApplication;
import com.indiepost.dto.stat.PeriodDto;
import com.indiepost.dto.stat.SiteStats;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Date;

/**
 * Created by jake on 17. 4. 23.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NewIndiepostApplication.class)
@WebAppConfiguration
public class StatServiceTest {
    @Autowired
    private StatService statService;

    @Autowired
    private PostService postService;

    @Test
    public void testFindPostIdByLegacyId() {
        Long legacyId = 10171L;
        Long id = postService.findIdByLegacyId(legacyId);
        System.out.println("===================================");
        System.out.println("Input:" + legacyId);
        System.out.println("Output:" + id);
        System.out.println("===================================");
    }

    @Test
    public void retrievingSiteStatsShouldWorkProperly() throws JsonProcessingException {
        PeriodDto dto = getOneDayPeriod();
        SiteStats siteStats = statService.getStats(dto);
        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println("\n\n*** Start serialize SiteStats ***\n\n");
        String result = objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true)
                .writeValueAsString(siteStats);
        System.out.println("Size of results: " + (result.getBytes().length / 1024.0) + " kb");
        System.out.println(result);
    }

    private PeriodDto getYearlyPeriod() {
        return new PeriodDto(new Date(2015, 3, 20), new Date());
    }

    private PeriodDto getMonthlyPeriod() {
        return new PeriodDto(new Date(2017, 1, 20), new Date());
    }

    private PeriodDto getDailylyPeriod() {
        return new PeriodDto(new Date(2017, 4, 20), new Date());
    }

    private PeriodDto getOneDayPeriod() {
        return new PeriodDto(new Date(2017, 4, 26), new Date(2017, 4, 26, 23, 59, 59));
    }
}
