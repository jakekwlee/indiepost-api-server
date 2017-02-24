package com.indiepost.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.indiepost.dto.InitialData;
import com.indiepost.dto.RenderingServerResponse;
import com.indiepost.service.InitialDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

/**
 * Created by jake on 17. 1. 4.
 */
@Controller
public class HomeController {

    private final InitialDataService initialDataService;

    private final RestTemplate restTemplate;

    @Autowired
    public HomeController(InitialDataService initialDataService, RestTemplate restTemplate) {
        this.initialDataService = initialDataService;
        this.restTemplate = restTemplate;
    }

    @RequestMapping(
            value = {"/", "/archive/**", "/category/**", "/page/**", "/posts/**"},
            method = RequestMethod.GET)
    public String Home(Model model) throws JsonProcessingException {
        final String uri = "http://localhost:3000/home";
        InitialData initialData = initialDataService.getInitialData();
        try {
            RenderingServerResponse response = restTemplate.postForObject(uri, initialData, RenderingServerResponse.class);
            model.addAttribute("markup", response.getMarkup());
            model.addAttribute("state", response.getState());
        } catch (ResourceAccessException e) {
            e.printStackTrace();
            model.addAttribute("markup", "");
            model.addAttribute("state", "{}");
        }
        return "index";
    }

    //    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        return "admin/index";
    }
}
