package com.indiepost.controller;

import com.indiepost.dto.Inquiry;
import com.indiepost.dto.Suggestion;
import com.indiepost.service.MailService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.validation.Valid;

/**
 * Created by jake on 8/31/17.
 */
@RestController
@RequestMapping("/mail")
public class MailController {

    private final MailService mailService;

    @Inject
    public MailController(MailService mailService) {
        this.mailService = mailService;
    }

    @PostMapping("/suggestion")
    public void postSuggestion(@RequestBody @Valid Suggestion dto) {
        mailService.sendSuggestion(dto);
    }

    @PostMapping("/inquiry")
    public void postInquiry(@RequestBody @Valid Inquiry dto) {
        mailService.sendInquiry(dto);
    }
}
