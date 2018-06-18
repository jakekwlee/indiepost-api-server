package com.indiepost.repository;

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
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = NewIndiepostApplication.class)
@WebAppConfiguration
@Transactional
public class UserReadingRepositoryTests {
    @Inject
    private UserReadingRepository repository;

    private List<Long> insertedIds = new ArrayList<>();

    @Before
    public void beforeTest() {
        UserReading userReading = new UserReading();
        userReading.setFirstRead(LocalDateTime.now());
        userReading.setLastRead(LocalDateTime.now());
        repository.save(userReading, 1L, 500L);
        Long id = userReading.getId();
        insertedIds.add(id);
    }

    @After
    public void afterTest() {
        for (Long id : insertedIds) {
            repository.deleteById(id);
        }
    }

    @Test
    public void findOneByUserIdAndPostId_shouldReturnAnUserReadingProperly() {
        UserReading userReading = repository.findOneByUserIdAndPostId(1L, 500L);
        assertThat(userReading).isNotNull();
    }

    @Test
    public void findOne_shouldReturnAnUserReadingProperly() {
        UserReading userReading = repository.findOne(insertedIds.get(0));
        assertThat(userReading).isNotNull();
    }

    @Test
    public void setAllReadingInvisible_shouldUpdateUserReadingProperly() {
        repository.setInvisibleAll(1L);
        UserReading userReading = repository.findOne(insertedIds.get(insertedIds.size() - 1));
        assertThat(userReading.isVisible()).isFalse();
    }
}
