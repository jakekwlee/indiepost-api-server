package com.indiepost.service;

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

import static com.indiepost.service.MailService.formatAddress;

import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * Created by jake on 8/31/17.
 */
@Service
@Transactional(readOnly = true)
public class MailServiceImpl implements MailService {

    private final SettingRepository settingRepository;

    private final UserService userService;

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
