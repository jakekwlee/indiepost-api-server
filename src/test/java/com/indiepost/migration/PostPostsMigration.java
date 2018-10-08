package com.indiepost.migration;

import com.indiepost.NewIndiepostApplication;
import com.indiepost.service.RelatedPostsNormalizer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.inject.Inject;
import javax.transaction.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = NewIndiepostApplication.class)
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
