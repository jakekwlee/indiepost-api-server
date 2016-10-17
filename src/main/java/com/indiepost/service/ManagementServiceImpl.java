package com.indiepost.service;

import com.indiepost.enums.PostEnum;
import com.indiepost.enums.UserEnum;
import com.indiepost.model.Category;
import com.indiepost.model.Post;
import com.indiepost.model.Tag;
import com.indiepost.model.User;
import com.indiepost.viewModel.cms.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jake on 10/8/16.
 */
@Service
@Transactional
public class ManagementServiceImpl implements ManagementService {

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private TagService tagService;

    @Override
    public List<PostMeta> getPublicPosts(int page, int maxResults, boolean descending) {
        List<Post> posts;

        if (descending) {
            posts = postService.findAll(page, maxResults);
        } else {
            posts = postService.findAllOrderByAsc(page, maxResults);
        }

        return getPostMetaList(posts);
    }

    @Override
    public List<PostMeta> getBookedPosts(int page, int maxResults, boolean descending) {
        List<Post> posts;

        if (descending) {
            posts = postService.findAll(PostEnum.Status.BOOKED, null, null, page, maxResults);
        } else {
            posts = postService.findAllOrderByAsc(PostEnum.Status.BOOKED, null, null, page, maxResults);
        }

        return getPostMetaList(posts);
    }

    @Override
    public List<PostMeta> getQueuedPosts(int page, int maxResults, boolean descending) {
        List<Post> posts;

        if (descending) {
            posts = postService.findAll(PostEnum.Status.QUEUED, null, null, page, maxResults);
        } else {
            posts = postService.findAllOrderByAsc(PostEnum.Status.QUEUED, null, null, page, maxResults);
        }

        return getPostMetaList(posts);
    }

    @Override
    public List<PostMeta> getDraftPosts(User currentUser, int page, int maxResults, boolean descending) {
        List<Post> posts;

        if (descending) {
            posts = postService.findAll(PostEnum.Status.DRAFT, currentUser, null, page, maxResults);
        } else {
            posts = postService.findAllOrderByAsc(PostEnum.Status.DRAFT, currentUser, null, page, maxResults);
        }

        return getPostMetaList(posts);
    }

    @Override
    public List<PostMeta> getDeletedPosts(User currentUser, int page, int maxResults, boolean descending) {
        List<Post> posts;

        if (descending) {
            posts = postService.findAll(PostEnum.Status.DELETED, currentUser, null, page, maxResults);
        } else {
            posts = postService.findAllOrderByAsc(PostEnum.Status.DELETED, currentUser, null, page, maxResults);
        }

        return getPostMetaList(posts);
    }

    @Override
    public List<TagMeta> getAllTags() {
        List<Tag> tags = tagService.findAll();
        List<TagMeta> tagMetaList = new ArrayList<>();
        for (Tag tag : tags) {
            TagMeta tagMeta = new TagMeta();
            tagMeta.setId(tag.getId());
            tagMeta.setName(tag.getName());
            tagMetaList.add(tagMeta);
        }
        return tagMetaList;
    }

    @Override
    public List<UserMeta> getAllAuthors() {
        List<UserMeta> userMetaList = new ArrayList<>();
        List<User> authors = userService.findByRolesEnum(UserEnum.Roles.Author);
        for (User author : authors) {
            UserMeta userMeta = getUserMetaFromUser(author);
            userMetaList.add(userMeta);
        }
        return userMetaList;
    }

    public UserMeta getUserMetaFromUser(User user) {
        UserMeta userMeta = new UserMeta();
        userMeta.setId(user.getId());
        userMeta.setUsername(user.getUsername());
        userMeta.setDisplayName(user.getDisplayName());
        userMeta.setEmail(user.getEmail());
        return userMeta;
    }

    @Override
    public List<CategoryMeta> getAllCategories() {
        List<Category> categories = categoryService.findAll();
        List<CategoryMeta> categoryMetaList = new ArrayList<>();
        for (Category category : categories) {
            CategoryMeta categoryMeta = new CategoryMeta();
            categoryMeta.setId(category.getId());
            categoryMeta.setSlug(category.getSlug());
            categoryMeta.setName(category.getName());
            categoryMetaList.add(categoryMeta);
        }
        return categoryMetaList;
    }

    @Override
    public PaginationMeta getPagination() {
        PaginationDetailMeta posts = new PaginationDetailMeta();
        PaginationDetailMeta queued = new PaginationDetailMeta();
        PaginationDetailMeta booked = new PaginationDetailMeta();
        PaginationDetailMeta drafts = new PaginationDetailMeta();
        PaginationDetailMeta trash = new PaginationDetailMeta();

        posts.setCount(postService.countPublished());
        queued.setCount(postService.countQueued());
        booked.setCount(postService.countBooked());
        drafts.setCount(postService.countDraft());
        trash.setCount(postService.countDeleted());
        PaginationMeta paginationMeta = new PaginationMeta();
        paginationMeta.setPublished(posts);
        paginationMeta.setQueued(queued);
        paginationMeta.setBooked(booked);
        paginationMeta.setDraft(drafts);
        paginationMeta.setDeleted(trash);

        return paginationMeta;
    }

    @Override
    public AdminInitialResponse getInitialState() {
        User currentUser = userService.getCurrentUser();
        AdminInitialResponse response = new AdminInitialResponse();
        response.setCurrentUser(getUserMetaFromUser(currentUser));
        List<List<PostMeta>> publicPosts = new ArrayList<>();
        publicPosts.add(getPublicPosts(0, 50, true));
        response.setPublished(publicPosts);
//        response.setDraft((getDraftPosts(currentUser, 1, 50, true)));
//        response.setDeleted((getDeletedPosts(currentUser, 1, 50, true)));
//        response.setBooked((getBookedPosts(1, 50, true)));
        response.setAuthors(getAllAuthors());
        response.setCategories(getAllCategories());
        response.setPagination(getPagination());
        response.setTags(getAllTags());
        return response;
    }

    private List<PostMeta> getPostMetaList(List<Post> posts) {
        List<PostMeta> postMetaList = new ArrayList<>();
        for (Post post : posts) {
            User author = post.getAuthor();
            Category category = post.getCategory();
            PostMeta postMeta = new PostMeta();
            UserMeta userMeta = new UserMeta();
            CategoryMeta categoryMeta = new CategoryMeta();

            userMeta.setId(author.getId());
            userMeta.setDisplayName(author.getDisplayName());
            userMeta.setEmail(author.getEmail());
            userMeta.setUsername(author.getUsername());

            categoryMeta.setId(category.getId());
            categoryMeta.setName(category.getName());
            categoryMeta.setSlug(category.getSlug());

            postMeta.setId(post.getId());
            postMeta.setAuthor(userMeta);
            postMeta.setAuthorName(post.getAuthorName());
            postMeta.setCreatedAt(post.getCreatedAt());
            postMeta.setPublishedAt(post.getPublishedAt());
            postMeta.setCreatedAt(post.getCreatedAt());
            postMeta.setTitle(post.getTitle());
            postMeta.setAuthorName(post.getAuthorName());
            postMeta.setLikedCount(post.getLikesCount());
            postMeta.setCategory(categoryMeta);
            postMeta.setStatus(post.getStatus().toString());
            postMetaList.add(postMeta);
        }
        return postMetaList;
    }
}
