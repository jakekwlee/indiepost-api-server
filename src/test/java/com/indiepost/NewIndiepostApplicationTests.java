package com.indiepost;

import com.indiepost.model.Post;
import com.indiepost.repository.PostExcerptRepository;
import com.indiepost.service.AdminService;
import com.indiepost.service.CategoryService;
import com.indiepost.service.PostService;
import com.indiepost.service.UserService;
import dto.response.AdminPostResponseDto;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = NewIndiepostApplication.class)
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
    private AdminService adminService;

    @Autowired
    private PostExcerptRepository postExcerptRepository;

    @Autowired
    private ApplicationContext context;

    private Authentication authentication;

    @Test
    public void contextLoads() {
    }

    // @Before
    public void init() {
        AuthenticationManager authenticationManager = this.context
                .getBean(AuthenticationManager.class);
        this.authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken("indiepost", "*4ACFE3202A5FF5CF467898FC58AAB1D615029441"));
    }

    //    @After
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
    @Transactional
    public void convertPostToAdminPostResponseWorks() throws Exception {
        Post post = postService.findById(new Long(81));
        AdminPostResponseDto adminPostResponseDto = new AdminPostResponseDto();
//        ??(post, adminPostResponseDto);
        assertNotNull(adminPostResponseDto.getId());
//        assertEquals(post.getTags().size(), adminPostResponseDto.getTags().size());
        assertEquals(post.getAuthor().getId(), adminPostResponseDto.getAuthorId());
    }

    //    @Test
    @Transactional
    public void convertAdminPostRequestToPostWorks() throws Exception {
        Post post = postService.findById(new Long(81));
        AdminPostResponseDto adminPostResponseDto = new AdminPostResponseDto();
//        ??(post, adminPostResponseDto);
        Post result;
        // Todo
    }
}
