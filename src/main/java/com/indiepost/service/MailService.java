package com.indiepost.service;

import com.indiepost.dto.InquiryDto;
import com.indiepost.dto.SuggestionDto;
import com.indiepost.enums.Types;
import com.indiepost.model.Setting;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.springframework.mail.SimpleMailMessage;

/**
 * Created by jake on 8/31/17.
 */
public interface MailService {
    static String formatAddress(String mailSenderName, String mailAddress) {
        return mailSenderName.concat(" <").concat(mailAddress).concat(">");
    }

    static String sanitize(String str) {
        return Jsoup.clean(str, Whitelist.simpleText());
    }

    void sendMessage(SimpleMailMessage message);

    void sendMessage(SimpleMailMessage message, Setting setting);

    String[] getMailReceivers(Types.UserRole role);

    void sendSuggestion(SuggestionDto dto);

    void sendInquiry(InquiryDto dto);
}
