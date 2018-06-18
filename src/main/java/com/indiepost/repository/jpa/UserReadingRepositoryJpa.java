package com.indiepost.repository.jpa;

import com.indiepost.model.Post;
import com.indiepost.model.QUserReading;
import com.indiepost.model.User;
import com.indiepost.model.UserReading;
import com.indiepost.repository.UserReadingRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class UserReadingRepositoryJpa implements UserReadingRepository {

    @PersistenceContext
    private EntityManager entityManager;

    private QUserReading ur = QUserReading.userReading;

    @Override
    public UserReading findOne(Long id) {
        return getQueryFactory().selectFrom(ur).fetchOne();
    }

    @Override
    public UserReading findOneByUserIdAndPostId(Long userId, Long postId) {
        return getQueryFactory().selectFrom(ur).where(
                ur.userId.eq(userId).and(
                        ur.postId.eq(postId))
        ).fetchOne();
    }

    @Override
    public void save(UserReading userReading) {
        entityManager.persist(userReading);
    }

    @Override
    public void save(UserReading userReading, Long userId, Long postId) {
        User userRef = entityManager.getReference(User.class, userId);
        Post postRef = entityManager.getReference(Post.class, postId);
        userReading.setUser(userRef);
        userReading.setPost(postRef);
        save(userReading);
    }

    @Override
    public void setInvisibleAll(Long userId) {
        getQueryFactory().update(ur).set(ur.visible, false)
                .where(ur.userId.eq(userId).and(ur.id.goe(1)))
                .execute();
    }

    @Override
    public void deleteById(Long id) {
        UserReading userReading = entityManager.getReference(UserReading.class, id);
        entityManager.remove(userReading);
    }

    private JPAQueryFactory getQueryFactory() {
        return new JPAQueryFactory(entityManager);
    }
}
