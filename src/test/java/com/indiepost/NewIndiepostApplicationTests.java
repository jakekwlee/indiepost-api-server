package com.indiepost;

import com.indiepost.enums.PostEnum;
import com.indiepost.model.Category;
import com.indiepost.model.User;
import com.indiepost.service.CategoryService;
import com.indiepost.service.PostService;
import com.indiepost.service.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = NewIndiepostApplication.class)
@WebAppConfiguration
public class NewIndiepostApplicationTests {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryService categoryService;

    @Test
    public void contextLoads() {
    }

    @Test
    public void passwordEncoderShouldEncodeProperly() throws Exception {
        String rawPassword = "admin";
        String encodedPassword = "*4ACFE3202A5FF5CF467898FC58AAB1D615029441";
        Assert.assertEquals(encodedPassword, this.passwordEncoder.encode(rawPassword));
    }

    @Test
    public void postServiceWorksCorrectly() throws Exception {
        User user = userService.findById(1);
        Category category = categoryService.findBySlug("music");
        postService.findAll(1, 50);
        postService.findAll(PostEnum.Status.QUEUED, user, category, 1, 50);
        postService.findByAuthorName("Indiepost", 1, 50);
        postService.findByStatusOrderByAsc(PostEnum.Status.PUBLISHED, 1, 100);
    }
}
