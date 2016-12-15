package com.indiepost.unit;

import com.indiepost.NewIndiepostApplication;
import com.indiepost.enums.PostEnum;
import com.indiepost.mapper.PostMapper;
import com.indiepost.mapper.PostMapperImpl;
import com.indiepost.model.Category;
import com.indiepost.model.Post;
import com.indiepost.model.Tag;
import com.indiepost.model.User;
import dto.TagDto;
import dto.request.AdminPostRequestDto;
import dto.response.AdminPostResponseDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;

/**
 * Created by jake on 16. 12. 14.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NewIndiepostApplication.class)
@WebAppConfiguration
public class BeanMappingTest {

    private PostMapper postMapper;

    private Post post;

    private AdminPostResponseDto postResponse;

    private AdminPostRequestDto postRequest;


    @Test
    public void testMapPostToPostResponse() {
        AdminPostResponseDto adminPostResponseDto;
        adminPostResponseDto = this.postMapper.postToAdminPostResponseDto(this.post);
        assertEquals("Map Post to PostResponse Properly", this.postResponse, adminPostResponseDto);
    }

    @Before
    public void initPost() {
        post = new Post();
        postRequest = new AdminPostRequestDto();
        postResponse = new AdminPostResponseDto();
        postMapper = new PostMapperImpl();

        User editor = new User();
        editor.setId(101L);
        User author = new User();
        author.setId(100L);
        Category category = new Category();
        category.setId(20L);
        category.setName("Test");
        category.setSlug("test");
        Post original = new Post();
        original.setId(100L);
        original.setStatus(PostEnum.Status.PUBLISH);
        Tag tag1 = new Tag();
        tag1.setId(7L);
        tag1.setName("test_tag_1");
        Tag tag2 = new Tag();
        tag2.setId(8L);
        tag2.setName("test_tag_2");
        Tag tag3 = new Tag();
        tag3.setId(9L);
        tag3.setName("test_tag_3");
        Set<Tag> tagSet = new HashSet<>();
        tagSet.add(tag1);
        tagSet.add(tag2);
        tagSet.add(tag3);
        List<TagDto> tagDtoList = new ArrayList<>();
        TagDto tagDto1 = new TagDto();
        tagDto1.setId(tag1.getId());
        tagDto1.setName(tag1.getName());
        TagDto tagDto2 = new TagDto();
        tagDto2.setId(tag2.getId());
        tagDto2.setName(tag2.getName());
        TagDto tagDto3 = new TagDto();
        tagDto3.setId(tag3.getId());
        tagDto3.setName(tag3.getName());
        tagDtoList.add(tagDto1);
        tagDtoList.add(tagDto2);
        tagDtoList.add(tagDto3);

        LocalDateTime aWeekAgo = LocalDateTime.now().minusDays(7);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime tomorow = LocalDateTime.now().plusDays(1);

        post.setId(200L);
        post.setTitle("Test post title");
        post.setExcerpt("Test post excerpt");
        post.setContent("Test post content body");
        post.setFeaturedImage("Test featured image");
        post.setDisplayName("Test display name");
        post.setCategory(category);
        post.setAuthor(author);
        post.setEditor(editor);
        post.setStatus(PostEnum.Status.AUTOSAVE);
        post.setCreatedAt(aWeekAgo);
        post.setModifiedAt(now);
        post.setPublishedAt(tomorow);
        post.setOriginal(original);
        post.setPostType(PostEnum.Type.POST);
        post.setTags(tagSet);

        postRequest.setId(post.getId());
        postRequest.setTitle(post.getTitle());
        postRequest.setContent(post.getContent());
        postRequest.setExcerpt(post.getExcerpt());
        postRequest.setDisplayName(post.getDisplayName());
        postRequest.setFeaturedImage(post.getFeaturedImage());
        postRequest.setCategoryId(post.getCategory().getId());
        postRequest.setOriginalId(post.getOriginal().getId());
        postRequest.setStatus(post.getStatus().toString());
        postRequest.setTags(tagDtoList);

        postResponse.setId(post.getId());
        postResponse.setTitle(post.getTitle());
        postResponse.setContent(post.getContent());
        postResponse.setExcerpt(post.getExcerpt());
        postResponse.setDisplayName(post.getDisplayName());
        postResponse.setFeaturedImage(post.getFeaturedImage());
        postResponse.setCategoryId(post.getCategory().getId());
        postResponse.setOriginalId(post.getOriginal().getId());
        postResponse.setStatus(post.getStatus().toString());
//        postResponse.setTags(postRequest.getTags());
        postResponse.setCreatedAt(post.getCreatedAt());
        postResponse.setModifiedAt(post.getModifiedAt());
        postResponse.setPublishedAt(post.getPublishedAt());
        postResponse.setAuthorId(author.getId());
        postResponse.setEditorId(editor.getId());
        postResponse.setCommentsCount(10);
        postResponse.setLikesCount(20);
        postResponse.setPostType(post.getPostType().toString());
    }

}
