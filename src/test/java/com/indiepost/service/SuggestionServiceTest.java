package com.indiepost.service;

import com.indiepost.NewIndiepostApplication;
import com.indiepost.dto.SuggestionDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Created by jake on 9/1/17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NewIndiepostApplication.class)
@WebAppConfiguration
public class SuggestionServiceTest {

    @Autowired
    private SuggestionService suggestionService;

    @Test
    public void testHandleSuggestion() {
        SuggestionDto dto = new SuggestionDto();
//        dto.setProposer("이기원");
//        dto.setEmail("bwv1050@gmail.com");
        dto.setSubject("SuggestionService 테스트 이메일");
        dto.setContact("010-7369-1070");
        dto.setContent("테스트 이메일입니다.\n\n이메일 테스트 중...");
        suggestionService.handelSuggestion(dto);
    }

}
