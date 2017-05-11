package com.indiepost.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by jake on 8/1/16.
 */
@Controller
@RequestMapping("/admin/**")
public class AdminController {

    @GetMapping
    public String getDashboard() {
        return "admin/index";
    }
}