package com.indiepost.task;

import com.indiepost.config.AppConfig;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by jake on 17. 3. 12.
 */
@Component
public class ElasticsearchIndexBuilder implements ApplicationListener<ApplicationReadyEvent> {

    private final AppConfig appConfig;

    @PersistenceContext
    private EntityManager entityManager;

    @Inject
    public ElasticsearchIndexBuilder(AppConfig appConfig) {
        this.appConfig = appConfig;
    }

    /**
     * Create an initial Lucene bulkIndex for the data already present in the
     * database.
     * This method is called when Spring's startup.
     */
    @Override
    public void onApplicationEvent(final ApplicationReadyEvent event) {
        if (!appConfig.isBuildSearchIndex()) {
            return;
        }
        try {
            // TODO build search bulkIndex
        } catch (Exception e) {
            System.out.println(
                    "An error occurred trying to build the search index: " +
                            e.toString());
        }
    }
}