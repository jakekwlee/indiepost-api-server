package com.indiepost.service;

import com.indiepost.enums.UserEnum;
import com.indiepost.model.Category;
import com.indiepost.model.Post;
import com.indiepost.model.Tag;
import com.indiepost.model.User;
import dto.CategoryDto;
import dto.TagDto;
import dto.UserDto;
import dto.response.*;
import org.apache.commons.lang3.time.FastDateFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
    public List<AdminPostListItemDto> getAllSimplifiedPosts(int page, int maxResults, boolean isDesc) {
        List<Post> posts = postExcerptService.findAll(page, maxResults, isDesc);
        return getSimplifiedPostList(posts);
    }

    @Override
    public List<AdminPostListItemDto> getLastUpdated(Date date) {
        List<Post> posts = postExcerptService.findLastUpdated(date);
        return getSimplifiedPostList(posts);
    }

    @Override
    public List<TagDto> getAllSimplifiedTags() {
        List<Tag> tags = tagService.findAll();
        List<TagDto> tagDtoList = new ArrayList<>();
        for (Tag tag : tags) {
            TagDto tagDto = new TagDto();
            tagDto.setId(tag.getId());
            tagDto.setName(tag.getName());
            tagDtoList.add(tagDto);
        }
        return tagDtoList;
    }

    @Override
    public List<UserDto> getAllSimplifiedAuthors() {
        List<User> authors = userService.findByRolesEnum(UserEnum.Roles.Author, 1, 1000000, true);
        return getSimplifiedUserList(authors);
    }

    @Override
    public List<UserDto> getAllUsersMeta(int page, int maxResults, boolean isDesc) {
        List<User> userList = userService.findAllUsers(page, maxResults, isDesc);
        return getSimplifiedUserList(userList);
    }

    @Override
    public UserDto getCurrentUser() {
        User currentUser = userService.getCurrentUser();
        return getSimplifiedUserFromUser(currentUser);
    }

    @Override
    public List<CategoryDto> getAllSimplifiedCategories() {
        List<Category> categories = categoryService.findAll();
        List<CategoryDto> categoryDtoList = new ArrayList<>();
        for (Category category : categories) {
            CategoryDto categoryDto = new CategoryDto();
            categoryDto.setId(category.getId());
            categoryDto.setSlug(category.getSlug());
            categoryDto.setName(category.getName());
            categoryDtoList.add(categoryDto);
        }
        return categoryDtoList;
    }

    @Override
    public AdminInitResponseDto getInitialResponse() {
        User currentUser = userService.getCurrentUser();
        AdminInitResponseDto adminInitResponseDto = new AdminInitResponseDto();
        adminInitResponseDto.setCurrentUser(getSimplifiedUserFromUser(currentUser));
        adminInitResponseDto.setAuthors(getAllSimplifiedAuthors());
        adminInitResponseDto.setCategories(getAllSimplifiedCategories());
        adminInitResponseDto.setTags(getAllSimplifiedTags());
        adminInitResponseDto.setAuthorNames(postExcerptService.findAllAuthorNames());
        return adminInitResponseDto;
    }

    private List<UserDto> getSimplifiedUserList(List<User> userList) {
        List<UserDto> simplifiedUserList = new ArrayList<>();
        for (User user : userList) {
            simplifiedUserList.add(getSimplifiedUserFromUser(user));
        }
        return simplifiedUserList;
    }

    private List<AdminPostListItemDto> getSimplifiedPostList(List<Post> posts) {
        List<AdminPostListItemDto> postMetaList = new ArrayList<>();
        for (Post post : posts) {
            User author = post.getAuthor();
            AdminPostListItemDto adminPostListItemDto = new AdminPostListItemDto();
            UserDto simplifiedUser = new UserDto();

            simplifiedUser.setId(author.getId());
            simplifiedUser.setDisplayName(author.getDisplayName());
            simplifiedUser.setEmail(author.getEmail());
            simplifiedUser.setUsername(author.getUsername());

            adminPostListItemDto.setId(post.getId());
            adminPostListItemDto.setAuthorDisplayName(post.getAuthor().getDisplayName());
            adminPostListItemDto.setCategoryName(post.getCategory().getName());
            adminPostListItemDto.setTags(getTagStringArray(post.getTags()));
            adminPostListItemDto.setStatus(post.getStatus().toString());

            adminPostListItemDto.setTitle(post.getTitle());
            adminPostListItemDto.setDisplayName(post.getDisplayName());
            adminPostListItemDto.setCreatedAt(getDateString(post.getCreatedAt()));
            adminPostListItemDto.setPublishedAt(getDateString(post.getPublishedAt()));
            adminPostListItemDto.setModifiedAt(getDateString(post.getModifiedAt()));
            adminPostListItemDto.setCreatedAt(getDateString(post.getCreatedAt()));
            adminPostListItemDto.setDisplayName(post.getDisplayName());
            adminPostListItemDto.setLikedCount(post.getLikesCount());

            postMetaList.add(adminPostListItemDto);
        }
        return postMetaList;
    }

    private String getDateString(LocalDateTime date) {
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

    private UserDto getSimplifiedUserFromUser(User user) {
        UserDto simplifiedUser = new UserDto();
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
