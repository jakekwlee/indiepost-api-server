package com.indiepost.service;

import com.indiepost.enums.PostEnum;
import com.indiepost.model.Category;
import com.indiepost.model.Post;
import com.indiepost.model.Tag;
import com.indiepost.model.User;
import com.indiepost.model.legacy.Contentlist;
import com.indiepost.repository.PostRepository;
import dto.request.AdminPostRequestDto;
import dto.response.AdminPostResponseDto;
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
    public AdminPostResponseDto save(AdminPostRequestDto adminPostRequestDto) {
        Post post = new Post();
        copyRequestToPost(post, adminPostRequestDto);
        User user = userService.getCurrentUser();
        if (post.getAuthor() == null) {
            post.setAuthor(user);
        }
        post.setEditor(user);
        post.setCreatedAt(LocalDateTime.now());
        post.setModifiedAt(LocalDateTime.now());
        return copyPostToResponse(post);
    }

    @Override
    public AdminPostResponseDto createAutosave(AdminPostRequestDto adminPostRequestDto) {
        Post autosave = new Post();
        User user = userService.getCurrentUser();

        autosave.setModifiedAt(LocalDateTime.now());
        autosave.setStatus(PostEnum.Status.AUTOSAVE);
        autosave.setEditor(user);

        Long originalId = adminPostRequestDto.getOriginalId() != null ?
                adminPostRequestDto.getOriginalId() :
                adminPostRequestDto.getId();

        if (originalId != null) {
            Post originalPost = postRepository.findById(adminPostRequestDto.getId());
            autosave.setOriginal(originalPost);

            if (adminPostRequestDto.getTitle() != null) {
                autosave.setTitle(adminPostRequestDto.getTitle());
            } else {
                autosave.setTitle(originalPost.getTitle());
            }
            if (adminPostRequestDto.getContent() != null) {
                autosave.setContent(adminPostRequestDto.getContent());
            } else {
                autosave.setContent(originalPost.getContent());
            }
            if (adminPostRequestDto.getExcerpt() != null) {
                autosave.setExcerpt(adminPostRequestDto.getExcerpt());
            } else {
                autosave.setExcerpt(originalPost.getExcerpt());
            }
            if (adminPostRequestDto.getDisplayName() != null) {
                autosave.setDisplayName(adminPostRequestDto.getDisplayName());
            } else {
                autosave.setDisplayName(originalPost.getDisplayName());
            }
            if (adminPostRequestDto.getFeaturedImage() != null) {
                autosave.setFeaturedImage(adminPostRequestDto.getFeaturedImage());
            } else {
                autosave.setFeaturedImage(originalPost.getFeaturedImage());
            }
            if (adminPostRequestDto.getCategoryId() != null) {
                Category category = categoryService.findById(
                        adminPostRequestDto.getCategoryId()
                );
                autosave.setCategory(category);
            } else {
                Category category = categoryService.findBySlug("film");
                autosave.setCategory(category);
            }
//            if (adminPostRequestDto.getPublishedAt() != null) {
//                autosave.setPublishedAt(adminPostRequestDto.getPublishedAt());
//            } else {
//                autosave.setPublishedAt(adminPostRequestDto.getPublishedAt());
//            }
//            if (adminPostRequestDto.getTags() != null) {
//                copyTagStringListToPost(
//                        adminPostRequestDto.getTags(),
//                        autosave
//                );
//            } else {
//                for (Tag tag : originalPost.getTags()) {
//                    autosave.addTag(tag);
//                }
//            }

        }
        LocalDateTime.now().plusDays(7);
        if (adminPostRequestDto.getTitle() != null) {
           // Todo
        }


//        post.setCreatedAt(new Date());
//        post.setModifiedAt(new Date());
//        post.setAuthor(user);
//        post.setEditor(user);
//        post.setPostType(PostEnum.Type.POST);
//        post.setStatus(PostEnum.Status.AUTOSAVE);
//
//        Long originalId = adminPostRequestDto.getId();
//
//        if (originalId != null) {
//            Post originalPost = postRepository.findById(originalId);
//            post.setOriginal(originalPost);
//        }
//
//        copyRequestToPost(post, adminPostRequestDto);
//        post.setStatus(PostEnum.Status.AUTOSAVE);
//
//        save(post);

        return copyPostToResponse(autosave);
    }

    @Override
    public void updateAutosave(Long id, AdminPostRequestDto adminPostRequestDto) {
        Post post = findById(adminPostRequestDto.getId());
        copyRequestToPost(post, adminPostRequestDto);
        post.setStatus(PostEnum.Status.AUTOSAVE);
        update(post);
    }

    @Override
    public AdminPostResponseDto update(Long id, AdminPostRequestDto adminPostRequestDto) {
        Long originalId = adminPostRequestDto.getOriginalId();
        Post post = findById(id);

        // if there is an original
        if (originalId != null && originalId != adminPostRequestDto.getId()) {
            adminPostRequestDto.setOriginalId(null);
            adminPostRequestDto.setStatus(null);
            Post autosaveOrDraft = findById(adminPostRequestDto.getId());
            delete(autosaveOrDraft);
        }
        copyRequestToPost(post, adminPostRequestDto);
//        post.setModifiedAt(new Date());

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
    public AdminPostResponseDto getPostResponse(Long id) {
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
    private void copyRequestToPost(Post post, AdminPostRequestDto adminPostRequestDto) {
        String title = adminPostRequestDto.getTitle();
        String content = adminPostRequestDto.getContent();
        String excerpt = adminPostRequestDto.getExcerpt();
        String displayName = adminPostRequestDto.getDisplayName();
        String featuredImage = adminPostRequestDto.getFeaturedImage();
        Long categoryId = adminPostRequestDto.getCategoryId();
        //List<String> tagStringList = adminPostRequestDto.getTags();
//        LocalDateTime publishedAt = adminPostRequestDto.getPublishedAt();
        String status = adminPostRequestDto.getStatus();
        Long originalId = adminPostRequestDto.getOriginalId();

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
//
//        if (publishedAt != null) {
//            post.setPublishedAt(publishedAt);
//        } else {
//            post.setPublishedAt(LocalDateTime.now());
//        }

        if (status != null) {
            post.setStatus(PostEnum.Status.valueOf(status));
        }

        if (originalId != null) {
            Post originalPost = postRepository.findById(originalId);
            post.setOriginal(originalPost);
        }

        post.clearTags();
//        if (tagStringList != null) {
//            for (String tagString : tagStringList) {
//                Tag tag = tagService.findByName(tagString);
//                if (tag == null) {
//                    tag = new Tag();
//                    tag.setName(tagString);
//                    tagService.save(tag);
//                }
//                post.addTag(tag);
//            }
//        }
    }

    // it does id copy
    private AdminPostResponseDto copyPostToResponse(Post post) {
        AdminPostResponseDto adminPostResponseDto = new AdminPostResponseDto();
        adminPostResponseDto.setId(post.getId());
        adminPostResponseDto.setPublishedAt(post.getPublishedAt());
        adminPostResponseDto.setModifiedAt(post.getModifiedAt());
        adminPostResponseDto.setCreatedAt(post.getCreatedAt());
        adminPostResponseDto.setAuthorId(post.getAuthor().getId());
        adminPostResponseDto.setEditorId(post.getEditor().getId());
        adminPostResponseDto.setCategoryId(post.getCategory().getId());
        adminPostResponseDto.setTitle(post.getTitle());
        adminPostResponseDto.setContent(post.getContent());
        adminPostResponseDto.setExcerpt(post.getExcerpt());
        adminPostResponseDto.setDisplayName(post.getDisplayName());
        adminPostResponseDto.setFeaturedImage(post.getFeaturedImage());
        adminPostResponseDto.setPostType(post.getPostType().toString());
        adminPostResponseDto.setStatus(post.getStatus().toString());
        if (post.getOriginal() != null) {
            adminPostResponseDto.setOriginalId(post.getOriginal().getId());
        }

        Set<Tag> tags = post.getTags();
        if (tags != null) {
            List<String> tagStringList = new ArrayList<>();
            for (Tag tag : tags) {
                tagStringList.add(tag.getName());
            }
//            adminPostResponseDto.setTags(tagStringList);
        }
        return adminPostResponseDto;
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
