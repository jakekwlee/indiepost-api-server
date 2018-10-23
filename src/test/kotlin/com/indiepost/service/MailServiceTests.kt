package com.indiepost.service

import com.indiepost.NewIndiepostApplication
import com.indiepost.dto.Inquiry
import com.indiepost.dto.Suggestion
import com.indiepost.enums.Types
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.mail.SimpleMailMessage
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.context.web.WebAppConfiguration
import javax.inject.Inject

/**
 * Created by jake on 9/1/17.
 */
@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [NewIndiepostApplication::class])
@WebAppConfiguration
class MailServiceTests {

    @Inject
    private val mailService: MailService? = null

    @Test
    fun mailServiceShouldSendMessageProperly() {
        val from = "테스터 <indiepost.mail@gmail.com>"
        val to = arrayOf("시스템관리자 <sysadmin@indiepost.co.kr>")
        val replyTo = "테스터 <john.doe@example.com>"
        val title = "테스트 이메일"
        val text = "이메일 테스트\n테스트 중입니다..."

        val message = SimpleMailMessage()
        message.setFrom(from)
        message.setTo(*to)
        message.setSubject(title)
        message.setText(text)
        message.setReplyTo(replyTo)
        //        mailService.sendMessage(message);
    }

    @Test
    fun testGetMailReceivers() {
        val userRole = Types.UserRole.Administrator
        val mailTo = mailService!!.getMailReceivers(userRole)
        assertThat(mailTo.size).isGreaterThan(0)
        for (to in mailTo) {
            if (to.contains("Indiepost")) {
                assertThat(to).isEqualTo("Indiepost <sysadmin@indiepost.co.kr>")
            }
        }
    }

    @Test
    fun testSendSuggestion() {
        val dto = Suggestion()
        dto.proposer = "이기원"
        dto.email = "bwv1050@gmail.com"
        dto.subject = "MailService 테스트 이메일"
        dto.contact = "010-7369-1070"
        dto.content = "테스트 이메일입니다.\n\n이메일 테스트 중..."
        //        mailService.sendSuggestion(dto);
    }

    @Test
    fun testSendInquiry() {
        val dto = Inquiry()
        dto.inquirer = "이기원"
        dto.clientName = "인디포스트"
        dto.email = "playsbach@naver.com"
        dto.contact = "010-7369-1070"
        dto.url = "http://www.indiepost.co.kr"
        dto.content = "광고 문의사항 테스트, from testSendInquiry method."
        //        mailService.sendInquiry(dto);
    }
}
