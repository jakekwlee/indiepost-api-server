package com.indiepost.service;

import com.indiepost.NewIndiepostApplication;
import com.indiepost.model.PostReading;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.inject.Inject;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = NewIndiepostApplication.class)
@WebAppConfiguration
public class PostReadingServiceTests {

    @Inject
    private PostUserInteractionService service;

    @Test
    public void add_shouldAddUserReadingProperlyAndReturnId() {
        Long insertedId = service.add(4L, 500L);
        assertThat(insertedId).isNotNull();
    }

    @Test
    @WithMockUser(username = "auth0|5b213cd8064de34cde981b47")
    public void setInvisibleByPostId_shouldAddUserReadingProperly() {
        service.setInvisible(8051L);
        PostReading postReading = service.findOne(1L);
        assertThat(postReading.isVisible()).isFalse();
    }

    @Test
    @WithMockUser(username = "auth0|5b213cd8064de34cde981b47")
    public void setInvisibleAllByUserId_shouldAddUserReadingProperly() {
        service.setInvisibleAll();
        PostReading postReading = service.findOne(1L);
        assertThat(postReading.isVisible()).isFalse();
        service.setVisibleAll();
    }

    @Test
    @WithMockUser(username = "auth0|5b213cd8064de34cde981b47")
    public void setInvisibleAllByUserId_shouldAddUserReadingProperlyAndReturnId() {
        service.setInvisibleAll();
        PostReading postReading = service.findOne(1L);
        assertThat(postReading.isVisible()).isFalse();
    }
}
