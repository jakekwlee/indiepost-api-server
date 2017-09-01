package com.indiepost.service;

import com.indiepost.dto.SuggestionDto;
import com.indiepost.model.Setting;
import com.indiepost.repository.SettingRepository;
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
        String proposerName = dto.getProposer();
        String proposerEmail = dto.getEmail();
        String text = dto.getContent()
                .concat("\n\nContact: ")
                .concat(dto.getContact());
        String from = formatAddress(proposerName, setting.getMailUsername());
        String replyTo = formatAddress(proposerName, proposerEmail);

//        String[] mailingList = mailService.getMailReceivers(Types.UserRole.EditorInChief);
        String[] mailReceivers = {formatAddress(
                "인디포스트 관리자",
                "sysadmin@indiepost.co.kr>"
        )};
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(mailReceivers);
        message.setSubject(dto.getSubject());
        message.setText(text);
        message.setReplyTo(replyTo);
        mailService.sendMessage(message, setting);
    }


}
