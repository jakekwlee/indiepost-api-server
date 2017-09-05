package com.indiepost.service;

import com.indiepost.dto.SuggestionDto;
import com.indiepost.enums.Types;
import com.indiepost.model.Setting;
import com.indiepost.repository.SettingRepository;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.indiepost.service.MailService.formatAddress;

/**
 * Created by jake on 8/31/17.
 */
@Service
@Transactional(readOnly = true)
public class SuggestionServiceImpl implements SuggestionService {

    private final MailService mailService;

    private final SettingRepository settingRepository;

    @Autowired
    public SuggestionServiceImpl(MailService mailService, SettingRepository settingRepository) {
        this.mailService = mailService;
        this.settingRepository = settingRepository;
    }

    @Override
    public void handelSuggestion(SuggestionDto dto) {
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

        String[] mailReceivers = mailService.getMailReceivers(Types.UserRole.Administrator);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(mailReceivers);
        message.setSubject(subject);
        message.setText(text);
        message.setReplyTo(replyTo);
        mailService.sendMessage(message, setting);
    }

    private String sanitize(String str) {
        return Jsoup.clean(str, Whitelist.simpleText());
    }
}
