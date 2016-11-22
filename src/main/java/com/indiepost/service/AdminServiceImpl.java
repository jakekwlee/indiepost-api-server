package com.indiepost.service;

import com.indiepost.enums.UserEnum;
import com.indiepost.model.Category;
import com.indiepost.model.Post;
import com.indiepost.model.Tag;
import com.indiepost.model.User;
import com.indiepost.responseModel.admin.*;
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
    public List<SimplifiedPost> getAllPostsMeta(int page, int maxResults, boolean isDesc) {
        List<Post> posts = postExcerptService.findAll(page, maxResults, isDesc);
        return getPostMetaList(posts);
    }

    @Override
    public List<SimplifiedTag> getAllTagsMeta() {
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
    public List<SimplifiedUser> getAllAuthorsMeta() {
        List<User> authors = userService.findByRolesEnum(UserEnum.Roles.Author, 1, 1000000, true);
        return getUserMetaList(authors);
    }

    @Override
    public List<SimplifiedUser> getAllUsersMeta(int page, int maxResults, boolean isDesc) {
        List<User> userList = userService.findAllUsers(page, maxResults, isDesc);
        return getUserMetaList(userList);
    }

    @Override
    public SimplifiedUser getCurrentUser() {
        User currentUser = userService.getCurrentUser();
        return getUserMetaFromUser(currentUser);
    }

    @Override
    public List<SimplifiedCategory> getAllCategoriesMeta() {
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
    public InitialResponse getMetaInformation() {
        User currentUser = userService.getCurrentUser();
        InitialResponse initialResponse = new InitialResponse();
        initialResponse.setCurrentUser(getUserMetaFromUser(currentUser));
        initialResponse.setAuthors(getAllAuthorsMeta());
        initialResponse.setCategories(getAllCategoriesMeta());
        initialResponse.setTags(getAllTagsMeta());
        initialResponse.setAuthorNames(postExcerptService.findAllAuthorNames());
        return initialResponse;
    }

    private List<SimplifiedUser> getUserMetaList(List<User> userList) {
        List<SimplifiedUser> simplifiedUserList = new ArrayList<>();
        for (User user : userList) {
            simplifiedUserList.add(getUserMetaFromUser(user));
        }
        return simplifiedUserList;
    }

    private List<SimplifiedPost> getPostMetaList(List<Post> posts) {
        List<SimplifiedPost> postMetaList = new ArrayList<>();
        for (Post post : posts) {
            User author = post.getAuthor();
            SimplifiedPost postMeta = new SimplifiedPost();
            SimplifiedUser simplifiedUser = new SimplifiedUser();

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

    private SimplifiedUser getUserMetaFromUser(User user) {
        SimplifiedUser simplifiedUser = new SimplifiedUser();
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
