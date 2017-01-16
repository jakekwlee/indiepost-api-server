package com.indiepost.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by jake on 17. 1. 4.
 */
@Controller
public class HomeController {

    @RequestMapping(
            value = {"/", "/archive/**", "/category/**", "/page/**"},
            method = RequestMethod.GET)
    public String Home() {
        return "index";
    }

    //    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        return "admin/index";
    }
}
