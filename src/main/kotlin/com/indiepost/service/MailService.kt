package com.indiepost.service

import com.indiepost.dto.Inquiry
import com.indiepost.dto.Suggestion
import com.indiepost.enums.Types
import com.indiepost.model.Setting
import org.jsoup.Jsoup
import org.jsoup.safety.Whitelist
import org.springframework.mail.SimpleMailMessage

/**
 * Created by jake on 8/31/17.
 */
interface MailService {

    fun sendMessage(message: SimpleMailMessage)

    fun sendMessage(message: SimpleMailMessage, setting: Setting)

    fun getMailReceivers(role: Types.UserRole): Array<String>

    fun sendSuggestion(dto: Suggestion)

    fun sendInquiry(dto: Inquiry)

    companion object {

        fun formatAddress(mailSenderName: String, mailAddress: String): String {
            return "$mailSenderName <$mailAddress>"
        }

        fun sanitize(str: String): String {
            return Jsoup.clean(str, Whitelist.simpleText())
        }
    }
}
