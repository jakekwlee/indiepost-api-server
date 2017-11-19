package com.indiepost.model.elasticsearch;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.List;

@Document(indexName = "post")
public class PostEs implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @Field(analyzer = "korean_index", searchAnalyzer = "korean_query", store = true)
    private String title;

    @Field(analyzer = "korean_index", searchAnalyzer = "korean_query", store = true)
    private String excerpt;

    @Field(analyzer = "korean_index", searchAnalyzer = "korean_query", store = true)
    private String content;

    @Field(store = true)
    private String bylineName;

    @Field(analyzer = "korean_index", searchAnalyzer = "korean_query", store = true)
    private List<String> contributors;

    @Field(analyzer = "korean_index", searchAnalyzer = "korean_query", store = true)
    private List<String> tags;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getExcerpt() {
        return excerpt;
    }

    public void setExcerpt(String excerpt) {
        this.excerpt = excerpt;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getBylineName() {
        return bylineName;
    }

    public void setBylineName(String bylineName) {
        this.bylineName = bylineName;
    }

    public List<String> getContributors() {
        return contributors;
    }

    public void setContributors(List<String> contributors) {
        this.contributors = contributors;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
