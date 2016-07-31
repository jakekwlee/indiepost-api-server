package com.indiepost.controller;

import com.indiepost.model.Post;
import com.indiepost.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * Created by jake on 7/27/16.
 */
@Controller
public class HomeController {

    @Autowired
    private PostService postService;

    @RequestMapping("/")
    public String getHome(Model model) {
        List<Post> posts = postService.findAllForUser(0, 10);
        model.addAttribute("posts", posts);
        return "home";
    }

    @RequestMapping("/category/{slug}")
    public String getHomeByCategory(Model model, @PathVariable("slug") String slug) {
        List<Post> posts = postService.findByCategorySlugForUser(slug, 0, 10);
        model.addAttribute("posts", posts);
        return "home";
    }

    @RequestMapping("/author/{username}")
    public String getHomeByAuthor(Model model, @PathVariable("username") String username) {
        List<Post> posts = postService.findByAuthoUsernamerForUser(username, 0, 10);
        model.addAttribute("posts", posts);
        return "home";
    }

    @RequestMapping("/category/{slug}/{id}")
    public String getHomeByAuthor(Model model, @PathVariable("slug") String slug, @PathVariable int id) {
        Post post = postService.findByIdForUser(id);
        model.addAttribute("post", post);
        return "post";
    }
}
