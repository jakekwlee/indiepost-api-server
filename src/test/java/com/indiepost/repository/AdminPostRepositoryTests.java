package com.indiepost.repository;

import com.indiepost.NewIndiepostApplication;
import com.indiepost.dto.post.PostQuery;
import com.indiepost.dto.post.Title;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;

import static com.indiepost.enums.Types.PostStatus.PUBLISH;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = NewIndiepostApplication.class)
@WebAppConfiguration
@Transactional
public class AdminPostRepositoryTests {
    @Inject
    private AdminPostRepository repository;

    @Test
    public void getTitleList_shouldReturnPostTitlesProperly() {
        List<Title> titleList = repository.getTitleList();
        PostQuery query = new PostQuery.Builder(PUBLISH).build();
        assertThat(titleList).hasSize(repository.count(query).intValue());
    }
}
