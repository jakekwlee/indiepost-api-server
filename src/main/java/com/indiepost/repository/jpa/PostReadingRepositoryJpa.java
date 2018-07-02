package com.indiepost.repository.jpa;

import com.indiepost.model.Post;
import com.indiepost.model.PostReading;
import com.indiepost.model.QPostReading;
import com.indiepost.model.User;
import com.indiepost.repository.PostReadingRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class PostReadingRepositoryJpa implements PostReadingRepository {

    @PersistenceContext
    private EntityManager entityManager;

    private QPostReading interaction = QPostReading.postReading;

    @Override
    public PostReading findOne(Long id) {
        return entityManager.find(PostReading.class, id);
    }

    @Override
    public PostReading findOneByUserIdAndPostId(Long userId, Long postId) {
        return getQueryFactory().selectFrom(interaction).where(
                interaction.userId.eq(userId).and(
                        interaction.postId.eq(postId))
        ).fetchOne();
    }

    @Override
    public List<PostReading> findByUserIdAndPostIds(Long userId, List<Long> postIds) {
        return getQueryFactory().selectFrom(interaction)
                .where(interaction.userId.eq(userId).and(interaction.postId.in(postIds)))
                .orderBy(interaction.lastRead.desc())
                .fetch();
    }

    @Override
    public void save(PostReading postReading) {
        entityManager.persist(postReading);
    }

    @Override
    public Long save(PostReading postReading, Long userId, Long postId) {
        User userRef = entityManager.getReference(User.class, userId);
        Post postRef = entityManager.getReference(Post.class, postId);
        postReading.setUser(userRef);
        postReading.setPost(postRef);
        save(postReading);
        entityManager.flush();
        return postReading.getId();
    }

    @Override
    public void setVisibility(Long userId, boolean visible) {
        getQueryFactory().update(interaction).set(interaction.visible, visible)
                .where(interaction.userId.eq(userId).and(interaction.id.goe(1)))
                .execute();
    }

    @Override
    public void deleteById(Long id) {
        PostReading postReading = entityManager.getReference(PostReading.class, id);
        entityManager.remove(postReading);
    }

    private JPAQueryFactory getQueryFactory() {
        return new JPAQueryFactory(entityManager);
    }
}
