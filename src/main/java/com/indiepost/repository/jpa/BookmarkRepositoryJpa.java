package com.indiepost.repository.jpa;

import com.indiepost.model.Bookmark;
import com.indiepost.model.Post;
import com.indiepost.model.QBookmark;
import com.indiepost.model.User;
import com.indiepost.repository.BookmarkRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class BookmarkRepositoryJpa implements BookmarkRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void save(Bookmark bookmark) {
        entityManager.persist(bookmark);
    }

    @Override
    public void create(Long userId, Long postId) {
        Bookmark bookmark = findOneByUserIdAndPostId(userId, postId);
        if (bookmark != null) {
            return;
        }
        User userRef = entityManager.getReference(User.class, userId);
        Post postRef = entityManager.getReference(Post.class, postId);
        bookmark = new Bookmark(userRef, postRef);
        save(bookmark);
    }

    @Override
    public void delete(Bookmark bookmark) {
        entityManager.remove(bookmark);
    }

    @Override
    public Bookmark findOneByUserIdAndPostId(Long userId, Long postId) {
        QBookmark b = QBookmark.bookmark;
        return getQueryFactory()
                .selectFrom(b)
                .where(b.userId.eq(userId).and(b.postId.eq(postId)))
                .fetchOne();
    }

    @Override
    public List<Bookmark> findByUserIdAndPostIds(Long userId, List<Long> postIds) {
        QBookmark b = QBookmark.bookmark;
        return getQueryFactory()
                .selectFrom(b)
                .where(b.userId.eq(userId).and(b.postId.in(postIds)))
                .fetch();
    }

    @Override
    public void removeAllBookmarksByUserId(Long userId) {
        QBookmark b = QBookmark.bookmark;
        getQueryFactory()
                .delete(b)
                .where(b.userId.eq(userId))
                .execute();
    }

    private JPAQueryFactory getQueryFactory() {
        return new JPAQueryFactory(entityManager);
    }
}
