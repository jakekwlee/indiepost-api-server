package com.indiepost;

import com.indiepost.config.ImageConfig;
import com.indiepost.enums.PostEnum;
import com.indiepost.model.Category;
import com.indiepost.model.Post;
import com.indiepost.model.Tag;
import com.indiepost.model.User;
import com.indiepost.repository.PostExcerptRepository;
import com.indiepost.responseModel.admin.InitialResponse;
import com.indiepost.service.AdminService;
import com.indiepost.service.CategoryService;
import com.indiepost.service.PostService;
import com.indiepost.service.UserService;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Set;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = NewIndiepostApplication.class)
@WebAppConfiguration
public class NewIndiepostApplicationTests {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private PostExcerptRepository postExcerptRepository;

    @Autowired
    private ApplicationContext context;

    @Autowired
    private ImageConfig imageConfig;

    private Authentication authentication;

    //    @Test
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

    //    @Test
    public void passwordEncoderShouldEncodeProperly() throws Exception {
        String rawPassword = "admin";
        String encodedPassword = "*4ACFE3202A5FF5CF467898FC58AAB1D615029441";
        Assert.assertEquals(encodedPassword, this.passwordEncoder.encode(rawPassword));
    }

    //    @Test
    public void postServiceWorksCorrectly() throws Exception {
        User user = userService.findById(new Long(1));
        Category category = categoryService.findBySlug("music");
        postService.findAll(1, 50, true);
        postService.findAll(PostEnum.Status.PENDING, user, category, 1, 50, true);
        postService.findByAuthorName("Indiepost", 1, 50, true);
        postService.findByStatus(PostEnum.Status.PUBLISHED, 1, 100, true);
    }

    //    @Test
    public void cmsMetaInformationWorksCorrectly() throws Exception {
        InitialResponse response = adminService.getInitialResponse();
        String dump = ReflectionToStringBuilder.toString(response);
        System.out.println(dump);
    }

//    @Test
//    @Transactional
    public void postExcerptServiceWorks() throws Exception {
        List<Post> posts = postExcerptRepository.findAll(new Long(0), new PageRequest(0, 10000));
        for (Post post : posts) {
            Set<Tag> tags = post.getTags();
            System.out.println(tags.toString());
        }
    }

    @Test
    @Transactional
    public void findAllAuthorNameWorksCorrectly() throws Exception {
        List<String> names = postExcerptRepository.findAllAuthorNames();
        for (String name : names) {
            System.out.println(name);
        }
    }

    //    @Test
    public void imageConfigWiredCorrectly() throws Exception {
        String dump = ReflectionToStringBuilder.toString(imageConfig);
        System.out.println(dump);
    }
}
