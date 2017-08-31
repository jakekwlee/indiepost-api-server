package com.indiepost.controller.api;

import com.indiepost.dto.SuggestionDto;
import com.indiepost.service.SuggestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Created by jake on 8/31/17.
 */
@RestController("/api/suggestion")
public class SuggestionController {

    private final SuggestionService suggestionService;

    @Autowired
    public SuggestionController(SuggestionService suggestionService) {
        this.suggestionService = suggestionService;
    }

    @PostMapping
    public void postSuggestion(@RequestBody @Valid SuggestionDto dto) {
        suggestionService.handelSuggestion(dto);
    }
}
