package com.indiepost.service;

import com.indiepost.NewIndiepostApplication;
import com.indiepost.enums.Types;
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
public class TestMailService {
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
        mailService.sendMessage(message);
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
}
