package com.indiepost.controller;

import com.indiepost.config.HomeConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by jake on 8/1/16.
 */
@Controller
@RequestMapping("/admin/**")
public class AdminController {

    private final HomeConfig homeConfig;

    @Autowired
    public AdminController(HomeConfig homeConfig) {
        this.homeConfig = homeConfig;
    }

    @GetMapping
    public String getDashboard(Model model) {
        model.addAttribute("cdnUrl", homeConfig.getCdnUrl());
        return "admin/index";
    }
}