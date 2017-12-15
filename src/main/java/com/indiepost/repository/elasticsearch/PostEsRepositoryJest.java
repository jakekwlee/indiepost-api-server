package com.indiepost.repository.elasticsearch;

import com.indiepost.model.elasticsearch.PostEs;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.JestResult;
import io.searchbox.core.*;
import io.searchbox.indices.CreateIndex;
import io.searchbox.indices.DeleteIndex;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

@Repository
public class PostEsRepositoryJest implements PostEsRepository {

    private static final String INDEX_NAME = "posts";

    private static final String TYPE_NAME = "post";

    private static final Logger log = LoggerFactory.getLogger(PostEsRepositoryJest.class);

    @Inject
    private JestClientFactory clientFactory;

    @Inject
    private String indexSettings;

    public PostEsRepositoryJest(JestClientFactory clientFactory, String indexSettings) {
        this.clientFactory = clientFactory;
        this.indexSettings = indexSettings;
    }

    @Override
    public boolean testConnection() {
        JestClient client = getClient();
        return client != null;
    }

    @Override
    public boolean createIndex() {
        CreateIndex createIndex = new CreateIndex.Builder(INDEX_NAME).settings(indexSettings)
                .build();
        try {
            JestResult result = getClient().execute(createIndex);
            if (result.isSucceeded()) {
                return true;
            }
            log.error(result.getErrorMessage());
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }

    @Override
    public boolean deleteIndex() {
        DeleteIndex deleteIndex = new DeleteIndex.Builder(INDEX_NAME).build();
        try {
            JestResult result = getClient().execute(deleteIndex);
            return result.isSucceeded();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }

    @Override
    public void rebuildIndices(List<PostEs> posts) {
        deleteIndex();
        if (createIndex()) {
            save(posts);
        }
    }

    @Override
    public List<PostEs> search(String text, String status, Pageable pageable) {
        return null;
    }

    @Override
    public PostEs findById(Long id) {
        Get get = new Get.Builder(INDEX_NAME, id.toString())
                .type(TYPE_NAME)
                .build();
        try {
            JestResult result = getClient().execute(get);
            return result.getSourceAsObject(PostEs.class);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public void save(List<PostEs> posts) {
        if (posts.isEmpty()) {
            return;
        }
        Bulk.Builder bulkBuilder = new Bulk.Builder()
                .defaultIndex(INDEX_NAME)
                .defaultType(TYPE_NAME);
        try {
            for (PostEs post : posts) {
                bulkBuilder.addAction(prepareIndex(post));
            }
            Bulk bulk = bulkBuilder.build();
            JestResult result = getClient().execute(bulk);
            if (!result.isSucceeded()) {
                log.error(result.getJsonString());
            } else {
                log.info(result.getJsonString());
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public void save(PostEs post) {
        try {
            Index index = prepareIndex(post);
            getClient().execute(index);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public void update(PostEs post) {
        try {
            Update update = new Update
                    .Builder(serializeToJson(post))
                    .index(INDEX_NAME)
                    .type(TYPE_NAME)
                    .build();
            getClient().execute(update);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public void deleteById(Long id) {
        Delete delete = prepareDelete(id);
        try {
            getClient().execute(delete);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public void deleteByIdIn(List<Long> ids) {
        if (ids.isEmpty()) {
            return;
        }
        Bulk.Builder bulkBuilder = new Bulk.Builder()
                .defaultIndex(INDEX_NAME);
        try {
            List<Delete> deletes = new ArrayList<>();
            for (Long id : ids) {
                deletes.add(prepareDelete(id));
            }
            bulkBuilder.addAction(deletes);
            Bulk bulk = bulkBuilder.build();
            getClient().execute(bulk);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public void delete(PostEs postEs) {
        deleteById(postEs.getId());
    }

    private Index prepareIndex(PostEs post) throws IOException {
        return new Index
                .Builder(post)
                .index(INDEX_NAME)
                .type(TYPE_NAME)
                .build();
    }

    private Delete prepareDelete(Long id) {
        return new Delete.Builder(id.toString())
                .index(INDEX_NAME)
                .build();
    }

    private String serializeToJson(PostEs post) throws IOException {
        XContentBuilder builder = jsonBuilder().startObject()
                .field("title", post.getTitle())
                .field("excerpt", post.getExcerpt())
                .field("content", post.getContent())
                .field("bylineName", post.getBylineName());
        if (!post.getTags().isEmpty()) {
            builder.field("tags", post.getTags());
        }
        if (!post.getContributors().isEmpty()) {
            builder.field("contributors", post.getContributors());
        }
        return builder.endObject().toString();

    }

    private JestClient getClient() {
        return clientFactory.getObject();
    }
}
