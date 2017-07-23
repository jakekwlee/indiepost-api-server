package com.indiepost.task;

import com.indiepost.config.AppConfig;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by jake on 17. 3. 12.
 */
@Component
public class SearchIndexBuilder implements ApplicationListener<ApplicationReadyEvent> {

    private final AppConfig appConfig;
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public SearchIndexBuilder(AppConfig appConfig) {
        this.appConfig = appConfig;
    }

    /**
     * Create an initial Lucene index for the data already present in the
     * database.
     * This method is called when Spring's startup.
     */
    @Override
    public void onApplicationEvent(final ApplicationReadyEvent event) {
        if (!appConfig.isBuildSearchIndex()) {
            return;
        }
        try {
            FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
            fullTextEntityManager.createIndexer().startAndWait();
        } catch (InterruptedException e) {
            System.out.println(
                    "An error occurred trying to build the search index: " +
                            e.toString());
        }
    }
}