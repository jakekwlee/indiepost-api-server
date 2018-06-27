package com.indiepost.repository.jpa;

import com.indiepost.model.Post;
import com.indiepost.model.PostInteraction;
import com.indiepost.model.QPostInteraction;
import com.indiepost.model.User;
import com.indiepost.repository.PostInteractionRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class PostInteractionRepositoryJpa implements PostInteractionRepository {

    @PersistenceContext
    private EntityManager entityManager;

    private QPostInteraction interaction = QPostInteraction.postInteraction;

    @Override
    public PostInteraction findOne(Long id) {
        return entityManager.find(PostInteraction.class, id);
    }

    @Override
    public PostInteraction findOneByUserIdAndPostId(Long userId, Long postId) {
        return getQueryFactory().selectFrom(interaction).where(
                interaction.userId.eq(userId).and(
                        interaction.postId.eq(postId))
        ).fetchOne();
    }

    @Override
    public List<PostInteraction> findByUserIdAndPostIds(Long userId, List<Long> postIds) {
        return getQueryFactory().selectFrom(interaction)
                .where(interaction.userId.eq(userId).and(interaction.postId.in(postIds)))
                .orderBy(interaction.lastRead.desc())
                .fetch();
    }

    @Override
    public void save(PostInteraction postInteraction) {
        entityManager.persist(postInteraction);
    }

    @Override
    public Long save(PostInteraction postInteraction, Long userId, Long postId) {
        User userRef = entityManager.getReference(User.class, userId);
        Post postRef = entityManager.getReference(Post.class, postId);
        postInteraction.setUser(userRef);
        postInteraction.setPost(postRef);
        save(postInteraction);
        entityManager.flush();
        return postInteraction.getId();
    }

    @Override
    public void setVisibility(Long userId, boolean visible) {
        getQueryFactory().update(interaction).set(interaction.visible, visible)
                .where(interaction.userId.eq(userId).and(interaction.id.goe(1)))
                .execute();
    }

    @Override
    public void clearAllBookmarks(Long userId) {
        getQueryFactory().update(interaction).setNull(interaction.bookmarked)
                .where(interaction.userId.eq(userId).and(interaction.id.goe(1)))
                .execute();
    }

    @Override
    public void deleteById(Long id) {
        PostInteraction postInteraction = entityManager.getReference(PostInteraction.class, id);
        entityManager.remove(postInteraction);
    }

    private JPAQueryFactory getQueryFactory() {
        return new JPAQueryFactory(entityManager);
    }
}
