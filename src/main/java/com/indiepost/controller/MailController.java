package com.indiepost.controller;

import com.indiepost.dto.InquiryDto;
import com.indiepost.dto.SuggestionDto;
import com.indiepost.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Created by jake on 8/31/17.
 */
@RestController
@RequestMapping("/mail")
public class MailController {

    private final MailService mailService;

    @Autowired
    public MailController(MailService mailService) {
        this.mailService = mailService;
    }

    @PostMapping("/suggestion")
    public void postSuggestion(@RequestBody @Valid SuggestionDto dto) {
        mailService.sendSuggestion(dto);
    }

    @PostMapping("/inquiry")
    public void postInquiry(@RequestBody @Valid InquiryDto dto) {
        mailService.sendInquiry(dto);
    }
}
