package com.indiepost.migration;

import com.indiepost.NewIndiepostApplicationKt;
import com.indiepost.service.RelatedPostsNormalizer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.inject.Inject;
import javax.transaction.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = NewIndiepostApplicationKt.class)
@WebAppConfiguration
@Transactional
public class PostPostsMigration {
    @Inject
    private RelatedPostsNormalizer relatedPostsNormalizer;

    @Test
    @Rollback(false)
    public void migrate_relatedPostsToManyToManyRelation() {
        relatedPostsNormalizer.normalize();
    }
}
