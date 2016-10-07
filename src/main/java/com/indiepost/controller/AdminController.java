package com.indiepost.controller;

import com.indiepost.model.User;
import com.indiepost.service.PostService;
import com.indiepost.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;

/**
 * Created by jake on 8/1/16.
 */
@Controller
@RequestMapping("/cms")
public class AdminController {
    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.GET)
    public String getDashboard(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        model.addAttribute("user", user);
        return "admin/dashboard";
    }
}
