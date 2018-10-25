package com.indiepost.repository.elasticsearch

import com.indiepost.enums.Types
import com.indiepost.model.User
import com.indiepost.model.Word
import com.indiepost.model.elasticsearch.PostEs
import io.searchbox.client.JestClient
import io.searchbox.client.JestClientFactory
import io.searchbox.cluster.Health
import io.searchbox.core.*
import io.searchbox.indices.CreateIndex
import io.searchbox.indices.DeleteIndex
import io.searchbox.indices.IndicesExists
import io.searchbox.indices.settings.UpdateSettings
import org.json.JSONArray
import org.json.JSONObject
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import java.io.IOException
import java.util.*
import java.util.stream.Collectors
import javax.inject.Inject

@Repository
class PostEsRepositoryJest @Inject constructor(
        private val clientFactory: JestClientFactory,
        private val indexSettings: String
) : com.indiepost.repository.elasticsearch.PostEsRepository {

    private val client: JestClient
        get() = clientFactory.getObject()

    override fun testConnection(): Boolean {
        try {
            val client = client
            val health = Health.Builder().build()
            val result = client.execute(health)
            if (result.isSucceeded) {
                log.info("Elasticsearch: Connection established successful.")
                return true
            }
            log.error("Elasticsearch: Connection failed.")
            log.error(result.jsonString)
            return false
        } catch (e: IOException) {
            log.error(e.message, e)
        }

        return false
    }

    override fun indexExist(): Boolean {
        try {
            val indicesExists = IndicesExists.Builder(INDEX_NAME).build()
            return client.execute(indicesExists).isSucceeded
        } catch (e: IOException) {
            log.error(e.message, e)
        }

        return false
    }

    override fun createIndex(): Boolean {
        try {
            val createIndex = CreateIndex.Builder(INDEX_NAME)
                    .settings(indexSettings)
                    .build()
            val result = client.execute(createIndex)
            if (result.isSucceeded) {
                log.info("Elasticsearch: Index <posts> is created successful.")
                return true
            }
            log.error("Elasticsearch: Failed index <posts> creation.")
            log.error(result.jsonString)
        } catch (e: IOException) {
            log.error(e.message, e)
        }

        return false
    }

    override fun deleteIndex(): Boolean {
        try {
            val deleteIndex = DeleteIndex.Builder(INDEX_NAME)
                    .build()
            val result = client.execute(deleteIndex)
            if (result.isSucceeded) {
                log.info("Elasticsearch: Index <posts> is deleted successful.")
                return true
            }
            log.error("Elasticsearch: Failed index delete <posts>.")
            log.error(result.jsonString)
            return false
        } catch (e: IOException) {
            log.error("Elasticsearch: Failed index delete <posts>.")
            log.error(e.message, e)
        }

        return false
    }

    override fun buildIndex(posts: List<PostEs>) {
        if (createIndex()) {
            log.info("Elasticsearch: Start building index <posts>.")
            bulkIndex(posts)
        }
    }

    override fun rebuildIndices(posts: List<PostEs>) {
        if (!indexExist()) {
            buildIndex(posts)
            return
        }
        if (deleteIndex()) {
            buildIndex(posts)
        }
    }

    override fun search(text: String, status: Types.PostStatus, pageable: Pageable): List<PostEs> {
        try {
            val searchJSON = buildSearch(text, status, pageable)
            val search = Search.Builder(searchJSON)
                    .addIndex(INDEX_NAME)
                    .addType(TYPE_NAME)
                    .build()
            val result = client.execute(search)
            if (result.isSucceeded) {
                val hits = result.getHits(PostEs::class.java)
                return hits.stream()
                        .map { hit -> mapHitToPostES(hit) }
                        .collect(Collectors.toList())
            }
            log.error(String.format("Elasticsearch: Failed search for: <%s>", text))
            log.error(result.jsonString)

        } catch (e: IOException) {
            log.error(String.format("Elasticsearch: Failed search for: <%s>", text))
            log.error(e.message, e)
        }

        return emptyList()
    }

    override fun search(text: String, status: Types.PostStatus, currentUser: User, pageable: Pageable): List<PostEs> {
        try {
            val searchJSON = buildSearch(text, status, currentUser, pageable)
            val search = Search.Builder(searchJSON)
                    .addIndex(INDEX_NAME)
                    .addType(TYPE_NAME)
                    .build()
            val result = client.execute(search)
            if (result.isSucceeded) {
                val hits = result.getHits(PostEs::class.java)
                return hits.stream()
                        .map { hit -> mapHitToPostES(hit) }
                        .collect(Collectors.toList())
            }
            log.error(String.format("Elasticsearch: Failed search for: <%s>", text))
            log.error(result.jsonString)
        } catch (e: IOException) {
            log.error(String.format("Elasticsearch: Failed search for: <%s>", text))
            log.error(e.message, e)
        }

        return emptyList()
    }

    override fun moreLikeThis(postIds: List<Long>, status: Types.PostStatus, pageable: Pageable): List<Long> {
        try {
            val stringifiedJSONQuery = buildMoreLikeThis(postIds, status, pageable)
            val search = Search.Builder(stringifiedJSONQuery)
                    .addIndex(INDEX_NAME)
                    .addType(TYPE_NAME)
                    .build()
            val result = client.execute(search)
            if (result.isSucceeded) {
                val hits = result.getHits(PostEs::class.java)
                return hits.stream()
                        .map { hit -> java.lang.Long.parseLong(hit.id) }
                        .collect(Collectors.toList())
            }
            log.error(String.format("Elasticsearch: Failed 'more like this' query for: <%s>", postIds))
            log.error(result.jsonString)
        } catch (e: IOException) {
            log.error(String.format("Elasticsearch: Failed 'more like this' query for: <%s>", postIds))
            log.error(e.message, e)
        }

        return emptyList()
    }

    override fun recommendation(bookmarkedPostIds: List<Long>, historyPostIds: List<Long>, status: Types.PostStatus, pageable: Pageable): List<Long> {
        try {
            val stringifiedJSONQuery = buildRecommendations(bookmarkedPostIds, historyPostIds, status, pageable)
            val search = Search.Builder(stringifiedJSONQuery)
                    .addIndex(INDEX_NAME)
                    .addType(TYPE_NAME)
                    .build()
            val result = client.execute(search)
            if (result.isSucceeded) {
                val hits = result.getHits(PostEs::class.java)
                return hits.stream()
                        .map { hit -> java.lang.Long.parseLong(hit.id) }
                        .collect(Collectors.toList())
            }
            log.error(String.format("Elasticsearch: Failed 'recommendations' query"))
            log.error(result.jsonString)
        } catch (e: IOException) {
            log.error(String.format("Elasticsearch: Failed 'recommendations' query"))
            log.error(e.message, e)
        }

        return emptyList()
    }

    override fun count(text: String, status: Types.PostStatus): Int {
        return count(text, status, null)
    }

    override fun count(text: String, status: Types.PostStatus, currentUser: User?): Int {
        try {
            val countJSON =
                    if (currentUser != null)
                        buildCount(text, status, currentUser)
                    else
                        buildCount(text, status)
            val count = Count.Builder()
                    .query(countJSON)
                    .addIndex(INDEX_NAME)
                    .addType(TYPE_NAME)
                    .build()
            val result = client.execute(count)
            if (result.isSucceeded) {
                return result.count!!.toInt()
            }
            log.error(String.format("Elasticsearch: Failed count for: <%s>", text))
            log.error(result.jsonString)
        } catch (e: IOException) {
            log.error(String.format("Elasticsearch: Failed count for: <%s>", text))
            log.error(e.message, e)
        }

        return 0
    }

    override fun findById(id: Long?): PostEs? {
        val get = Get.Builder(INDEX_NAME, id!!.toString())
                .type(TYPE_NAME)
                .build()
        try {
            val result = client.execute(get)
            if (result.isSucceeded) {
                return result.getSourceAsObject(PostEs::class.java)
            }
            log.error(String.format("Elasticsearch: Retrieve post<%d> is failed.", id))
            log.error(result.jsonString)
        } catch (e: IOException) {
            log.error(e.message, e)
        }
        return null
    }

    override fun index(post: PostEs) {
        try {
            val index = prepareIndex(post)
            client.execute(index)
        } catch (e: IOException) {
            log.error(e.message, e)
        }

    }

    //  Moved to AWS lambda function
    override fun bulkIndex(posts: List<PostEs>) {
        if (posts.isEmpty()) {
            return
        }
        val bulkBuilder = Bulk.Builder()
                .defaultIndex(INDEX_NAME)
                .defaultType(TYPE_NAME)
        try {
            for (post in posts) {
                bulkBuilder.addAction(prepareIndex(post))
            }
            val bulk = bulkBuilder.build()
            val result = client.execute(bulk)
            if (result.isSucceeded) {
                log.info("Elasticsearch: Bulk index action successful.")
            } else {
                log.error("Elasticsearch: Failed bulk index action.")
                log.error(result.jsonString)
            }
        } catch (e: IOException) {
            log.error("Elasticsearch: Failed bulk index action.")
            log.error(e.message, e)
        }

    }

    override fun update(post: PostEs) {
        try {
            val update = Update.Builder(post)
                    .index(INDEX_NAME)
                    .type(TYPE_NAME)
                    .build()
            client.execute(update)
        } catch (e: IOException) {
            log.error(e.message, e)
        }

    }

    override fun deleteById(id: Long?) {
        val delete = prepareDelete(id!!)
        try {
            val result = client.execute(delete)
            if (result.isSucceeded) {
                log.info(String.format("Elasticsearch: Delete post<%d> successful.", id))
                return
            }
            log.error(String.format("Elasticsearch: Delete post<%d> failed.", id))
            log.error(result.jsonString)
        } catch (e: IOException) {
            log.error(e.message, e)
        }

    }

    override fun delete(postEs: PostEs) {
        deleteById(postEs.id)
    }

    override fun bulkDelete(ids: List<Long>) {
        if (ids.isEmpty()) {
            return
        }
        val bulkBuilder = Bulk.Builder()
                .defaultIndex(INDEX_NAME)
        try {
            val deletes = ArrayList<Delete>()
            for (id in ids) {
                deletes.add(prepareDelete(id))
            }
            bulkBuilder.addAction(deletes)
            val bulk = bulkBuilder.build()
            val result = client.execute(bulk)
            if (result.isSucceeded) {
                log.info("Elasticsearch: Bulk delete is successful.")
                return
            }
            log.error("Elasticsearch: Bulk delete is failed.")
            log.error(result.jsonString)
        } catch (e: IOException) {
            log.error(e.message, e)
        }

    }

    override fun updateDictionary(words: List<Word>) {
        val indexPoses = JSONArray(Arrays.asList("N", "SL", "SH", "SN", "XR", "V", "UNK", "M"))
        val dictionary = words.stream()
                .map { (_, surface, cost) ->
                    val cost = if (cost != null) "," + cost.toString() else ""
                    surface!! + cost
                }
                .collect(Collectors.toList())
        val userWords = JSONArray(dictionary)
        val settings = JSONObject()
                .put("analysis", JSONObject()
                        .put("analyzer", JSONObject()
                                .put("korean", JSONObject()
                                        .put("type", "custom")
                                        .put("tokenizer", "seunjeon_default_tokenizer")))
                        .put("tokenizer", JSONObject()
                                .put("seunjeon_default_tokenizer", JSONObject()
                                        .put("index_poses", indexPoses)
                                        .put("user_words", userWords)
                                )
                        )
                )
        val updateSettings = UpdateSettings.Builder(settings.toString()).addIndex(INDEX_NAME).build()
        try {
            val result = client.execute(updateSettings)
            if (result.isSucceeded) {
                return
            }
            log.error(result.jsonString)
        } catch (e: IOException) {
            log.error(e.message, e)
        }

    }

    private fun getFilterContext(status: Types.PostStatus): JSONArray {
        return JSONArray()
                .put(JSONObject()
                        .put("term", JSONObject()
                                .put("status", status.toString())
                        )
                )
    }

    private fun getFilterContext(status: Types.PostStatus, user: User): JSONArray {
        val filterContext = getFilterContext(status)
        val role = user.roleType
        when (role) {
            Types.UserRole.Administrator -> return filterContext
            Types.UserRole.EditorInChief, Types.UserRole.Editor -> {
                if (status == Types.PostStatus.PUBLISH ||
                        status == Types.PostStatus.FUTURE ||
                        status == Types.PostStatus.PENDING) {
                    return filterContext
                }
                filterContext.put(JSONObject()
                        .put("term", JSONObject()
                                .put("modifiedUserId", user.id)
                        )
                )
                return filterContext
            }
            else -> {
                filterContext.put(JSONObject()
                        .put("term", JSONObject()
                                .put("creatorId", user.id)
                        )
                )
                return filterContext
            }
        }

    }

    private fun getQueryContext(text: String): JSONArray {
        return JSONArray()
                .put(JSONObject()
                        .put("dis_max", JSONObject()
                                .put("queries", JSONArray()
                                        .put(JSONObject()
                                                .put("match", JSONObject()
                                                        .put("title", JSONObject()
                                                                .put("query", text)
                                                                .put("analyzer", "korean")
                                                                .put("boost", 4)
                                                                .put("minimum_should_match", "4<75%")
                                                        )
                                                )
                                        )
                                        .put(JSONObject()
                                                .put("match", JSONObject()
                                                        .put("excerpt", JSONObject()
                                                                .put("query", text)
                                                                .put("analyzer", "korean")
                                                                .put("boost", 3)
                                                                .put("minimum_should_match", "4<75%")
                                                        )
                                                )
                                        )
                                        .put(JSONObject()
                                                .put("match", JSONObject()
                                                        .put("content", JSONObject()
                                                                .put("query", text)
                                                                .put("analyzer", "korean")
                                                                .put("boost", 0.6)
                                                                .put("minimum_should_match", "4<75%")
                                                        )
                                                )
                                        )
                                        .put(JSONObject()
                                                .put("prefix", JSONObject()
                                                        .put("contributors", JSONObject()
                                                                .put("value", text)
                                                                .put("boost", 2)
                                                        )

                                                )
                                        )
                                        .put(JSONObject()
                                                .put("prefix", JSONObject()
                                                        .put("tags", JSONObject()
                                                                .put("value", text)
                                                                .put("boost", 2)
                                                        )
                                                )
                                        )
                                )
                        )
                )
    }

    private fun getAdminQueryContext(text: String): JSONArray {
        return JSONArray()
                .put(JSONObject()
                        .put("dis_max", JSONObject()
                                .put("queries", JSONArray()
                                        .put(JSONObject()
                                                .put("match", JSONObject()
                                                        .put("title", JSONObject()
                                                                .put("query", text)
                                                                .put("analyzer", "korean")
                                                                .put("boost", 4)
                                                                .put("minimum_should_match", "4<75%")
                                                        )
                                                )
                                        )
                                        .put(JSONObject()
                                                .put("match", JSONObject()
                                                        .put("excerpt", JSONObject()
                                                                .put("query", text)
                                                                .put("analyzer", "korean")
                                                                .put("boost", 3)
                                                                .put("minimum_should_match", "4<75%")
                                                        )
                                                )
                                        )
                                        .put(JSONObject()
                                                .put("match", JSONObject()
                                                        .put("categoryName", JSONObject()
                                                                .put("term", text)
                                                                .put("boost", 3)
                                                        )
                                                )
                                        )
                                        .put(JSONObject()
                                                .put("match", JSONObject()
                                                        .put("contributors", JSONObject()
                                                                .put("term", text)
                                                                .put("boost", 3)
                                                        )
                                                )
                                        )
                                        .put(JSONObject()
                                                .put("match", JSONObject()
                                                        .put("tags", JSONObject()
                                                                .put("term", text)
                                                                .put("boost", 3)
                                                        )
                                                )
                                        )
                                        .put(JSONObject()
                                                .put("match", JSONObject()
                                                        .put("creatorName", JSONObject()
                                                                .put("query", text)
                                                                .put("boost", 2)
                                                                .put("minimum_should_match", "4<75%")
                                                        )
                                                )
                                        )
                                        .put(JSONObject()
                                                .put("match", JSONObject()
                                                        .put("modifiedUserName", JSONObject()
                                                                .put("query", text)
                                                                .put("boost", 2)
                                                                .put("minimum_should_match", "4<75%")
                                                        )
                                                )
                                        )
                                        .put(JSONObject()
                                                .put("match", JSONObject()
                                                        .put("bylineName", JSONObject()
                                                                .put("term", text)
                                                                .put("boost", 2)
                                                        )
                                                )
                                        )
                                        .put(JSONObject()
                                                .put("match", JSONObject()
                                                        .put("content", JSONObject()
                                                                .put("query", text)
                                                                .put("analyzer", "korean")
                                                                .put("boost", 0.6)
                                                                .put("minimum_should_match", "4<75%")
                                                        )
                                                )
                                        )
                                )
                        )
                )
    }

    private fun buildSearch(text: String, status: Types.PostStatus, pageable: Pageable): String {
        val filterContext = getFilterContext(status)
        val queryContext = getQueryContext(text)

        return JSONObject()
                .put("query", JSONObject()
                        .put("bool", JSONObject()
                                .put("filter", filterContext)
                                .put("must", queryContext)
                        )
                )
                .put("_source", JSONArray()
                        .put("title")
                        .put("excerpt")
                )
                .put("from", pageable.offset)
                .put("size", pageable.pageSize)
                .put("highlight", JSONObject()
                        .put("require_field_match", false)
                        .put("fields", JSONObject()
                                .put("title", JSONObject()
                                        .put("number_of_fragments", 0)
                                )
                                .put("excerpt", JSONObject()
                                        .put("number_of_fragments", 0)
                                )
                        )
                )
                .toString()
    }

    private fun buildSearch(text: String, status: Types.PostStatus, currentUser: User, pageable: Pageable): String {
        val filterContext = getFilterContext(status, currentUser)
        val queryContext = getAdminQueryContext(text)

        return JSONObject()
                .put("query", JSONObject()
                        .put("bool", JSONObject()
                                .put("filter", filterContext)
                                .put("must", queryContext)
                        )
                )
                .put("_source", JSONArray()
                        .put("title")
                )
                .put("from", pageable.offset)
                .put("size", pageable.pageSize)
                .put("highlight", JSONObject()
                        .put("require_field_match", false)
                        .put("fields", JSONObject()
                                .put("title", JSONObject())
                                .put("bylineName", JSONObject())
                                .put("categoryName", JSONObject())
                                .put("creatorName", JSONObject())
                                .put("modifiedUserName", JSONObject())
                        )
                )
                .toString()
    }

    private fun buildMoreLikeThis(postIds: List<Long>, status: Types.PostStatus, pageable: Pageable): String {
        val filterContext = getFilterContext(status)
        val posts = JSONArray()
        for (postId in postIds) {
            posts.put(JSONObject()
                    .put("_index", INDEX_NAME)
                    .put("_type", TYPE_NAME)
                    .put("_id", postId)
            )
        }

        return JSONObject()
                .put("query", JSONObject()
                        .put("bool", JSONObject()
                                .put("filter", filterContext)
                                .put("should", JSONArray()
                                        .put(JSONObject()
                                                .put("more_like_this",
                                                        JSONObject()
                                                                .put("fields", JSONArray()
                                                                        .put("tags")
                                                                )
                                                                .put("like", posts)
                                                                .put("min_term_freq", 1)
                                                                .put("min_doc_freq", 1)
                                                                .put("boost", 3)
                                                )
                                        )
                                        .put(JSONObject()
                                                .put("more_like_this",
                                                        JSONObject()
                                                                .put("fields", JSONArray()
                                                                        .put("title")
                                                                )
                                                                .put("like", posts)
                                                                .put("min_term_freq", 1)
                                                                .put("min_doc_freq", 1)
                                                                .put("max_doc_freq", 50)
                                                                .put("boost", 2)
                                                )
                                        )
                                        .put(JSONObject()
                                                .put("more_like_this",
                                                        JSONObject()
                                                                .put("fields", JSONArray()
                                                                        .put("contributors")
                                                                )
                                                                .put("like", posts)
                                                                .put("min_term_freq", 1)
                                                                .put("min_doc_freq", 1)
                                                                .put("boost", 2)
                                                )
                                        )
                                        .put(JSONObject()
                                                .put("more_like_this",
                                                        JSONObject()
                                                                .put("fields", JSONArray()
                                                                        .put("excerpt")
                                                                        .put("content")
                                                                )
                                                                .put("like", posts)
                                                                .put("min_term_freq", 1)
                                                                .put("min_doc_freq", 1)
                                                                .put("max_doc_freq", 50)
                                                )
                                        )
                                )
                        )
                )
                .put("_source", JSONArray()
                        .put("_id")
                )
                .put("from", pageable.offset)
                .put("size", pageable.pageSize)
                .toString()
    }

    private fun buildRecommendations(bookmarkedPostIds: List<Long>?, historyPostIds: List<Long>?, status: Types.PostStatus, pageable: Pageable): String {
        val filterContext = getFilterContext(status)
        val bookmarkedPosts = JSONArray()
        val historyPosts = JSONArray()
        val should = JSONArray()
        if (bookmarkedPostIds == null || bookmarkedPostIds.size > 0) {
            for (postId in bookmarkedPostIds!!) {
                bookmarkedPosts.put(JSONObject()
                        .put("_index", INDEX_NAME)
                        .put("_type", TYPE_NAME)
                        .put("_id", postId)
                )
            }
            should
                    .put(JSONObject()
                            .put("more_like_this",
                                    JSONObject()
                                            .put("fields", JSONArray()
                                                    .put("tags")
                                            )
                                            .put("like", bookmarkedPosts)
                                            .put("min_term_freq", 1)
                                            .put("min_doc_freq", 1)
                                            .put("boost", 6)
                            )
                    )
                    .put(JSONObject()
                            .put("more_like_this",
                                    JSONObject()
                                            .put("fields", JSONArray()
                                                    .put("title")
                                            )
                                            .put("like", bookmarkedPosts)
                                            .put("min_term_freq", 1)
                                            .put("min_doc_freq", 1)
                                            .put("max_doc_freq", 50)
                                            .put("boost", 4)
                            )
                    )
                    .put(JSONObject()
                            .put("more_like_this",
                                    JSONObject()
                                            .put("fields", JSONArray()
                                                    .put("contributors")
                                            )
                                            .put("like", bookmarkedPosts)
                                            .put("min_term_freq", 1)
                                            .put("min_doc_freq", 1)
                                            .put("boost", 4)
                            )
                    )
                    .put(JSONObject()
                            .put("more_like_this",
                                    JSONObject()
                                            .put("fields", JSONArray()
                                                    .put("excerpt")
                                                    .put("content")
                                            )
                                            .put("like", bookmarkedPosts)
                                            .put("min_term_freq", 1)
                                            .put("min_doc_freq", 1)
                                            .put("max_doc_freq", 50)
                                            .put("boost", 2)
                            )
                    )

        }
        if (historyPostIds == null || historyPostIds.size > 0) {
            for (postId in historyPostIds!!) {
                historyPosts.put(JSONObject()
                        .put("_index", INDEX_NAME)
                        .put("_type", TYPE_NAME)
                        .put("_id", postId)
                )
            }
            should
                    .put(JSONObject()
                            .put("more_like_this",
                                    JSONObject()
                                            .put("fields", JSONArray()
                                                    .put("tags")
                                            )
                                            .put("like", historyPosts)
                                            .put("min_term_freq", 1)
                                            .put("min_doc_freq", 1)
                                            .put("boost", 3)
                            )
                    )
                    .put(JSONObject()
                            .put("more_like_this",
                                    JSONObject()
                                            .put("fields", JSONArray()
                                                    .put("title")
                                            )
                                            .put("like", historyPosts)
                                            .put("min_term_freq", 1)
                                            .put("min_doc_freq", 1)
                                            .put("max_doc_freq", 50)
                                            .put("boost", 2)
                            )
                    )
                    .put(JSONObject()
                            .put("more_like_this",
                                    JSONObject()
                                            .put("fields", JSONArray()
                                                    .put("contributors")
                                            )
                                            .put("like", historyPosts)
                                            .put("min_term_freq", 1)
                                            .put("min_doc_freq", 1)
                                            .put("boost", 2)
                            )
                    )
                    .put(JSONObject()
                            .put("more_like_this",
                                    JSONObject()
                                            .put("fields", JSONArray()
                                                    .put("excerpt")
                                                    .put("content")
                                            )
                                            .put("like", historyPosts)
                                            .put("min_term_freq", 1)
                                            .put("min_doc_freq", 1)
                                            .put("max_doc_freq", 50)
                            )
                    )
        }

        return JSONObject()
                .put("query", JSONObject()
                        .put("bool", JSONObject()
                                .put("filter", filterContext)
                                .put("should", should)
                        )
                )
                .put("_source", JSONArray()
                        .put("_id")
                )
                .put("from", pageable.offset)
                .put("size", pageable.pageSize)
                .toString()
    }

    private fun buildCount(text: String, status: Types.PostStatus): String {
        val filterContext = getFilterContext(status)
        val queryContext = getQueryContext(text)

        return JSONObject()
                .put("query", JSONObject()
                        .put("bool", JSONObject()
                                .put("filter", filterContext)
                                .put("must", queryContext)
                        )
                )
                .toString()
    }

    private fun buildCount(text: String, status: Types.PostStatus, currentUser: User): String {
        val filterContext = getFilterContext(status, currentUser)
        val queryContext = getQueryContext(text)

        return JSONObject()
                .put("query", JSONObject()
                        .put("bool", JSONObject()
                                .put("filter", filterContext)
                                .put("must", queryContext)
                        )
                )
                .toString()
    }

    private fun prepareIndex(post: PostEs): Index {
        return Index.Builder(post)
                .index(INDEX_NAME)
                .type(TYPE_NAME)
                .build()
    }

    private fun prepareDelete(id: Long): Delete {
        return Delete.Builder(id.toString())
                .index(INDEX_NAME)
                .type(TYPE_NAME)
                .build()
    }

    private fun mapHitToPostES(hit: SearchResult.Hit<PostEs, Void>): PostEs {
        val id = java.lang.Long.parseLong(hit.id)
        val postEs = PostEs(id)

        if (hit.highlight == null) {
            return postEs
        }
        hit.highlight["title"]?.let {
            postEs.title = it[0]
        }
        hit.highlight["excerpt"]?.let {
            postEs.excerpt = it[0]
        }
        hit.highlight["bylineName"]?.let {
            postEs.bylineName = it[0]
        }
        hit.highlight["creatorName"]?.let {
            postEs.creatorName = it[0]
        }
        hit.highlight["modifiedUserName"]?.let {
            postEs.modifiedUserName = it[0]
        }
        return postEs
    }

    companion object {
        private val INDEX_NAME = "posts"
        private val TYPE_NAME = "post"
        private val log = LoggerFactory.getLogger(PostEsRepositoryJest::class.java)
    }
}
