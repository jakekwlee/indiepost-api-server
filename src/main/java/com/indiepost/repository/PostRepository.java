package com.indiepost.repository;

import com.indiepost.model.Category;
import com.indiepost.model.Post;
import com.indiepost.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by jake on 7/26/16.
 */
public interface PostRepository extends JpaRepository<Post, Integer> {

    List<Post> findByCategory(Category category);

    List<Post> findByCategoryOrderByIdAsc(Category category, Pageable pageable);

    List<Post> findByCategoryOrderByIdDesc(Category category, Pageable pageable);

    List<Post> findByAuthor(User author);

    List<Post> findByAuthorOrderByIdAsc(User author, Pageable pageable);

    List<Post> findByAuthorOrderByIdDesc(User author, Pageable pageable);

    List<Post> findByEditor(User editor);

    List<Post> findByEditorOrderByIdAsc(User editor, Pageable pageable);

    List<Post> findByEditorOrderByIdDesc(User editor, Pageable pageable);

    List<Post> findAllByOrderByIdDesc(Pageable pageable);
}