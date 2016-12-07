package com.indiepost.service;

import com.indiepost.enums.PostEnum;
import com.indiepost.model.Category;
import com.indiepost.model.Post;
import com.indiepost.model.Tag;
import com.indiepost.model.User;
import com.indiepost.model.legacy.Contentlist;
import com.indiepost.repository.PostRepository;
import com.indiepost.requestModel.admin.PostRequest;
import com.indiepost.responseModel.admin.PostResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by jake on 7/30/16.
 */
@Service
@Transactional
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private TagService tagService;

    @Autowired
    private LegacyPostService legacyPostService;

    @Override
    public Long save(Post post) {
        return postRepository.save(post);
    }

    @Override
    public PostResponse save(PostRequest postRequest) {
        Post post = new Post();
        copyRequestToPost(post, postRequest);
        User user = userService.getCurrentUser();
        if (post.getAuthor() == null) {
            post.setAuthor(user);
        }
        post.setEditor(user);
        post.setCreatedAt(new Date());
        post.setModifiedAt(new Date());
        return copyPostToResponse(post);
    }

    @Override
    public PostResponse createAutosave(PostRequest postRequest) {
        Post post = new Post();
        User user = userService.getCurrentUser();

        post.setCreatedAt(new Date());
        post.setModifiedAt(new Date());
        post.setAuthor(user);
        post.setEditor(user);
        post.setPostType(PostEnum.Type.POST);
        post.setStatus(PostEnum.Status.AUTOSAVE);

        Long originalId = postRequest.getId();

        if (originalId != null) {
            Post originalPost = postRepository.findById(originalId);
            post.setOriginal(originalPost);
        }

        copyRequestToPost(post, postRequest);
        post.setStatus(PostEnum.Status.AUTOSAVE);

        save(post);

        return copyPostToResponse(post);
    }

    @Override
    public void updateAutosave(Long id, PostRequest postRequest) {
        Post post = findById(postRequest.getId());
        copyRequestToPost(post, postRequest);
        post.setStatus(PostEnum.Status.AUTOSAVE);
        update(post);
    }

    @Override
    public PostResponse update(Long id, PostRequest postRequest) {
        Long originalId = postRequest.getOriginalId();
        Post post = findById(id);

        // if there is an original
        if (originalId != null && originalId != postRequest.getId()) {
            postRequest.setOriginalId(null);
            postRequest.setStatus(null);
            Post autosaveOrDraft = findById(postRequest.getId());
            delete(autosaveOrDraft);
        }
        copyRequestToPost(post, postRequest);
        post.setModifiedAt(new Date());

        PostEnum.Status status = post.getStatus();

        if (status == PostEnum.Status.PUBLISH ||
                status == PostEnum.Status.FUTURE) {
            if (post.getLegacyPost() == null) {
                Contentlist contentlist = legacyPostService.save(post);
                post.setLegacyPost(contentlist);
            } else {
                legacyPostService.update(post);
            }
        }

        update(post);
        return getPostResponse(post.getId());
    }

    @Override
    public Post findById(Long id) {
        return postRepository.findById(id);
    }

    @Override
    public void update(Post post) {
        postRepository.update(post);
    }

    @Override
    public void delete(Post post) {
        postRepository.delete(post);
    }

    @Override
    public void deleteById(Long id) {
        Post post = postRepository.findById(id);
        delete(post);
        legacyPostService.deleteById(id);
    }

    @Override
    public Long count() {
        return postRepository.count();
    }

    @Override
    public Long count(PostEnum.Status status) {
        return postRepository.count(status);
    }

    @Override
    public List<Post> findAll(int page, int maxResults, boolean isDesc) {
        return postRepository.findAll(getPageable(page, maxResults, isDesc));
    }

    @Override
    public List<Post> findAll(PostEnum.Status status, User author, Category category, int page, int maxResults, boolean isDesc) {
        return postRepository.findAll(status, author.getId(), category.getId(), getPageable(page, maxResults, isDesc));
    }

    @Override
    public List<Post> findByCategory(Category category, int page, int maxResults, boolean isDesc) {
        return postRepository
                .findByCategoryId(category.getId(), getPageable(page, maxResults, isDesc));
    }

    @Override
    public List<Post> findByCategorySlug(String slug, int page, int maxResults, boolean isDesc) {
        return postRepository
                .findByCategorySlug(slug, getPageable(page, maxResults, isDesc));
    }

    @Override
    public List<Post> findByAuthor(User user, int page, int maxResults, boolean isDesc) {
        return postRepository
                .findByAuthorId(user.getId(), getPageable(page, maxResults, isDesc));
    }

    @Override
    public List<Post> findByAuthorName(String authorName, int page, int maxResults, boolean isDesc) {
        return postRepository
                .findByAuthorName(authorName, getPageable(page, maxResults, isDesc));
    }

    @Override
    public List<Post> findByStatus(PostEnum.Status status, int page, int maxResults, boolean isDesc) {
        return postRepository
                .findByStatus(status, getPageable(page, maxResults, isDesc));
    }

    @Override
    public List<Post> findByTag(Tag tag, int page, int maxResults, boolean isDesc) {
        return postRepository
                .findByTagId(tag.getId(), getPageable(page, maxResults, isDesc));
    }

    @Override
    public List<Post> findByTagName(String tagName, int page, int maxResults, boolean isDesc) {
        return postRepository
                .findByTagName(tagName, getPageable(page, maxResults, isDesc));
    }

    @Override
    public PostResponse getPostResponse(Long id) {
        Post post = findById(id);
        return copyPostToResponse(post);
    }

    @Override
    public void publishPosts() {
        List<Post> posts = postRepository.findPostToPublish();
        for (Post post : posts) {
            post.setStatus(PostEnum.Status.PUBLISH);
            postRepository.save(post);
        }
    }

    // no id copy
    private void copyRequestToPost(Post post, PostRequest postRequest) {
        String title = postRequest.getTitle();
        String content = postRequest.getContent();
        String excerpt = postRequest.getExcerpt();
        String displayName = postRequest.getDisplayName();
        String featuredImage = postRequest.getFeaturedImage();
        Long categoryId = postRequest.getCategoryId();
        List<String> tagStringList = postRequest.getTags();
        Date publishedAt = postRequest.getPublishedAt();
        String status = postRequest.getStatus();
        Long originalId = postRequest.getOriginalId();

        if (title != null) {
            post.setTitle(title);
        } else {
            post.setTitle("No Title");
        }

        if (content != null) {
            post.setContent(content);
        } else {
            post.setContent("");
        }

        if (excerpt != null) {
            post.setExcerpt(excerpt);
        } else {
            post.setExcerpt("");
        }

        if (displayName != null) {
            post.setDisplayName(displayName);
        } else {
            post.setDisplayName("Indiepost");
        }

        if (featuredImage != null) {
            post.setFeaturedImage(featuredImage);
        } else {
            post.setFeaturedImage("");
        }

        if (categoryId != null) {
            post.setCategory(categoryService.findById(categoryId));
        } else {
            post.setCategory(categoryService.findBySlug("film"));
        }

        if (publishedAt != null) {
            post.setPublishedAt(publishedAt);
        } else {
            post.setPublishedAt(new Date());
        }

        if (status != null) {
            post.setStatus(PostEnum.Status.valueOf(status));
        }

        if (originalId != null) {
            Post originalPost = postRepository.findById(originalId);
            post.setOriginal(originalPost);
        }

        post.clearTags();
        if (tagStringList != null) {
            for (String tagString : tagStringList) {
                Tag tag = tagService.findByName(tagString);
                if (tag == null) {
                    tag = new Tag();
                    tag.setName(tagString);
                    tagService.save(tag);
                }
                post.addTag(tag);
            }
        }
    }

    // it does id copy
    private PostResponse copyPostToResponse(Post post) {
        PostResponse postResponse = new PostResponse();
        postResponse.setId(post.getId());
        postResponse.setPublishedAt(post.getPublishedAt());
        postResponse.setModifiedAt(post.getModifiedAt());
        postResponse.setCreatedAt(post.getCreatedAt());
        postResponse.setAuthorId(post.getAuthor().getId());
        postResponse.setEditorId(post.getEditor().getId());
        postResponse.setCategoryId(post.getCategory().getId());
        postResponse.setTitle(post.getTitle());
        postResponse.setContent(post.getContent());
        postResponse.setExcerpt(post.getExcerpt());
        postResponse.setDisplayName(post.getDisplayName());
        postResponse.setFeaturedImage(post.getFeaturedImage());
        postResponse.setPostType(post.getPostType().toString());
        postResponse.setStatus(post.getStatus().toString());
        if (post.getOriginal() != null) {
            postResponse.setOriginalId(post.getOriginal().getId());
        }

        Set<Tag> tags = post.getTags();
        if (tags != null) {
            List<String> tagStringList = new ArrayList<>();
            for (Tag tag : tags) {
                tagStringList.add(tag.getName());
            }
            postResponse.setTags(tagStringList);
        }
        return postResponse;
    }

    private Pageable getPageable(int page, int maxResults, boolean isDesc) {
        return isDesc ?
                new PageRequest(page, maxResults, Sort.Direction.DESC, "publishedAt") :
                new PageRequest(page, maxResults, Sort.Direction.ASC, "publishedAt");
    }
}
