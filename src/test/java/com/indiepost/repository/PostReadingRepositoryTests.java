package com.indiepost.repository;

import com.indiepost.NewIndiepostApplication;
import com.indiepost.model.PostReading;
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
public class PostReadingRepositoryTests {
    @Inject
    private PostReadingRepository repository;

    private List<Long> insertedIds = new ArrayList<>();

    @Before
    public void beforeTest() {
        PostReading postReading = new PostReading();
        postReading.setCreated(LocalDateTime.now());
        postReading.setLastRead(LocalDateTime.now());
        Long id = repository.save(postReading, 1L, 500L);
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
        PostReading postReading = repository.findOneByUserIdAndPostId(1L, 500L);
        assertThat(postReading).isNotNull();
    }

    @Test
    public void findOne_shouldReturnAnUserReadingProperly() {
        PostReading postReading = repository.findOne(insertedIds.get(0));
        assertThat(postReading).isNotNull();
    }
}
