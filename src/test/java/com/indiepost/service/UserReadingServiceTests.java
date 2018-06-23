package com.indiepost.service;

import com.indiepost.NewIndiepostApplication;
import com.indiepost.model.UserReading;
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
public class UserReadingServiceTests {

    @Inject
    private UserReadingService service;

    private List<Long> insertedIds = new ArrayList<>();

    @Before
    public void beforeTest() {
        UserReading userReading = new UserReading();
        userReading.setCreated(LocalDateTime.now());
        userReading.setLastRead(LocalDateTime.now());
        userReading.setBookmarked(false);
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
        service.setBookmark(1L, 500L);
        UserReading userReading = service.findOne(insertedIds.get(0));
        assertThat(userReading.isBookmarked()).isTrue();
        assertThat(userReading.getBookmarkedAt()).isNotNull();
    }

    @Test
    public void setInvisibleByUserIdAndPostId_shouldAddUserReadingProperly() {
        service.setInvisible(1L, 500L);
        UserReading userReading = service.findOne(insertedIds.get(0));
        assertThat(userReading.isVisible()).isFalse();
    }

    @Test
    public void setInvisibleAllByUserId_shouldAddUserReadingProperly() {
        service.setInvisibleAllByUserId(1L);
        UserReading userReading = service.findOne(insertedIds.get(0));
        assertThat(userReading.isVisible()).isFalse();
        service.setVisibleAllByUserId(1L);
    }

    @Test
    public void setInvisibleAllByUserId_shouldAddUserReadingProperlyAndReturnId() {
        service.setInvisibleAllByUserId(1L);
        UserReading userReading = service.findOne(insertedIds.get(0));
        assertThat(userReading.isVisible()).isFalse();
    }

    @After
    public void afterTest() {
        for (Long id : insertedIds) {
            service.deleteById(id);
        }
    }

}
