package com.indiepost.controller;

import com.indiepost.model.Category;
import com.indiepost.model.Post;
import com.indiepost.model.Role;
import com.indiepost.model.User;
import com.indiepost.service.CategoryService;
import com.indiepost.service.PostService;
import com.indiepost.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;
import java.util.List;

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

    @Autowired
    private CategoryService categoryService;

    @RequestMapping(method = RequestMethod.GET)
    public String getDashboard(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        model.addAttribute("user", user);
        return "admin/dashboard";
    }

    @RequestMapping("/posts")
    public String getPosts(Model model, Authentication authentication, Principal principal) {
        List<Post> posts = postService.findAll(0, 150);
        User user = userService.findByUsername(principal.getName());
        model.addAttribute("hasEditRole", hasRoleEditorInChief(authentication));
        model.addAttribute("posts", posts);
        model.addAttribute("user", user);
        return "admin/posts";
    }

    @RequestMapping("posts/new")
    public String editPost(Model model, Authentication authentication, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        List<Category> categories = categoryService.findAll();
        List<User> authors = userService.findByRolesEnum(User.Roles.Author);
        model.addAttribute("categories", categories);
        model.addAttribute("user", user);
        model.addAttribute("authors", authors);
        model.addAttribute("statuses", Post.Status.values());
        return "admin/newPost";
    }

    private Boolean hasRoleEditorInChief(Authentication authentication) {
        return authentication.getAuthorities().contains(new SimpleGrantedAuthority(User.Roles.EditorInChief.toString()));
    }
}
