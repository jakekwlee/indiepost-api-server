package com.indiepost.repository.elasticsearch;

import com.indiepost.enums.Types;
import com.indiepost.model.User;
import com.indiepost.model.Word;
import com.indiepost.model.elasticsearch.PostEs;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostEsRepository {

    boolean testConnection();

    boolean indexExist();

    boolean createIndex();

    boolean deleteIndex();

    void buildIndex(List<PostEs> posts);

    void rebuildIndices(List<PostEs> posts);

    List<PostEs> search(String text, Types.PostStatus status, Pageable pageable);

    List<PostEs> search(String text, Types.PostStatus status, User currentUser, Pageable pageable);

    List<Long> moreLikeThis(List<Long> postId, Types.PostStatus status, Pageable pageable);

    List<Long> recommendation(List<Long> bookmarkedPostIds, List<Long> historyPostIds, Types.PostStatus status, Pageable pageable);

    Integer count(String text, Types.PostStatus status);

    Integer count(String text, Types.PostStatus status, User currentUser);

    PostEs findById(Long id);

    void index(PostEs post);

    void bulkIndex(List<PostEs> posts);

    void update(PostEs post);

    void deleteById(Long id);

    void delete(PostEs postEs);

    void bulkDelete(List<Long> ids);

    void updateDictionary(List<Word> words);
}
