package com.indiepost;

import com.indiepost.enums.PostEnum;
import com.indiepost.model.Category;
import com.indiepost.model.User;
import com.indiepost.service.ManagementService;
import com.indiepost.service.CategoryService;
import com.indiepost.service.PostService;
import com.indiepost.service.UserService;
import com.indiepost.viewModel.cms.TopLevelResponse;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @Autowired
    private ManagementService managementService;

    @Autowired
    private ApplicationContext context;

    private Authentication authentication;

    @Test
    public void contextLoads() {
    }

    @Before
    public void init() {
        AuthenticationManager authenticationManager = this.context
                .getBean(AuthenticationManager.class);
        this.authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken("indiepost", "*4ACFE3202A5FF5CF467898FC58AAB1D615029441"));
    }

    @After
    public void close() {
        SecurityContextHolder.clearContext();
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

    @Test
    public void cmsInitialResponseWorksCorrectly() throws Exception {
        TopLevelResponse response = managementService.getInitialState();
        String dump = ReflectionToStringBuilder.toString(response);
        System.out.println(dump);
    }
}
