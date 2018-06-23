package com.indiepost.service;

import com.indiepost.model.UserReading;
import com.indiepost.repository.UserReadingRepository;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Service
@Transactional
public class UserReadingServiceImpl implements UserReadingService {

    private final UserReadingRepository userReadingRepository;

    @Inject
    public UserReadingServiceImpl(UserReadingRepository userReadingRepository) {
        this.userReadingRepository = userReadingRepository;
    }

    @Override
    public Long add(Long userId, Long postId) {
        UserReading userReading = userReadingRepository.findOneByUserIdAndPostId(userId, postId);
        LocalDateTime now = LocalDateTime.now();
        if (userReading != null) {
            userReading.setLastRead(now);
            userReading.increaseReadCount();
            return userReading.getId();
        }
        userReading = new UserReading();
        userReading.setCreated(now);
        userReading.setLastRead(now);
        userReadingRepository.save(userReading, userId, postId);
        return userReading.getId();
    }

    @Override
    public UserReading findOne(Long id) {
        return userReadingRepository.findOne(id);
    }

    @Override
    public UserReading findOneByUserIdAndPostId(Long userId, Long postId) {
        return userReadingRepository.findOneByUserIdAndPostId(userId, postId);
    }

    @Override
    public void setInvisible(Long userId, Long postId) {
        UserReading userReading = userReadingRepository.findOneByUserIdAndPostId(userId, postId);
        if (userReading == null) {
            // TODO throw error
            return;
        }
        userReading.setVisible(false);
    }

    @Override
    public void setInvisibleAllByUserId(Long userId) {
        userReadingRepository.setVisibility(userId, false);
    }

    @Override
    public void setVisibleAllByUserId(Long userId) {
        userReadingRepository.setVisibility(userId, true);
    }

    @Override
    public void setBookmark(Long userId, Long postId) {
        UserReading userReading = userReadingRepository.findOneByUserIdAndPostId(userId, postId);
        userReading.setBookmarked(true);
        userReading.setBookmarkedAt(LocalDateTime.now());
    }

    @Override
    public void unsetBookmark(Long userId, Long postId) {
        UserReading userReading = userReadingRepository.findOneByUserIdAndPostId(userId, postId);
        userReading.setBookmarked(false);
        userReading.setBookmarkedAt(null);
    }

    @Override
    public void deleteById(Long id) {
        userReadingRepository.deleteById(id);
    }
}
