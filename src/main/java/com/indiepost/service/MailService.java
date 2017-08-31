package com.indiepost.service;

import com.indiepost.enums.Types;
import org.springframework.mail.SimpleMailMessage;

/**
 * Created by jake on 8/31/17.
 */
public interface MailService {
    void sendMessage(SimpleMailMessage message);

    String[] getMailReceivers(Types.UserRole role);
}
