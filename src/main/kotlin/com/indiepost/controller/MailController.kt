package com.indiepost.controller

import com.indiepost.dto.Inquiry
import com.indiepost.dto.Suggestion
import com.indiepost.service.MailService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

import javax.inject.Inject
import javax.validation.Valid

/**
 * Created by jake on 8/31/17.
 */
@RestController
@RequestMapping("/mail")
class MailController @Inject constructor(private val mailService: MailService) {

    @PostMapping("/suggestion")
    fun postSuggestion(@RequestBody @Valid dto: Suggestion) {
        mailService.sendSuggestion(dto)
    }

    @PostMapping("/inquiry")
    fun postInquiry(@RequestBody @Valid dto: Inquiry) {
        mailService.sendInquiry(dto)
    }
}
