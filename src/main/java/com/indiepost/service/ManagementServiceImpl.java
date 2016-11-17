package com.indiepost.service;

import com.indiepost.enums.UserEnum;
import com.indiepost.model.Category;
import com.indiepost.model.Post;
import com.indiepost.model.Tag;
import com.indiepost.model.User;
import com.indiepost.viewModel.admin.*;
import org.apache.commons.lang3.time.FastDateFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by jake on 10/8/16.
 */
@Service
@Transactional
public class ManagementServiceImpl implements ManagementService {

    private PostExcerptService postExcerptService;

    private CategoryService categoryService;

    private UserService userService;

    private TagService tagService;

    @Autowired
    public ManagementServiceImpl(PostExcerptService postExcerptService, CategoryService categoryService, TagService tagService, UserService userService) {
        this.postExcerptService = postExcerptService;
        this.categoryService = categoryService;
        this.tagService = tagService;
        this.userService = userService;
    }

    @Override
    public List<PostMeta> getAllPostsMeta(int page, int maxResults, boolean isDesc) {
        List<Post> posts = postExcerptService.findAll(page, maxResults, isDesc);
        return getPostMetaList(posts);
    }

    @Override
    public List<TagMeta> getAllTagsMeta() {
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
    public List<UserMeta> getAllAuthorsMeta() {
        List<User> authors = userService.findByRolesEnum(UserEnum.Roles.Author, 1, 1000000, true);
        return getUserMetaList(authors);
    }

    @Override
    public List<UserMeta> getAllUsersMeta(int page, int maxResults, boolean isDesc) {
        List<User> userList = userService.findAllUsers(page, maxResults, isDesc);
        return getUserMetaList(userList);
    }

    @Override
    public UserMeta getCurrentUser() {
        User currentUser = userService.getCurrentUser();
        return getUserMetaFromUser(currentUser);
    }

    @Override
    public List<CategoryMeta> getAllCategoriesMeta() {
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
    public MetaInformation getMetaInformation() {
        User currentUser = userService.getCurrentUser();
        MetaInformation metaInformation = new MetaInformation();
        metaInformation.setCurrentUser(getUserMetaFromUser(currentUser));
        metaInformation.setAuthors(getAllAuthorsMeta());
        metaInformation.setCategories(getAllCategoriesMeta());
        metaInformation.setTags(getAllTagsMeta());
        return metaInformation;
    }

    private List<UserMeta> getUserMetaList(List<User> userList) {
        List<UserMeta> userMetaList = new ArrayList<>();
        for (User user : userList) {
            userMetaList.add(getUserMetaFromUser(user));
        }
        return userMetaList;
    }

    private List<PostMeta> getPostMetaList(List<Post> posts) {
        List<PostMeta> postMetaList = new ArrayList<>();
        for (Post post : posts) {
            User author = post.getAuthor();
            PostMeta postMeta = new PostMeta();
            UserMeta userMeta = new UserMeta();

            userMeta.setId(author.getId());
            userMeta.setDisplayName(author.getDisplayName());
            userMeta.setEmail(author.getEmail());
            userMeta.setUsername(author.getUsername());

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

    private List<String> getTagStringArray(List<Tag> tags) {
        List<String> tagStringArray = new ArrayList<>();
        for (Tag tag : tags) {
            tagStringArray.add(tag.getName());
        }
        return tagStringArray;
    }

    private UserMeta getUserMetaFromUser(User user) {
        UserMeta userMeta = new UserMeta();
        userMeta.setId(user.getId());
        userMeta.setUsername(user.getUsername());
        userMeta.setDisplayName(user.getDisplayName());
        userMeta.setEmail(user.getEmail());
        userMeta.setBirthday(user.getBirthday());
        userMeta.setGender(user.getGender());
        userMeta.setJoinedAt(user.getJoinedAt());
        userMeta.setPicture(user.getPicture());
        userMeta.setProfile(user.getProfile());
        return userMeta;
    }
}
