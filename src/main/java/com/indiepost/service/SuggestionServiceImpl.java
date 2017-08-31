package com.indiepost.service;

import com.indiepost.dto.SuggestionDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by jake on 8/31/17.
 */
@Service
@Transactional(readOnly = true)
public class SuggestionServiceImpl implements SuggestionService {

    private final MailService mailService;

    @Autowired
    public SuggestionServiceImpl(MailService mailService) {
        this.mailService = mailService;
    }

    @Override
    public void handelSuggestion(SuggestionDto dto) {
        String from = dto.getProposer().concat(" <indiepost.mail@gmail.com>");
        String text = dto.getContent()
                .concat("\n\nContact: ")
                .concat(dto.getContact());
        String replyTo;
        if (StringUtils.isEmpty(dto.getEmail()) || dto.getEmail().contains("no-reply")) {
            replyTo = dto.getProposer().concat(" <no-reply@deny.net>");
        } else {
            replyTo = dto.getProposer() + " <" + dto.getEmail() + ">";
        }
//        String[] mailingList = mailService.getMailReceivers(Types.UserRole.EditorInChief);
        String[] mailingList = {"시스템 관리자 <sysadmin@indiepost.co.kr>"};

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(mailingList);
        message.setSubject(dto.getSubject());
        message.setText(text);
        message.setReplyTo(replyTo);
        mailService.sendMessage(message);
    }

}
