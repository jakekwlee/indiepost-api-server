package com.indiepost.controller;

import com.indiepost.config.AppConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by jake on 8/1/16.
 */
public class AdminController {

    private final AppConfig config;

    @Autowired
    public AdminController(AppConfig config) {
        this.config = config;
    }

    @GetMapping
    public String getDashboard(Model model) {
        model.addAttribute("cdnUrl", config.getCdnUrl());
        return "admin/index";
    }
}