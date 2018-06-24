package com.indiepost.service;

import com.indiepost.dto.post.PostInteractionDto;
import com.indiepost.model.PostInteraction;
import com.indiepost.model.User;
import com.indiepost.repository.PostInteractionRepository;
import com.indiepost.repository.UserRepository;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.time.LocalDateTime;

import static com.indiepost.utils.DateUtil.localDateTimeToInstant;

@Service
@Transactional
public class PostInteractionServiceImpl implements PostInteractionService {

    private final PostInteractionRepository postInteractionRepository;

    private final UserRepository userRepository;

    @Inject
    public PostInteractionServiceImpl(PostInteractionRepository postInteractionRepository, UserRepository userRepository) {
        this.postInteractionRepository = postInteractionRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Long add(Long userId, Long postId) {
        PostInteraction postInteraction = postInteractionRepository.findOneByUserIdAndPostId(userId, postId);
        LocalDateTime now = LocalDateTime.now();
        if (postInteraction != null) {
            postInteraction.setLastRead(now);
            postInteraction.increaseReadCount();
            return postInteraction.getId();
        }
        postInteraction = new PostInteraction();
        postInteraction.setCreated(now);
        postInteraction.setLastRead(now);
        postInteractionRepository.save(postInteraction, userId, postId);
        return postInteraction.getId();
    }

    @Override
    public PostInteraction findOne(Long id) {
        return postInteractionRepository.findOne(id);
    }

    @Override
    public PostInteractionDto findUsersByPostId(Long postId) {
        Long userId = getCurrentUserId();
        if (userId == null) {
            return null;
        }
        PostInteraction postInteraction = postInteractionRepository.findOneByUserIdAndPostId(userId, postId);
        PostInteractionDto dto = new PostInteractionDto(postId);
        if (postInteraction != null) {
            dto.setLastRead(localDateTimeToInstant(postInteraction.getLastRead()));
            if (postInteraction.getBookmarked() != null) {
                dto.setBookmarked(localDateTimeToInstant(postInteraction.getBookmarked()));
            }
        }
        return dto;
    }

    @Override
    public PostInteraction findOneByPostId(Long postId) {
        Long userId = getCurrentUserId();
        if (userId == null) {
            return null;
        }
        return postInteractionRepository.findOneByUserIdAndPostId(userId, postId);
    }

    @Override
    public void setInvisible(Long postId) {
        Long userId = getCurrentUserId();
        if (userId == null) {
            return;
        }
        PostInteraction postInteraction = postInteractionRepository.findOneByUserIdAndPostId(userId, postId);
        if (postInteraction == null) {
            // TODO throw error
            return;
        }
        postInteraction.setVisible(false);
    }

    @Override
    public void setInvisibleAll() {
        Long userId = getCurrentUserId();
        if (userId == null) {
            return;
        }
        postInteractionRepository.setVisibility(userId, false);
    }

    @Override
    public void setVisibleAll() {
        Long userId = getCurrentUserId();
        if (userId == null) {
            return;
        }
        postInteractionRepository.setVisibility(userId, true);
    }

    @Override
    public void setBookmark(Long postId) {
        Long userId = getCurrentUserId();
        if (userId == null) {
            return;
        }
        PostInteraction postInteraction = postInteractionRepository.findOneByUserIdAndPostId(userId, postId);
        postInteraction.setBookmarked(LocalDateTime.now());
    }

    @Override
    public void unsetBookmark(Long postId) {
        Long userId = getCurrentUserId();
        if (userId == null) {
            return;
        }
        PostInteraction postInteraction = postInteractionRepository.findOneByUserIdAndPostId(userId, postId);
        postInteraction.setBookmarked(null);
    }

    @Override
    public void deleteById(Long id) {
        postInteractionRepository.deleteById(id);
    }

    private Long getCurrentUserId() {
        User currentUser = userRepository.findCurrentUser();
        if (currentUser == null) {
            return null;
        }
        return currentUser.getId();
    }
}
