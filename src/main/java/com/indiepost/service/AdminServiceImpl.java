package com.indiepost.service;

import com.indiepost.enums.UserEnum;
import com.indiepost.model.Category;
import com.indiepost.model.Post;
import com.indiepost.model.Tag;
import com.indiepost.model.User;
import com.indiepost.model.response.*;
import org.apache.commons.lang3.time.FastDateFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by jake on 10/8/16.
 */
@Service
@Transactional
public class AdminServiceImpl implements AdminService {

    private PostExcerptService postExcerptService;

    private CategoryService categoryService;

    private UserService userService;

    private TagService tagService;

    @Autowired
    public AdminServiceImpl(PostExcerptService postExcerptService, CategoryService categoryService, TagService tagService, UserService userService) {
        this.postExcerptService = postExcerptService;
        this.categoryService = categoryService;
        this.tagService = tagService;
        this.userService = userService;
    }

    @Override
    public List<SimplifiedPost> getAllSimplifiedPosts(int page, int maxResults, boolean isDesc) {
        List<Post> posts = postExcerptService.findAll(page, maxResults, isDesc);
        return getSimplifiedPostList(posts);
    }

    @Override
    public List<SimplifiedPost> getLastUpdated(Date date) {
        List<Post> posts = postExcerptService.findLastUpdated(date);
        return getSimplifiedPostList(posts);
    }

    @Override
    public List<SimplifiedTag> getAllSimplifiedTags() {
        List<Tag> tags = tagService.findAll();
        List<SimplifiedTag> simplifiedTagList = new ArrayList<>();
        for (Tag tag : tags) {
            SimplifiedTag simplifiedTag = new SimplifiedTag();
            simplifiedTag.setId(tag.getId());
            simplifiedTag.setName(tag.getName());
            simplifiedTagList.add(simplifiedTag);
        }
        return simplifiedTagList;
    }

    @Override
    public List<AdminUserResponse> getAllSimplifiedAuthors() {
        List<User> authors = userService.findByRolesEnum(UserEnum.Roles.Author, 1, 1000000, true);
        return getSimplifiedUserList(authors);
    }

    @Override
    public List<AdminUserResponse> getAllUsersMeta(int page, int maxResults, boolean isDesc) {
        List<User> userList = userService.findAllUsers(page, maxResults, isDesc);
        return getSimplifiedUserList(userList);
    }

    @Override
    public AdminUserResponse getCurrentUser() {
        User currentUser = userService.getCurrentUser();
        return getSimplifiedUserFromUser(currentUser);
    }

    @Override
    public List<SimplifiedCategory> getAllSimplifiedCategories() {
        List<Category> categories = categoryService.findAll();
        List<SimplifiedCategory> simplifiedCategoryList = new ArrayList<>();
        for (Category category : categories) {
            SimplifiedCategory simplifiedCategory = new SimplifiedCategory();
            simplifiedCategory.setId(category.getId());
            simplifiedCategory.setSlug(category.getSlug());
            simplifiedCategory.setName(category.getName());
            simplifiedCategoryList.add(simplifiedCategory);
        }
        return simplifiedCategoryList;
    }

    @Override
    public AdminInitialResponse getInitialResponse() {
        User currentUser = userService.getCurrentUser();
        AdminInitialResponse adminInitialResponse = new AdminInitialResponse();
        adminInitialResponse.setCurrentUser(getSimplifiedUserFromUser(currentUser));
        adminInitialResponse.setAuthors(getAllSimplifiedAuthors());
        adminInitialResponse.setCategories(getAllSimplifiedCategories());
        adminInitialResponse.setTags(getAllSimplifiedTags());
        adminInitialResponse.setAuthorNames(postExcerptService.findAllAuthorNames());
        return adminInitialResponse;
    }

    private List<AdminUserResponse> getSimplifiedUserList(List<User> userList) {
        List<AdminUserResponse> simplifiedUserList = new ArrayList<>();
        for (User user : userList) {
            simplifiedUserList.add(getSimplifiedUserFromUser(user));
        }
        return simplifiedUserList;
    }

    private List<SimplifiedPost> getSimplifiedPostList(List<Post> posts) {
        List<SimplifiedPost> postMetaList = new ArrayList<>();
        for (Post post : posts) {
            User author = post.getAuthor();
            SimplifiedPost postMeta = new SimplifiedPost();
            AdminUserResponse simplifiedUser = new AdminUserResponse();

            simplifiedUser.setId(author.getId());
            simplifiedUser.setDisplayName(author.getDisplayName());
            simplifiedUser.setEmail(author.getEmail());
            simplifiedUser.setUsername(author.getUsername());

            postMeta.setId(post.getId());
            postMeta.setAuthorDisplayName(post.getAuthor().getDisplayName());
            postMeta.setCategoryName(post.getCategory().getName());
            postMeta.setTags(getTagStringArray(post.getTags()));
            postMeta.setStatus(post.getStatus().toString());

            postMeta.setTitle(post.getTitle());
            postMeta.setDisplayName(post.getDisplayName());
            postMeta.setCreatedAt(getDateString(post.getCreatedAt()));
            postMeta.setPublishedAt(getDateString(post.getPublishedAt()));
            postMeta.setModifiedAt(getDateString(post.getModifiedAt()));
            postMeta.setCreatedAt(getDateString(post.getCreatedAt()));
            postMeta.setDisplayName(post.getDisplayName());
            postMeta.setLikedCount(post.getLikesCount());

            postMetaList.add(postMeta);
        }
        return postMetaList;
    }

    private String getDateString(Date date) {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm a", Locale.KOREA);
        return fastDateFormat.format(date);
    }

    private Set<String> getTagStringArray(Set<Tag> tags) {
        Set<String> tagStringArray = new HashSet<>();
        for (Tag tag : tags) {
            tagStringArray.add(tag.getName());
        }
        return tagStringArray;
    }

    private AdminUserResponse getSimplifiedUserFromUser(User user) {
        AdminUserResponse simplifiedUser = new AdminUserResponse();
        simplifiedUser.setId(user.getId());
        simplifiedUser.setUsername(user.getUsername());
        simplifiedUser.setDisplayName(user.getDisplayName());
        simplifiedUser.setEmail(user.getEmail());
        simplifiedUser.setBirthday(user.getBirthday());
        simplifiedUser.setGender(user.getGender());
        simplifiedUser.setJoinedAt(user.getJoinedAt());
        simplifiedUser.setPicture(user.getPicture());
        simplifiedUser.setProfile(user.getProfile());
        return simplifiedUser;
    }
}
