package com.indiepost.service;

import com.indiepost.NewIndiepostApplication;
import com.indiepost.dto.post.AdminPostResponseDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Arrays;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = NewIndiepostApplication.class)
@WebAppConfiguration
public class AdminPostServiceTests {
    @Autowired
    private AdminPostService service;

    @Test
    @WithMockUser(username = "eunjechoi")
    public void save_shouldSaveTagsOrderProperly() {
        AdminPostResponseDto post = service.createAutosave();
        Long id = post.getId();
        post.setTags(Arrays.asList("tag1", "tag2", "tag3"));
        post.setContent("test content");
        post.setTitle("test title");
        post.setExcerpt("test except");
        post.setCategoryId(2L);
        post.setDisplayName("TEST name");
        service.update(post);
    }
}
