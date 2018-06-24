package com.indiepost.service;

import com.indiepost.NewIndiepostApplication;
import com.indiepost.model.PostInteraction;
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
@SpringBootTest(classes = NewIndiepostApplication.class)
@WebAppConfiguration
public class PostInteractionServiceTests {

    @Inject
    private PostInteractionService service;

    private List<Long> insertedIds = new ArrayList<>();

    @Before
    public void beforeTest() {
        PostInteraction postInteraction = new PostInteraction();
        postInteraction.setCreated(LocalDateTime.now());
        postInteraction.setLastRead(LocalDateTime.now());
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
    public void setBookmark_shouldAddUserReadingProperlyAndReturnId() {
        service.setBookmark(500L);
        PostInteraction postInteraction = service.findOne(insertedIds.get(0));
        assertThat(postInteraction.getBookmarked()).isNotNull();
    }

    @Test
    public void setInvisibleByUserIdAndPostId_shouldAddUserReadingProperly() {
        service.setInvisible(500L);
        PostInteraction postInteraction = service.findOne(insertedIds.get(0));
        assertThat(postInteraction.isVisible()).isFalse();
    }

    @Test
    public void setInvisibleAllByUserId_shouldAddUserReadingProperly() {
        service.setInvisibleAll();
        PostInteraction postInteraction = service.findOne(insertedIds.get(0));
        assertThat(postInteraction.isVisible()).isFalse();
        service.setVisibleAll();
    }

    @Test
    public void setInvisibleAllByUserId_shouldAddUserReadingProperlyAndReturnId() {
        service.setInvisibleAll();
        PostInteraction postInteraction = service.findOne(insertedIds.get(0));
        assertThat(postInteraction.isVisible()).isFalse();
    }

    @After
    public void afterTest() {
        for (Long id : insertedIds) {
            service.deleteById(id);
        }
    }

}
