package com.indiepost.service;

import com.indiepost.dto.Inquiry;
import com.indiepost.dto.Suggestion;
import com.indiepost.enums.Types;
import com.indiepost.model.Setting;
import com.indiepost.model.User;
import com.indiepost.repository.SettingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import static com.indiepost.service.MailService.formatAddress;
import static com.indiepost.service.MailService.sanitize;

/**
 * Created by jake on 8/31/17.
 */
@Service
@Transactional(readOnly = true)
public class MailServiceImpl implements MailService {

    private final SettingRepository settingRepository;

    private final UserService userService;

    private final String INQUIRY_REPLY_BODY_TEMPLATE = "담당자: %s\n\n회사(이벤트)명: %s\n\n홈페이지: %s\n\n연락처: %s\n\n문의 내용:\n\n%s";

    private final String INQUIRY_REPLY_SUBJECT_TEMPLATE = "%s (%s) 님의 문의 사항";

    @Autowired
    public MailServiceImpl(SettingRepository settingRepository, UserService userService) {
        this.settingRepository = settingRepository;
        this.userService = userService;
    }

    @Override
    public void sendMessage(SimpleMailMessage message) {
        getMailSender().send(message);
    }

    @Override
    public void sendMessage(SimpleMailMessage message, Setting setting) {
        getMailSender(setting).send(message);
    }

    @Override
    public String[] getMailReceivers(Types.UserRole role) {
        List<User> users = userService.findByRolesEnum(role, 0, 100, false);
        return users.stream()
                .map(user -> formatAddress(user.getDisplayName(), user.getEmail()))
                .collect(Collectors.toList()).toArray(new String[0]);
    }

    @Override
    public void sendSuggestion(Suggestion dto) {
        Setting setting = settingRepository.get();
        String subject = sanitize(dto.getSubject());
        String proposerName = dto.getProposer() != null ? sanitize(dto.getProposer()) : "Anonymous";
        String proposerEmail = dto.getEmail() != null ? sanitize(dto.getEmail()) : "no-reply@indiepost.co.kr";
        String text = sanitize(dto.getContent());
        if (dto.getContact() != null) {
            text += "\n\nContact: " + sanitize(dto.getContact());
        }
        String from = formatAddress(proposerName, setting.getMailUsername());
        String replyTo = formatAddress(proposerName, proposerEmail);

//        String[] mailReceivers = mailService.getMailReceivers(Types.UserRole.Administrator);
        String[] mailReceivers = {formatAddress("Jake Lee", "bwv1050@gmail.com")};
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(mailReceivers);
        message.setSubject(subject);
        message.setText(text);
        message.setReplyTo(replyTo);
        sendMessage(message, setting);
    }

    @Override
    public void sendInquiry(Inquiry dto) {
        Setting setting = settingRepository.get();
        String inquirer = sanitize(dto.getInquirer());
        String clientName = dto.getClientName() != null ? sanitize(dto.getClientName()) : "";
        String contact = dto.getContact() != null ? sanitize(dto.getContact()) : "";
        String url = dto.getUrl() != null ? sanitize(dto.getUrl()) : "";
        String content = sanitize(dto.getContent());

        String subject = formatInquirySubject(inquirer, clientName);
        String body = formatInquiryBody(inquirer, clientName, url, contact, content);

        String from = formatAddress(inquirer, setting.getMailUsername());
        String replyTo = formatAddress(inquirer, sanitize(dto.getEmail()));

        String[] mailReceivers = getMailReceivers(Types.UserRole.Administrator);
//        String[] mailReceivers = {formatAddress("Jake Lee", "bwv1050@gmail.com")};

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(mailReceivers);
        message.setSubject(subject);
        message.setText(body);
        message.setReplyTo(replyTo);
        sendMessage(message, setting);
    }

    private String formatInquirySubject(String inquirer, String clientName) {
        return String.format(INQUIRY_REPLY_SUBJECT_TEMPLATE, inquirer, clientName);
    }

    private String formatInquiryBody(String inquirer, String clientName, String url, String contact, String content) {
        return String.format(INQUIRY_REPLY_BODY_TEMPLATE, inquirer, clientName, url, contact, content);
    }

    private JavaMailSender getMailSender() {
        return getMailSender(null);
    }

    private JavaMailSender getMailSender(Setting setting) {
        if (setting == null) {
            setting = settingRepository.get();
        }

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setDefaultEncoding("UTF-8");
        mailSender.setHost(setting.getMailHost());
        mailSender.setPort(setting.getMailPort());

        mailSender.setUsername(setting.getMailUsername());
        mailSender.setPassword(setting.getMailPassword());

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        return mailSender;
    }
}
