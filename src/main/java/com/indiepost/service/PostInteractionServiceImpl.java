package com.indiepost.service;

import com.indiepost.dto.post.PostInteractionDto;
import com.indiepost.model.Bookmark;
import com.indiepost.model.PostReading;
import com.indiepost.model.User;
import com.indiepost.repository.BookmarkRepository;
import com.indiepost.repository.PostReadingRepository;
import com.indiepost.repository.UserRepository;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.time.LocalDateTime;

import static com.indiepost.utils.DateUtil.localDateTimeToInstant;

@Service
@Transactional
public class PostInteractionServiceImpl implements PostInteractionService {

    private final PostReadingRepository postReadingRepository;

    private final BookmarkRepository bookmarkRepository;

    private final UserRepository userRepository;

    @Inject
    public PostInteractionServiceImpl(PostReadingRepository postReadingRepository, UserRepository userRepository, BookmarkRepository bookmarkRepository) {
        this.postReadingRepository = postReadingRepository;
        this.bookmarkRepository = bookmarkRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Long add(Long userId, Long postId) {
        PostReading postReading = postReadingRepository.findOneByUserIdAndPostId(userId, postId);
        LocalDateTime now = LocalDateTime.now();
        if (postReading != null) {
            postReading.setLastRead(now);
            postReading.increaseReadCount();
            postReading.setVisible(true);
            return postReading.getId();
        }
        postReading = new PostReading();
        postReading.setCreated(now);
        postReading.setLastRead(now);
        postReadingRepository.save(postReading, userId, postId);
        return postReading.getId();
    }

    @Override
    public PostReading findOne(Long id) {
        return postReadingRepository.findOne(id);
    }

    @Override
    public PostInteractionDto findUsersByPostId(Long postId) {
        Long userId = getCurrentUserId();
        if (userId == null) {
            return null;
        }
        PostReading postReading = postReadingRepository.findOneByUserIdAndPostId(userId, postId);
        Bookmark bookmark = bookmarkRepository.findOneByUserIdAndPostId(userId, postId);
        PostInteractionDto dto = new PostInteractionDto(postId);
        if (postReading != null) {
            dto.setLastRead(localDateTimeToInstant(postReading.getLastRead()));
        }
        if (bookmark != null) {
            dto.setBookmarked(localDateTimeToInstant(bookmark.getCreated()));
        }
        return dto;
    }

    @Override
    public PostReading findOneByPostId(Long postId) {
        Long userId = getCurrentUserId();
        if (userId == null) {
            return null;
        }
        return postReadingRepository.findOneByUserIdAndPostId(userId, postId);
    }

    @Override
    public Bookmark findBookmark(Long userId, Long postId) {
        return bookmarkRepository.findOneByUserIdAndPostId(userId, postId);
    }

    @Override
    public void setInvisible(Long postId) {
        Long userId = getCurrentUserId();
        if (userId == null) {
            return;
        }
        PostReading postReading = postReadingRepository.findOneByUserIdAndPostId(userId, postId);
        if (postReading == null) {
            // TODO throw error
            return;
        }
        postReading.setVisible(false);
    }

    @Override
    public void setInvisibleAll() {
        Long userId = getCurrentUserId();
        if (userId == null) {
            return;
        }
        postReadingRepository.setVisibility(userId, false);
    }

    @Override
    public void setVisibleAll() {
        Long userId = getCurrentUserId();
        if (userId == null) {
            return;
        }
        postReadingRepository.setVisibility(userId, true);
    }

    @Override
    public void addBookmark(Long postId) {
        Long userId = getCurrentUserId();
        if (userId == null) {
            return;
        }
        bookmarkRepository.create(userId, postId);
    }

    @Override
    public void removeBookmark(Long postId) {
        Long userId = getCurrentUserId();
        if (userId == null) {
            return;
        }
        Bookmark bookmark = bookmarkRepository.findOneByUserIdAndPostId(userId, postId);
        if (bookmark != null) {
            bookmarkRepository.delete(bookmark);
        }
    }

    @Override
    public void removeAllUsersBookmarks() {
        Long userId = getCurrentUserId();
        if (userId == null) {
            return;
        }
        bookmarkRepository.removeAllBookmarksByUserId(userId);
    }

    @Override
    public void deleteById(Long id) {
        postReadingRepository.deleteById(id);
    }

    private Long getCurrentUserId() {
        User currentUser = userRepository.findCurrentUser();
        if (currentUser == null) {
            return null;
        }
        return currentUser.getId();
    }
}
