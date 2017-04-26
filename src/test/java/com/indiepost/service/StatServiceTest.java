package com.indiepost.service;

import com.indiepost.NewIndiepostApplication;
import com.indiepost.dto.stat.TimeDomainStatResult;
import com.indiepost.enums.Types;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
    public void testGetPageviews() {
        final Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        Date yesterday = calendar.getTime();
        Date now = new Date();
        List<TimeDomainStatResult> timeDomainStatResultList = statService.getPageviewTrend(yesterday, now, Types.Period.HOUR);
        System.out.println(timeDomainStatResultList);
    }
}
