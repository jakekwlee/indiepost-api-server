package com.indiepost.repository.jpa;

import com.indiepost.model.Post;
import com.indiepost.model.QUserRead;
import com.indiepost.model.User;
import com.indiepost.model.UserRead;
import com.indiepost.repository.UserReadRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class UserReadRepositoryJpa implements UserReadRepository {

    @PersistenceContext
    private EntityManager entityManager;

    private QUserRead ur = QUserRead.userRead;

    @Override
    public UserRead findOne(Long id) {
        return entityManager.find(UserRead.class, id);
    }

    @Override
    public UserRead findOneByUserIdAndPostId(Long userId, Long postId) {
        return getQueryFactory().selectFrom(ur).where(
                ur.userId.eq(userId).and(
                        ur.postId.eq(postId))
        ).fetchOne();
    }

    @Override
    public void save(UserRead userRead) {
        entityManager.persist(userRead);
    }

    @Override
    public Long save(UserRead userRead, Long userId, Long postId) {
        User userRef = entityManager.getReference(User.class, userId);
        Post postRef = entityManager.getReference(Post.class, postId);
        userRead.setUser(userRef);
        userRead.setPost(postRef);
        save(userRead);
        entityManager.flush();
        return userRead.getId();
    }

    @Override
    public void setInvisibleAll(Long userId) {
        getQueryFactory().update(ur).set(ur.visible, false)
                .where(ur.userId.eq(userId).and(ur.id.goe(1)))
                .execute();
    }

    @Override
    public void deleteById(Long id) {
        UserRead userRead = entityManager.getReference(UserRead.class, id);
        entityManager.remove(userRead);
    }

    private JPAQueryFactory getQueryFactory() {
        return new JPAQueryFactory(entityManager);
    }
}
