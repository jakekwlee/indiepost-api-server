package com.indiepost.service

import com.indiepost.dto.Inquiry
import com.indiepost.dto.Suggestion
import com.indiepost.enums.Types
import com.indiepost.model.Setting
import com.indiepost.repository.SettingRepository
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.JavaMailSenderImpl
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.stream.Collectors
import javax.inject.Inject

/**
 * Created by jake on 8/31/17.
 */
@Service
@Transactional(readOnly = true)
class MailServiceImpl @Inject constructor(
        private val settingRepository: SettingRepository,
        private val userService: UserService) : MailService {

    private val mailSender: JavaMailSender
        get() = getMailSender(null)

    override fun sendMessage(message: SimpleMailMessage) {
        mailSender.send(message)
    }

    override fun sendMessage(message: SimpleMailMessage, setting: Setting) {
        getMailSender(setting).send(message)
    }

    override fun getMailReceivers(role: Types.UserRole): Array<String> {
        val users = userService.findByRole(role, 0, 100, false)
        return users.stream()
                .map { user -> MailService.formatAddress(user.displayName!!, user.email!!) }
                .collect(Collectors.toList()).toTypedArray()
    }

    override fun sendSuggestion(dto: Suggestion) {
        val setting = settingRepository.get()
        val subject = MailService.sanitize(dto.subject!!)
        val proposerName = if (dto.proposer != null) MailService.sanitize(dto.proposer!!) else "Anonymous"
        val proposerEmail = if (dto.email != null) MailService.sanitize(dto.email!!) else "no-reply@indiepost.co.kr"
        var text = MailService.sanitize(dto.content!!)
        if (dto.contact != null) {
            text += "\n\nContact: " + MailService.sanitize(dto.contact!!)
        }
        val from = MailService.formatAddress(proposerName, setting.mailUsername!!)
        val replyTo = MailService.formatAddress(proposerName, proposerEmail)

        //        String[] mailReceivers = mailService.getMailReceivers(Types.UserRole.Administrator);
        val mailReceivers = arrayOf(MailService.formatAddress("Jake Lee", "bwv1050@gmail.com"))
        val message = SimpleMailMessage()
        message.setFrom(from)
        message.setTo(*mailReceivers)
        message.setSubject(subject)
        message.setText(text)
        message.setReplyTo(replyTo)
        sendMessage(message, setting)
    }

    override fun sendInquiry(dto: Inquiry) {
        val setting = settingRepository.get()
        val inquirer = MailService.sanitize(dto.inquirer!!)
        val clientName = if (dto.clientName != null) MailService.sanitize(dto.clientName!!) else ""
        val contact = if (dto.contact != null) MailService.sanitize(dto.contact!!) else ""
        val url = if (dto.url != null) MailService.sanitize(dto.url!!) else ""
        val content = MailService.sanitize(dto.content!!)

        val subject = formatInquirySubject(inquirer, clientName)
        val body = formatInquiryBody(inquirer, clientName, url, contact, content)

        val from = MailService.formatAddress(inquirer, setting.mailUsername!!)
        val replyTo = MailService.formatAddress(inquirer, MailService.sanitize(dto.email!!))

        val mailReceivers = getMailReceivers(Types.UserRole.Administrator)
        //        String[] mailReceivers = {formatAddress("Jake Lee", "bwv1050@gmail.com")};

        val message = SimpleMailMessage()
        message.setFrom(from)
        message.setTo(*mailReceivers)
        message.setSubject(subject)
        message.setText(body)
        message.setReplyTo(replyTo)
        sendMessage(message, setting)
    }

    private fun formatInquirySubject(inquirer: String, clientName: String): String {
        val INQUIRY_REPLY_SUBJECT_TEMPLATE = "%s (%s) 님의 문의 사항"
        return String.format(INQUIRY_REPLY_SUBJECT_TEMPLATE, inquirer, clientName)
    }

    private fun formatInquiryBody(inquirer: String, clientName: String, url: String, contact: String, content: String): String {
        val INQUIRY_REPLY_BODY_TEMPLATE = "담당자: %s\n\n회사(이벤트)명: %s\n\n홈페이지: %s\n\n연락처: %s\n\n문의 내용:\n\n%s"
        return String.format(INQUIRY_REPLY_BODY_TEMPLATE, inquirer, clientName, url, contact, content)
    }

    private fun getMailSender(setting: Setting?): JavaMailSender {
        val s = (setting ?: settingRepository.get())

        val mailSender = JavaMailSenderImpl()
        mailSender.defaultEncoding = "UTF-8"
        mailSender.host = s.mailHost
        mailSender.port = s.mailPort

        mailSender.username = s.mailUsername
        mailSender.password = s.mailPassword

        val props = mailSender.javaMailProperties
        props["mail.transport.protocol"] = "smtp"
        props["mail.smtp.auth"] = "true"
        props["mail.smtp.starttls.enable"] = "true"

        return mailSender
    }
}
