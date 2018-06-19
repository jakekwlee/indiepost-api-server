package com.indiepost.service;

import com.indiepost.model.UserRead;
import com.indiepost.repository.UserReadRepository;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Service
@Transactional
public class UserReadServiceImpl implements UserReadService {

    private final UserReadRepository userReadRepository;

    @Inject
    public UserReadServiceImpl(UserReadRepository userReadRepository) {
        this.userReadRepository = userReadRepository;
    }

    @Override
    public Long add(Long userId, Long postId) {
        UserRead userRead = userReadRepository.findOneByUserIdAndPostId(userId, postId);
        LocalDateTime now = LocalDateTime.now();
        if (userRead != null) {
            userRead.setLastRead(now);
            userRead.increaseReadCount();
            return userRead.getId();
        }
        userRead = new UserRead();
        userRead.setFirstRead(now);
        userRead.setLastRead(now);
        userReadRepository.save(userRead, userId, postId);
        return userRead.getId();
    }

    @Override
    public UserRead findOne(Long id) {
        return userReadRepository.findOne(id);
    }

    @Override
    public UserRead findOneByUserIdAndPostId(Long userId, Long postId) {
        return userReadRepository.findOneByUserIdAndPostId(userId, postId);
    }

    @Override
    public void setInvisible(Long userId, Long postId) {
        UserRead userRead = userReadRepository.findOneByUserIdAndPostId(userId, postId);
        if (userRead == null) {
            // TODO throw error
            return;
        }
        userRead.setVisible(false);
    }

    @Override
    public void setInvisibleAllByUserId(Long userId) {
        userReadRepository.setInvisibleAll(userId);
    }

    @Override
    public void setBookmark(Long userId, Long postId) {
        UserRead userRead = userReadRepository.findOneByUserIdAndPostId(userId, postId);
        userRead.setBookmarked(true);
        userRead.setBookmarkedAt(LocalDateTime.now());
    }

    @Override
    public void unsetBookmark(Long userId, Long postId) {
        UserRead userRead = userReadRepository.findOneByUserIdAndPostId(userId, postId);
        userRead.setBookmarked(false);
        userRead.setBookmarkedAt(null);
    }

    @Override
    public void deleteById(Long id) {
        userReadRepository.deleteById(id);
    }
}
