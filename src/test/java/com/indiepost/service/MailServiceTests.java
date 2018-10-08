package com.indiepost.service;

import com.indiepost.NewIndiepostApplication;
import com.indiepost.dto.Inquiry;
import com.indiepost.dto.Suggestion;
import com.indiepost.enums.Types;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by jake on 9/1/17.
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = NewIndiepostApplication.class)
@WebAppConfiguration
public class MailServiceTests {

    @Inject
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
        assertThat(mailTo.length).isGreaterThan(0);
        for (String to : mailTo) {
            if (to.contains("Indiepost")) {
                assertThat(to).isEqualTo("Indiepost <sysadmin@indiepost.co.kr>");
            }
        }
    }

    @Test
    public void testSendSuggestion() {
        Suggestion dto = new Suggestion();
        dto.setProposer("이기원");
        dto.setEmail("bwv1050@gmail.com");
        dto.setSubject("MailService 테스트 이메일");
        dto.setContact("010-7369-1070");
        dto.setContent("테스트 이메일입니다.\n\n이메일 테스트 중...");
//        mailService.sendSuggestion(dto);
    }

    @Test
    public void testSendInquiry() {
        Inquiry dto = new Inquiry();
        dto.setInquirer("이기원");
        dto.setClientName("인디포스트");
        dto.setEmail("playsbach@naver.com");
        dto.setContact("010-7369-1070");
        dto.setUrl("http://www.indiepost.co.kr");
        dto.setContent("광고 문의사항 테스트, from testSendInquiry method.");
//        mailService.sendInquiry(dto);
    }
}
