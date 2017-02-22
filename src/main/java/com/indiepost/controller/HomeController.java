package com.indiepost.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.indiepost.service.InitialDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jake on 17. 1. 4.
 */
@Controller
public class HomeController {

    @Autowired
    private InitialDataService initialDataService;

    @RequestMapping(
            value = {"/", "/archive/**", "/category/**", "/page/**", "/posts/**"},
            method = RequestMethod.GET)
    public String Home(Model model, HttpServletRequest request) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> req = new HashMap<>();

        String root = request.getServletPath().equals("/index.html") ? "/" : request.getServletPath();

        if (request.getQueryString() != null)
            req.put("location", String.format("%s?%s", root, request.getQueryString()));
        else
            req.put("location", root);

        model.addAttribute("req", mapper.writeValueAsString(req));
        model.addAttribute("initialData", mapper.writeValueAsString(initialDataService.getInitialData()));
        return "index";
    }

    //    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        return "admin/index";
    }
}
