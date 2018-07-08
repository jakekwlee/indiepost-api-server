package com.indiepost.repository.elasticsearch;

import com.indiepost.enums.Types;
import com.indiepost.model.User;
import com.indiepost.model.Word;
import com.indiepost.model.elasticsearch.PostEs;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.JestResult;
import io.searchbox.cluster.Health;
import io.searchbox.core.*;
import io.searchbox.indices.CreateIndex;
import io.searchbox.indices.DeleteIndex;
import io.searchbox.indices.IndicesExists;
import io.searchbox.indices.settings.UpdateSettings;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class PostEsRepositoryJest implements com.indiepost.repository.elasticsearch.PostEsRepository {

    private static final String INDEX_NAME = "posts";

    private static final String TYPE_NAME = "post";

    private static final Logger log = LoggerFactory.getLogger(PostEsRepositoryJest.class);

    private final JestClientFactory clientFactory;

    private final String indexSettings;

    @Inject
    public PostEsRepositoryJest(JestClientFactory clientFactory, String indexSettings) {
        this.clientFactory = clientFactory;
        this.indexSettings = indexSettings;
    }


    @Override
    public boolean testConnection() {
        try {
            io.searchbox.client.JestClient client = getClient();
            Health health = new Health.Builder().build();
            JestResult result = client.execute(health);
            if (result.isSucceeded()) {
                log.info("Elasticsearch: Connection established successful.");
                return true;
            }
            log.error("Elasticsearch: Connection failed.");
            log.error(result.getJsonString());
            return false;
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }

    @Override
    public boolean indexExist() {
        try {
            IndicesExists indicesExists = new IndicesExists.Builder(INDEX_NAME).build();
            return getClient().execute(indicesExists).isSucceeded();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }

    @Override
    public boolean createIndex() {
        try {
            CreateIndex createIndex = new CreateIndex
                    .Builder(INDEX_NAME)
                    .settings(indexSettings)
                    .build();
            JestResult result = getClient().execute(createIndex);
            if (result.isSucceeded()) {
                log.info("Elasticsearch: Index <posts> is created successful.");
                return true;
            }
            log.error("Elasticsearch: Failed index <posts> creation.");
            log.error(result.getJsonString());
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }

    @Override
    public boolean deleteIndex() {
        try {
            DeleteIndex deleteIndex = new DeleteIndex
                    .Builder(INDEX_NAME)
                    .build();
            JestResult result = getClient().execute(deleteIndex);
            if (result.isSucceeded()) {
                log.info("Elasticsearch: Index <posts> is deleted successful.");
                return true;
            }
            log.error("Elasticsearch: Failed index delete <posts>.");
            log.error(result.getJsonString());
            return false;
        } catch (IOException e) {
            log.error("Elasticsearch: Failed index delete <posts>.");
            log.error(e.getMessage(), e);
        }
        return false;
    }

    @Override
    public void buildIndex(List<PostEs> posts) {
        if (createIndex()) {
            log.info("Elasticsearch: Start building index <posts>.");
            bulkIndex(posts);
        }
    }

    @Override
    public void rebuildIndices(List<PostEs> posts) {
        if (!indexExist()) {
            buildIndex(posts);
            return;
        }
        if (deleteIndex()) {
            buildIndex(posts);
        }
    }

    @Override
    public List<PostEs> search(String text, Types.PostStatus status, Pageable pageable) {
        try {
            String searchJSON = buildSearch(text, status, pageable);
            Search search = new Search.Builder(searchJSON)
                    .addIndex(INDEX_NAME)
                    .addType(TYPE_NAME)
                    .build();
            SearchResult result = getClient().execute(search);
            if (result.isSucceeded()) {
                List<SearchResult.Hit<PostEs, Void>> hits = result.getHits(PostEs.class);
                return hits.stream()
                        .map(hit -> mapHitToPostES(hit))
                        .collect(Collectors.toList());
            }
            log.error(String.format("Elasticsearch: Failed search for: <%s>", text));
            log.error(result.getJsonString());

        } catch (IOException e) {
            log.error(String.format("Elasticsearch: Failed search for: <%s>", text));
            log.error(e.getMessage(), e);
        }
        return new ArrayList<>();
    }

    @Override
    public List<PostEs> search(String text, Types.PostStatus status, User currentUser, Pageable pageable) {
        try {
            String searchJSON = buildSearch(text, status, currentUser, pageable);
            Search search = new Search.Builder(searchJSON)
                    .addIndex(INDEX_NAME)
                    .addType(TYPE_NAME)
                    .build();
            SearchResult result = getClient().execute(search);
            if (result.isSucceeded()) {
                List<SearchResult.Hit<PostEs, Void>> hits = result.getHits(PostEs.class);
                return hits.stream()
                        .map(hit -> mapHitToPostES(hit))
                        .collect(Collectors.toList());
            }
            log.error(String.format("Elasticsearch: Failed search for: <%s>", text));
            log.error(result.getJsonString());
        } catch (IOException e) {
            log.error(String.format("Elasticsearch: Failed search for: <%s>", text));
            log.error(e.getMessage(), e);
        }
        return new ArrayList<>();
    }

    @Override
    public Integer count(String text, Types.PostStatus status) {
        return count(text, status, null);
    }

    @Override
    public Integer count(String text, Types.PostStatus status, User currentUser) {
        try {
            String countJSON = currentUser != null ? buildCount(text, status, currentUser) : buildCount(text, status);
            Count count = new Count.Builder()
                    .query(countJSON)
                    .addIndex(INDEX_NAME)
                    .addType(TYPE_NAME)
                    .build();
            CountResult result = getClient().execute(count);
            if (result.isSucceeded()) {
                return result.getCount().intValue();
            }
            log.error(String.format("Elasticsearch: Failed count for: <%s>", text));
            log.error(result.getJsonString());
        } catch (IOException e) {
            log.error(String.format("Elasticsearch: Failed count for: <%s>", text));
            log.error(e.getMessage(), e);
        }
        return 0;
    }

    @Override
    public PostEs findById(Long id) {
        Get get = new Get.Builder(INDEX_NAME, id.toString())
                .type(TYPE_NAME)
                .build();
        try {
            JestResult result = getClient().execute(get);
            if (result.isSucceeded()) {
                return result.getSourceAsObject(PostEs.class);
            }
            log.error(String.format("Elasticsearch: Retrieve post<%d> is failed.", id));
            log.error(result.getJsonString());
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public void index(PostEs post) {
        try {
            Index index = prepareIndex(post);
            getClient().execute(index);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    //  Moved to AWS lambda function
    @Override
    public void bulkIndex(List<PostEs> posts) {
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
            if (result.isSucceeded()) {
                log.info("Elasticsearch: Bulk index action successful.");
            } else {
                log.error("Elasticsearch: Failed bulk index action.");
                log.error(result.getJsonString());
            }
        } catch (IOException e) {
            log.error("Elasticsearch: Failed bulk index action.");
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public void update(PostEs post) {
        try {
            Update update = new Update
                    .Builder(post)
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
            JestResult result = getClient().execute(delete);
            if (result.isSucceeded()) {
                log.info(String.format("Elasticsearch: Delete post<%d> successful.", id));
                return;
            }
            log.error(String.format("Elasticsearch: Delete post<%d> failed.", id));
            log.error(result.getJsonString());
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public void delete(PostEs postEs) {
        deleteById(postEs.getId());
    }

    @Override
    public void bulkDelete(List<Long> ids) {
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
            JestResult result = getClient().execute(bulk);
            if (result.isSucceeded()) {
                log.info("Elasticsearch: Bulk delete is successful.");
                return;
            }
            log.error("Elasticsearch: Bulk delete is failed.");
            log.error(result.getJsonString());
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    public void updateDictionary(List<Word> words) {
        JSONArray indexPoses = new JSONArray(Arrays.asList("N", "SL", "SH", "SN", "XR", "V", "UNK", "M"));
        List<String> dictionary = words.stream()
                .map(word -> {
                    String cost = word.getCost() != null ? "," + word.getCost().toString() : "";
                    return word.getSurface() + cost;
                })
                .collect(Collectors.toList());
        JSONArray userWords = new JSONArray(dictionary);
        JSONObject settings = new JSONObject()
                .put("analysis", new JSONObject()
                        .put("analyzer", new JSONObject()
                                .put("korean", new JSONObject()
                                        .put("type", "custom")
                                        .put("tokenizer", "seunjeon_default_tokenizer")))
                        .put("tokenizer", new JSONObject()
                                .put("seunjeon_default_tokenizer", new JSONObject()
                                        .put("index_poses", indexPoses)
                                        .put("user_words", userWords)
                                )
                        )
                );
        UpdateSettings updateSettings = new UpdateSettings.Builder(settings.toString()).addIndex(INDEX_NAME).build();
        try {
            JestResult result = getClient().execute(updateSettings);
            if (result.isSucceeded()) {
                return;
            }
            log.error(result.getJsonString());
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }

    }

    private JSONArray getFilterContext(Types.PostStatus status) {
        return new JSONArray()
                .put(new JSONObject()
                        .put("term", new JSONObject()
                                .put("status", status.toString())
                        )
                );
    }

    private JSONArray getFilterContext(Types.PostStatus status, User user) {
        JSONArray filterContext = getFilterContext(status);
        Types.UserRole role = user.getHighestRole();
        switch (role) {
            case Administrator:
                return filterContext;
            case EditorInChief:
            case Editor:
                if (status.equals(Types.PostStatus.PUBLISH) ||
                        status.equals(Types.PostStatus.FUTURE) ||
                        status.equals(Types.PostStatus.PENDING)) {
                    return filterContext;
                }
                filterContext.put(new JSONObject()
                        .put("term", new JSONObject()
                                .put("modifiedUserId", user.getId())
                        )
                );
                return filterContext;
            default:
                filterContext.put(new JSONObject()
                        .put("term", new JSONObject()
                                .put("creatorId", user.getId())
                        )
                );
                return filterContext;
        }

    }

    private JSONArray getQueryContext(String text) {
        return new JSONArray()
                .put(new JSONObject()
                        .put("dis_max", new JSONObject()
                                .put("queries", new JSONArray()
                                        .put(new JSONObject()
                                                .put("match", new JSONObject()
                                                        .put("title", new JSONObject()
                                                                .put("query", text)
                                                                .put("analyzer", "korean")
                                                                .put("boost", 4)
                                                                .put("minimum_should_match", "4<75%")
                                                        )
                                                )
                                        )
                                        .put(new JSONObject()
                                                .put("match", new JSONObject()
                                                        .put("excerpt", new JSONObject()
                                                                .put("query", text)
                                                                .put("analyzer", "korean")
                                                                .put("boost", 3)
                                                                .put("minimum_should_match", "4<75%")
                                                        )
                                                )
                                        )
                                        .put(new JSONObject()
                                                .put("match", new JSONObject()
                                                        .put("content", new JSONObject()
                                                                .put("query", text)
                                                                .put("analyzer", "korean")
                                                                .put("boost", 0.6)
                                                                .put("minimum_should_match", "4<75%")
                                                        )
                                                )
                                        )
                                        .put(new JSONObject()
                                                .put("match", new JSONObject()
                                                        .put("bylineName", new JSONObject()
                                                                .put("query", text)
                                                                .put("minimum_should_match", "4<75%")
                                                        )
                                                )
                                        )
                                        .put(new JSONObject()
                                                .put("match", new JSONObject()
                                                        .put("tags", new JSONObject()
                                                                .put("query", text)
                                                                .put("analyzer", "korean")
                                                                .put("minimum_should_match", "4<75%")
                                                        )
                                                )
                                        )
                                        .put(new JSONObject()
                                                .put("match", new JSONObject()
                                                        .put("contributors", new JSONObject()
                                                                .put("query", text)
                                                                .put("analyzer", "korean")
                                                                .put("minimum_should_match", "3<75%")
                                                        )
                                                )
                                        )
                                )
                        )
                );
    }

    private JSONArray getAdminQueryContext(String text) {
        return new JSONArray()
                .put(new JSONObject()
                        .put("dis_max", new JSONObject()
                                .put("queries", new JSONArray()
                                        .put(new JSONObject()
                                                .put("match", new JSONObject()
                                                        .put("title", new JSONObject()
                                                                .put("query", text)
                                                                .put("analyzer", "korean")
                                                                .put("boost", 4)
                                                                .put("minimum_should_match", "4<75%")
                                                        )
                                                )
                                        )
                                        .put(new JSONObject()
                                                .put("match", new JSONObject()
                                                        .put("excerpt", new JSONObject()
                                                                .put("query", text)
                                                                .put("analyzer", "korean")
                                                                .put("boost", 3)
                                                                .put("minimum_should_match", "4<75%")
                                                        )
                                                )
                                        )
                                        .put(new JSONObject()
                                                .put("match", new JSONObject()
                                                        .put("categoryName", new JSONObject()
                                                                .put("query", text)
                                                                .put("boost", 3)
                                                                .put("minimum_should_match", "4<75%")
                                                        )
                                                )
                                        )
                                        .put(new JSONObject()
                                                .put("match", new JSONObject()
                                                        .put("creatorName", new JSONObject()
                                                                .put("query", text)
                                                                .put("boost", 3)
                                                                .put("minimum_should_match", "4<75%")
                                                        )
                                                )
                                        )
                                        .put(new JSONObject()
                                                .put("match", new JSONObject()
                                                        .put("modifiedUserName", new JSONObject()
                                                                .put("query", text)
                                                                .put("boost", 3)
                                                                .put("minimum_should_match", "4<75%")
                                                        )
                                                )
                                        )
                                        .put(new JSONObject()
                                                .put("match", new JSONObject()
                                                        .put("content", new JSONObject()
                                                                .put("query", text)
                                                                .put("analyzer", "korean")
                                                                .put("boost", 0.6)
                                                                .put("minimum_should_match", "4<75%")
                                                        )
                                                )
                                        )
                                        .put(new JSONObject()
                                                .put("match", new JSONObject()
                                                        .put("bylineName", new JSONObject()
                                                                .put("query", text)
                                                                .put("minimum_should_match", "4<75%")
                                                        )
                                                )
                                        )
                                        .put(new JSONObject()
                                                .put("match", new JSONObject()
                                                        .put("tags", new JSONObject()
                                                                .put("query", text)
                                                                .put("analyzer", "korean")
                                                                .put("minimum_should_match", "4<75%")
                                                        )
                                                )
                                        )
                                        .put(new JSONObject()
                                                .put("match", new JSONObject()
                                                        .put("contributors", new JSONObject()
                                                                .put("query", text)
                                                                .put("analyzer", "korean")
                                                                .put("minimum_should_match", "3<75%")
                                                        )
                                                )
                                        )
                                )
                        )
                );
    }

    private String buildSearch(String text, Types.PostStatus status, Pageable pageable) {
        JSONArray filterContext = getFilterContext(status);
        JSONArray queryContext = getQueryContext(text);

        return new JSONObject()
                .put("query", new JSONObject()
                        .put("bool", new JSONObject()
                                .put("filter", filterContext)
                                .put("must", queryContext)
                        )
                )
                .put("_source", new JSONArray()
                        .put("title")
                        .put("excerpt")
                )
                .put("from", pageable.getOffset())
                .put("size", pageable.getPageSize())
                .put("highlight", new JSONObject()
                        .put("require_field_match", false)
                        .put("fields", new JSONObject()
                                .put("title", new JSONObject()
                                        .put("number_of_fragments", 0)
                                )
                                .put("excerpt", new JSONObject()
                                        .put("number_of_fragments", 0)
                                )
                        )
                )
                .toString();
    }

    private String buildSearch(String text, Types.PostStatus status, User currentUser, Pageable pageable) {
        JSONArray filterContext = getFilterContext(status, currentUser);
        JSONArray queryContext = getAdminQueryContext(text);

        return new JSONObject()
                .put("query", new JSONObject()
                        .put("bool", new JSONObject()
                                .put("filter", filterContext)
                                .put("must", queryContext)
                        )
                )
                .put("_source", new JSONArray()
                        .put("title")
                )
                .put("from", pageable.getOffset())
                .put("size", pageable.getPageSize())
                .put("highlight", new JSONObject()
                        .put("require_field_match", false)
                        .put("fields", new JSONObject()
                                .put("title", new JSONObject())
                                .put("bylineName", new JSONObject())
                                .put("categoryName", new JSONObject())
                                .put("creatorName", new JSONObject())
                                .put("modifiedUserName", new JSONObject())
                        )
                )
                .toString();
    }

    private String buildCount(String text, Types.PostStatus status) {
        JSONArray filterContext = getFilterContext(status);
        JSONArray queryContext = getQueryContext(text);

        return new JSONObject()
                .put("query", new JSONObject()
                        .put("bool", new JSONObject()
                                .put("filter", filterContext)
                                .put("must", queryContext)
                        )
                )
                .toString();
    }

    private String buildCount(String text, Types.PostStatus status, User currentUser) {
        JSONArray filterContext = getFilterContext(status, currentUser);
        JSONArray queryContext = getQueryContext(text);

        return new JSONObject()
                .put("query", new JSONObject()
                        .put("bool", new JSONObject()
                                .put("filter", filterContext)
                                .put("must", queryContext)
                        )
                )
                .toString();
    }

    private Index prepareIndex(PostEs post) {
        return new Index
                .Builder(post)
                .index(INDEX_NAME)
                .type(TYPE_NAME)
                .build();
    }

    private Delete prepareDelete(Long id) {
        return new Delete.Builder(id.toString())
                .index(INDEX_NAME)
                .type(TYPE_NAME)
                .build();
    }

    private PostEs mapHitToPostES(SearchResult.Hit<PostEs, Void> hit) {
        Long id = Long.parseLong(hit.id);
        PostEs postEs = new PostEs(id);

        if (hit.highlight == null) {
            return postEs;
        }

        if (hit.highlight.get("title") != null) {
            String title = hit.highlight.get("title").get(0);
            postEs.setTitle(title);
        }

        if (hit.highlight.get("excerpt") != null) {
            String excerpt = hit.highlight.get("excerpt").get(0);
            postEs.setExcerpt(excerpt);
        }

        if (hit.highlight.get("bylineName") != null) {
            String bylineName = hit.highlight.get("bylineName").get(0);
            postEs.setBylineName(bylineName);
        }

        if (hit.highlight.get("categoryName") != null) {
            String categoryName = hit.highlight.get("categoryName").get(0);
            postEs.setCategoryName(categoryName);
        }

        if (hit.highlight.get("creatorName") != null) {
            String creatorName = hit.highlight.get("creatorName").get(0);
            postEs.setCreatorName(creatorName);
        }

        if (hit.highlight.get("modifiedUserName") != null) {
            String modifiedUserName = hit.highlight.get("modifiedUserName").get(0);
            postEs.setModifiedUserName(modifiedUserName);
        }
        return postEs;
    }

    private String getHighlight(SearchResult.Hit<PostEs, Void> hit, String field) {
        String result;
        List<String> highlight = hit.highlight.get(field);
        int size = highlight.size();
        if (size > 1) {
            result = highlight.stream()
                    .reduce("", (s, str) -> s.concat(str).concat(" "));
        } else {
            result = highlight.get(0);
        }
        return result;
    }

    private JestClient getClient() {
        return clientFactory.getObject();
    }
}
