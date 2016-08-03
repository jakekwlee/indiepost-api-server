package com.indiepost.controller;

import com.indiepost.model.Post;
import com.indiepost.model.User;
import com.indiepost.service.PostService;
import com.indiepost.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

/**
 * Created by jake on 7/27/16.
 */
@Controller
public class HomeController {

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @RequestMapping("/")
    public String getHome(Model model, Authentication authentication, Principal principal) {
        getUserFromPrincipal(model, authentication, principal);
        List<Post> posts = postService.findAllForUser(0, 10);
        model.addAttribute("posts", posts);
        return "home";
    }

    @RequestMapping("/page/{page}")
    public String getHome(Model model, Authentication authentication, Principal principal, @PathVariable("page") int page) {
        getUserFromPrincipal(model, authentication, principal);
        List<Post> posts = postService.findAllForUser(page, 10);
        model.addAttribute("posts", posts);
        return "home";
    }

    @RequestMapping("/category/{slug}")
    public String getHomeByCategory(Model model, Authentication authentication, Principal principal, @PathVariable("slug") String slug) {
        getUserFromPrincipal(model, authentication, principal);
        List<Post> posts = postService.findByCategorySlugForUser(slug, 0, 10);
        String title = "";
        if (posts.size() > 0) {
            title = posts.get(0).getCategory().getName();
        }
        model.addAttribute("title", title);
        model.addAttribute("posts", posts);
        return "home";
    }

    @RequestMapping("/category/{slug}/{page}")
    public String getHomeByCategory(Model model, Authentication authentication, Principal principal, @PathVariable("slug") String slug, @PathVariable int page) {
        getUserFromPrincipal(model, authentication, principal);
        List<Post> posts = postService.findByCategorySlugForUser(slug, page, 10);
        String title = "";
        if (posts.size() > 0) {
            title = posts.get(0).getCategory().getName();
        }
        model.addAttribute("title", title);
        model.addAttribute("posts", posts);
        return "home";
    }

    @RequestMapping("/author/{username}")
    public String getHomeByAuthor(Model model, Authentication authentication, Principal principal, @PathVariable("username") String username) {
        getUserFromPrincipal(model, authentication, principal);
        List<Post> posts = postService.findByAuthoUsernamerForUser(username, 0, 10);
        model.addAttribute("posts", posts);
        return "home";
    }

    @RequestMapping("/archives/{id}")
    public String getPost(Model model, Authentication authentication, Principal principal, @PathVariable int id) {
        getUserFromPrincipal(model, authentication, principal);
        Post post = postService.findByIdForUser(id);
        model.addAttribute("post", post);
        return "post";
    }

    private void getUserFromPrincipal(Model model, Authentication authentication, Principal principal) {
        if (principal != null) {
            String username = principal.getName();
            User user = userService.findByUsername(username);
            model.addAttribute("user", user);
            model.addAttribute("hasRoleEditor", hasRoleEditor(authentication));
        }
    }

    private Boolean hasRoleEditor(Authentication authentication) {
        return authentication.getAuthorities().contains(new SimpleGrantedAuthority(User.Roles.Editor.toString()));
    }
}
