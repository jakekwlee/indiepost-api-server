package com.indiepost.controller.api;

import com.indiepost.dto.InitialData;
import com.indiepost.service.InitialDataService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

/**
 * Created by jake on 17. 1. 22.
 */
@RestController
@RequestMapping("/api/init")
public class InitialDataController {

    private final InitialDataService initialDataService;

    @Inject
    public InitialDataController(InitialDataService initialDataService) {
        this.initialDataService = initialDataService;
    }

    @GetMapping
    public InitialData getInitialData() {
        return initialDataService.getInitialData(true);
    }
}
