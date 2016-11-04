package com.indiepost.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by jake on 8/1/16.
 */
@Controller
@RequestMapping("/cms")
public class AdminController {
    @RequestMapping(method = RequestMethod.GET)
    public String getDashboard() {
        return "admin/dashboard";
    }
}
