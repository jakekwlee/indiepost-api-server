package com.indiepost.service;

import com.indiepost.enums.PostEnum;
import com.indiepost.model.Category;
import com.indiepost.model.Post;
import com.indiepost.model.Tag;
import com.indiepost.model.User;
import com.indiepost.model.legacy.Contentlist;
import com.indiepost.repository.PostRepository;
import com.indiepost.model.request.AdminPostRequest;
import com.indiepost.model.response.AdminPostResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

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
    public AdminPostResponse save(AdminPostRequest adminPostRequest) {
        Post post = new Post();
        copyRequestToPost(post, adminPostRequest);
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
    public AdminPostResponse createAutosave(AdminPostRequest adminPostRequest) {
        Post autosave = new Post();
        User user = userService.getCurrentUser();

        autosave.setModifiedAt(new Date());
        autosave.setStatus(PostEnum.Status.AUTOSAVE);
        autosave.setEditor(user);

        Long originalId = adminPostRequest.getOriginalId() != null ?
                adminPostRequest.getOriginalId() :
                adminPostRequest.getId();

        if (originalId != null) {
            Post originalPost = postRepository.findById(adminPostRequest.getId());
            autosave.setOriginal(originalPost);

            if (adminPostRequest.getTitle() != null) {
                autosave.setTitle(adminPostRequest.getTitle());
            } else {
                autosave.setTitle(originalPost.getTitle());
            }
            if (adminPostRequest.getContent() != null) {
                autosave.setContent(adminPostRequest.getContent());
            } else {
                autosave.setContent(originalPost.getContent());
            }
            if (adminPostRequest.getExcerpt() != null) {
                autosave.setExcerpt(adminPostRequest.getExcerpt());
            } else {
                autosave.setExcerpt(originalPost.getExcerpt());
            }
            if (adminPostRequest.getDisplayName() != null) {
                autosave.setDisplayName(adminPostRequest.getDisplayName());
            } else {
                autosave.setDisplayName(originalPost.getDisplayName());
            }
            if (adminPostRequest.getFeaturedImage() != null) {
                autosave.setFeaturedImage(adminPostRequest.getFeaturedImage());
            } else {
                autosave.setFeaturedImage(originalPost.getFeaturedImage());
            }
            if (adminPostRequest.getCategoryId() != null) {
                Category category = categoryService.findById(
                        adminPostRequest.getCategoryId()
                );
                autosave.setCategory(category);
            } else {
                Category category = categoryService.findBySlug("film");
                autosave.setCategory(category);
            }
            if (adminPostRequest.getPublishedAt() != null) {
                autosave.setPublishedAt(adminPostRequest.getPublishedAt());
            } else {
                autosave.setPublishedAt(adminPostRequest.getPublishedAt());
            }
            if (adminPostRequest.getTags() != null) {
                copyTagStringListToPost(
                        adminPostRequest.getTags(),
                        autosave
                );
            } else {
                for (Tag tag : originalPost.getTags()) {
                    autosave.addTag(tag);
                }
            }

        }
        Date tomorrow = new Date();
        LocalDateTime.from(tomorrow.toInstant()).plusDays(7);
        if (adminPostRequest.getTitle() != null) {
           // Todo
        }


//        post.setCreatedAt(new Date());
//        post.setModifiedAt(new Date());
//        post.setAuthor(user);
//        post.setEditor(user);
//        post.setPostType(PostEnum.Type.POST);
//        post.setStatus(PostEnum.Status.AUTOSAVE);
//
//        Long originalId = adminPostRequest.getId();
//
//        if (originalId != null) {
//            Post originalPost = postRepository.findById(originalId);
//            post.setOriginal(originalPost);
//        }
//
//        copyRequestToPost(post, adminPostRequest);
//        post.setStatus(PostEnum.Status.AUTOSAVE);
//
//        save(post);

        return copyPostToResponse(autosave);
    }

    @Override
    public void updateAutosave(Long id, AdminPostRequest adminPostRequest) {
        Post post = findById(adminPostRequest.getId());
        copyRequestToPost(post, adminPostRequest);
        post.setStatus(PostEnum.Status.AUTOSAVE);
        update(post);
    }

    @Override
    public AdminPostResponse update(Long id, AdminPostRequest adminPostRequest) {
        Long originalId = adminPostRequest.getOriginalId();
        Post post = findById(id);

        // if there is an original
        if (originalId != null && originalId != adminPostRequest.getId()) {
            adminPostRequest.setOriginalId(null);
            adminPostRequest.setStatus(null);
            Post autosaveOrDraft = findById(adminPostRequest.getId());
            delete(autosaveOrDraft);
        }
        copyRequestToPost(post, adminPostRequest);
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
    public AdminPostResponse getPostResponse(Long id) {
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
    private void copyRequestToPost(Post post, AdminPostRequest adminPostRequest) {
        String title = adminPostRequest.getTitle();
        String content = adminPostRequest.getContent();
        String excerpt = adminPostRequest.getExcerpt();
        String displayName = adminPostRequest.getDisplayName();
        String featuredImage = adminPostRequest.getFeaturedImage();
        Long categoryId = adminPostRequest.getCategoryId();
        List<String> tagStringList = adminPostRequest.getTags();
        Date publishedAt = adminPostRequest.getPublishedAt();
        String status = adminPostRequest.getStatus();
        Long originalId = adminPostRequest.getOriginalId();

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
    private AdminPostResponse copyPostToResponse(Post post) {
        AdminPostResponse adminPostResponse = new AdminPostResponse();
        adminPostResponse.setId(post.getId());
        adminPostResponse.setPublishedAt(post.getPublishedAt());
        adminPostResponse.setModifiedAt(post.getModifiedAt());
        adminPostResponse.setCreatedAt(post.getCreatedAt());
        adminPostResponse.setAuthorId(post.getAuthor().getId());
        adminPostResponse.setEditorId(post.getEditor().getId());
        adminPostResponse.setCategoryId(post.getCategory().getId());
        adminPostResponse.setTitle(post.getTitle());
        adminPostResponse.setContent(post.getContent());
        adminPostResponse.setExcerpt(post.getExcerpt());
        adminPostResponse.setDisplayName(post.getDisplayName());
        adminPostResponse.setFeaturedImage(post.getFeaturedImage());
        adminPostResponse.setPostType(post.getPostType().toString());
        adminPostResponse.setStatus(post.getStatus().toString());
        if (post.getOriginal() != null) {
            adminPostResponse.setOriginalId(post.getOriginal().getId());
        }

        Set<Tag> tags = post.getTags();
        if (tags != null) {
            List<String> tagStringList = new ArrayList<>();
            for (Tag tag : tags) {
                tagStringList.add(tag.getName());
            }
            adminPostResponse.setTags(tagStringList);
        }
        return adminPostResponse;
    }

    private void copyTagStringListToPost(List<String> tagStringList, Post post) {
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

    private Pageable getPageable(int page, int maxResults, boolean isDesc) {
        return isDesc ?
                new PageRequest(page, maxResults, Sort.Direction.DESC, "publishedAt") :
                new PageRequest(page, maxResults, Sort.Direction.ASC, "publishedAt");
    }
}
