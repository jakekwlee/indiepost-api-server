package com.indiepost.repository;

import com.indiepost.NewIndiepostApplication;
import com.indiepost.dto.post.PostQuery;
import com.indiepost.dto.post.Title;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.inject.Inject;
import java.util.List;

import static com.indiepost.enums.Types.PostStatus.PUBLISH;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = NewIndiepostApplication.class)
@WebAppConfiguration
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
