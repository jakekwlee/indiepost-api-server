package com.indiepost.service;

import com.indiepost.enums.UserEnum;
import com.indiepost.model.Category;
import com.indiepost.model.Post;
import com.indiepost.model.Tag;
import com.indiepost.model.User;
import dto.CategoryDto;
import dto.TagDto;
import dto.UserDto;
import dto.response.AdminDataTableItem;
import dto.response.AdminInitResponseDto;
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
    public List<AdminDataTableItem> getAdminPostListItemDtos(int page, int maxResults, boolean isDesc) {
        List<Post> posts = postExcerptService.findAll(page, maxResults, isDesc);
        return getAdminDataTableDtos(posts);
    }

    @Override
    public List<AdminDataTableItem> getLastUpdated(Date date) {
        List<Post> posts = postExcerptService.findLastUpdated(date);
        return getAdminDataTableDtos(posts);
    }

    @Override
    public List<TagDto> getAllTagDtos() {
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
    public List<UserDto> getAllAuthorsUserDtos() {
        List<User> authors = userService.findByRolesEnum(UserEnum.Roles.Author, 1, 1000000, true);
        return getUserDtos(authors);
    }

    @Override
    public List<UserDto> getAllUserDtos(int page, int maxResults, boolean isDesc) {
        List<User> userList = userService.findAllUsers(page, maxResults, isDesc);
        return getUserDtos(userList);
    }

    @Override
    public UserDto getCurrentUserDto() {
        User currentUser = userService.getCurrentUser();
        return getUserDtoFromUser(currentUser);
    }

    @Override
    public List<CategoryDto> getAlllCategoryDtos() {
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
        adminInitResponseDto.setCurrentUser(getUserDtoFromUser(currentUser));
        adminInitResponseDto.setAuthors(getAllAuthorsUserDtos());
        adminInitResponseDto.setCategories(getAlllCategoryDtos());
        adminInitResponseDto.setTags(getAllTagDtos());
        adminInitResponseDto.setAuthorNames(postExcerptService.findAllAuthorNames());
        return adminInitResponseDto;
    }

    private List<UserDto> getUserDtos(List<User> userList) {
        List<UserDto> simplifiedUserList = new ArrayList<>();
        for (User user : userList) {
            simplifiedUserList.add(getUserDtoFromUser(user));
        }
        return simplifiedUserList;
    }

    private List<AdminDataTableItem> getAdminDataTableDtos(List<Post> posts) {
        List<AdminDataTableItem> adminDataTableItems = new ArrayList<>();
        for (Post post : posts) {
            User author = post.getAuthor();
            AdminDataTableItem adminDataTableItem = new AdminDataTableItem();
            UserDto userDto = new UserDto();

            userDto.setId(author.getId());
            userDto.setDisplayName(author.getDisplayName());
            userDto.setEmail(author.getEmail());
            userDto.setUsername(author.getUsername());

            adminDataTableItem.setId(post.getId());
            adminDataTableItem.setAuthorDisplayName(post.getAuthor().getDisplayName());
            adminDataTableItem.setCategoryName(post.getCategory().getName());
            adminDataTableItem.setTags(getTagStringArray(post.getTags()));
            adminDataTableItem.setStatus(post.getStatus().toString());

            adminDataTableItem.setTitle(post.getTitle());
            adminDataTableItem.setDisplayName(post.getDisplayName());
            adminDataTableItem.setCreatedAt(getDateString(post.getCreatedAt()));
            adminDataTableItem.setPublishedAt(getDateString(post.getPublishedAt()));
            adminDataTableItem.setModifiedAt(getDateString(post.getModifiedAt()));
            adminDataTableItem.setCreatedAt(getDateString(post.getCreatedAt()));
            adminDataTableItem.setDisplayName(post.getDisplayName());
            adminDataTableItem.setLikedCount(post.getLikesCount());

            adminDataTableItems.add(adminDataTableItem);
        }
        return adminDataTableItems;
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

    private UserDto getUserDtoFromUser(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setDisplayName(user.getDisplayName());
        userDto.setEmail(user.getEmail());
        userDto.setBirthday(user.getBirthday());
        userDto.setGender(user.getGender());
        userDto.setJoinedAt(user.getJoinedAt());
        userDto.setPicture(user.getPicture());
        userDto.setProfile(user.getProfile());
        return userDto;
    }
}
