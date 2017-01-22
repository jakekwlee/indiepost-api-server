package com.indiepost.controller.api;

import com.indiepost.dto.InitialResponse;
import com.indiepost.service.InitialDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by jake on 17. 1. 22.
 */
@RequestMapping("/api/init")
public class InitialDataController {

    private final InitialDataService initialDataService;

    @Autowired
    public InitialDataController(InitialDataService initialDataService) {
        this.initialDataService = initialDataService;
    }


    @RequestMapping(method = RequestMethod.GET)
    public InitialResponse getInitialData() {
        return initialDataService.getInitialData();
    }
}
