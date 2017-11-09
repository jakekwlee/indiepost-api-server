package com.indiepost.service;

import com.indiepost.NewIndiepostApplication;
import com.indiepost.dto.InquiryDto;
import com.indiepost.dto.SuggestionDto;
import com.indiepost.enums.Types;
import com.indiepost.service.MailService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by jake on 9/1/17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NewIndiepostApplication.class)
@WebAppConfiguration
public class MailServiceIntegrationTests {
    @Autowired
    private MailService mailService;

    @Test
    public void mailServiceShouldSendMessageProperly() {
        String from = "테스터 <indiepost.mail@gmail.com>";
        String[] to = {"시스템관리자 <sysadmin@indiepost.co.kr>"};
        String replyTo = "테스터 <john.doe@example.com>";
        String title = "테스트 이메일";
        String text = "이메일 테스트\n테스트 중입니다...";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(title);
        message.setText(text);
        message.setReplyTo(replyTo);
//        mailService.sendMessage(message);
    }

    @Test
    public void testGetMailReceivers() {
        Types.UserRole userRole = Types.UserRole.Administrator;
        String[] mailTo = mailService.getMailReceivers(userRole);
        assertTrue(mailTo.length > 0);
        for (String to : mailTo) {
            if (to.contains("Indiepost")) {
                assertEquals("Indiepost <sysadmin@indiepost.co.kr>", to);
            }
        }
    }

    @Test
    public void testSendSuggestion() {
        SuggestionDto dto = new SuggestionDto();
        dto.setProposer("이기원");
        dto.setEmail("bwv1050@gmail.com");
        dto.setSubject("MailService 테스트 이메일");
        dto.setContact("010-7369-1070");
        dto.setContent("테스트 이메일입니다.\n\n이메일 테스트 중...");
//        mailService.sendSuggestion(dto);
    }

    @Test
    public void testSendInquiry() {
        InquiryDto dto = new InquiryDto();
        dto.setInquirer("이기원");
        dto.setClientName("인디포스트");
        dto.setEmail("playsbach@naver.com");
        dto.setContact("010-7369-1070");
        dto.setUrl("http://www.indiepost.co.kr");
        dto.setContent("광고 문의사항 테스트, from testSendInquiry method.");
//        mailService.sendInquiry(dto);
    }
}
