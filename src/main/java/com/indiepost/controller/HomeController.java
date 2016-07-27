package com.indiepost.controller;

import com.indiepost.model.Post;
import com.indiepost.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * Created by jake on 7/27/16.
 */
@Controller
public class HomeController {

    @Autowired
    private PostRepository postRepository;

    @RequestMapping("/")
    public String home(Model model) {
        List<Post> posts = postRepository.findAllByOrderByIdDesc(new PageRequest(0, 20));
        model.addAttribute("posts", posts);
        return "home";
    }
}
