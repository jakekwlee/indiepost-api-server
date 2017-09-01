package com.indiepost.service;

import com.indiepost.enums.Types;
import com.indiepost.model.Setting;
import org.springframework.mail.SimpleMailMessage;

/**
 * Created by jake on 8/31/17.
 */
public interface MailService {
    void sendMessage(SimpleMailMessage message);

    void sendMessage(SimpleMailMessage message, Setting setting);

    String[] getMailReceivers(Types.UserRole role);

    static String formatAddress(String mailSenderName, String mailAddress) {
        return mailSenderName.concat(" <").concat(mailAddress).concat(">");
    }
}
