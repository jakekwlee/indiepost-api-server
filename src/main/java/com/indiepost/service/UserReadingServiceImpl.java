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
    public void addUserReading(Long userId, Long postId) {
        UserReading userReading = userReadingRepository.findOneByUserIdAndPostId(userId, postId);
        LocalDateTime now = LocalDateTime.now();
        if (userReading != null) {
            userReading.setLastRead(now);
            userReading.increaseReadCount();
            return;
        }
        userReading = new UserReading();
        userReading.setFirstRead(now);
        userReading.setLastRead(now);
        userReadingRepository.save(userReading, userId, postId);
    }

    @Override
    public void setUserReadingInvisible(Long userId, Long postId) {
        UserReading userReading = userReadingRepository.findOneByUserIdAndPostId(userId, postId);
        if (userReading == null) {
            // TODO throw error
            return;
        }
        userReading.setVisible(false);
    }

    @Override
    public void setInvisibleAll(Long userId) {
        userReadingRepository.setInvisibleAll(userId);
    }

    @Override
    public void addBookmark(Long userId, Long postId) {
        UserReading userReading = userReadingRepository.findOneByUserIdAndPostId(userId, postId);
        userReading.setBookmarked(true);
        userReading.setBookmarkedAt(LocalDateTime.now());
    }

    @Override
    public void removeBookmark(Long userId, Long postId) {
        UserReading userReading = userReadingRepository.findOneByUserIdAndPostId(userId, postId);
        userReading.setBookmarked(false);
        userReading.setBookmarkedAt(null);
    }
}
