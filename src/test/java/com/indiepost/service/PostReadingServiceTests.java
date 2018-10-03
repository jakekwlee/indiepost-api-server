package com.indiepost.service;

import com.indiepost.NewIndiepostApplicationKt;
import com.indiepost.model.PostReading;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = NewIndiepostApplicationKt.class)
@WebAppConfiguration
public class PostReadingServiceTests {

    @Inject
    private PostUserInteractionService service;

    private List<Long> insertedIds = new ArrayList<>();

    @Before
    public void beforeTest() {
        PostReading postReading = new PostReading();
        postReading.setCreated(LocalDateTime.now());
        postReading.setLastRead(LocalDateTime.now());
        Long id = service.add(1L, 500L);
        insertedIds.add(id);
    }

    @Test
    public void add_shouldAddUserReadingProperlyAndReturnId() {
        Long insertedId = service.add(4L, 500L);
        assertThat(insertedId).isNotNull();
        insertedIds.add(insertedId);
    }

    @Test
    public void setInvisibleByUserIdAndPostId_shouldAddUserReadingProperly() {
        service.setInvisible(500L);
        PostReading postReading = service.findOne(insertedIds.get(0));
        assertThat(postReading.isVisible()).isFalse();
    }

    @Test
    public void setInvisibleAllByUserId_shouldAddUserReadingProperly() {
        service.setInvisibleAll();
        PostReading postReading = service.findOne(insertedIds.get(0));
        assertThat(postReading.isVisible()).isFalse();
        service.setVisibleAll();
    }

    @Test
    public void setInvisibleAllByUserId_shouldAddUserReadingProperlyAndReturnId() {
        service.setInvisibleAll();
        PostReading postReading = service.findOne(insertedIds.get(0));
        assertThat(postReading.isVisible()).isFalse();
    }

    @After
    public void afterTest() {
        for (Long id : insertedIds) {
            service.deleteById(id);
        }
    }

}
