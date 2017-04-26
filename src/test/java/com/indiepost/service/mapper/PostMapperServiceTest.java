package com.indiepost.service.mapper;

import com.indiepost.NewIndiepostApplication;
import com.indiepost.dto.TagDto;
import com.indiepost.dto.admin.AdminPostRequestDto;
import com.indiepost.dto.admin.AdminPostResponseDto;
import com.indiepost.model.Post;
import com.indiepost.model.Tag;
import com.indiepost.service.AdminPostService;
import com.indiepost.service.PostService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by jake on 16. 12. 14.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NewIndiepostApplication.class)
@WebAppConfiguration
public class PostMapperServiceTest {

    @Autowired
    private PostMapperService postMapperService;

    @Autowired
    private AdminPostService adminPostService;

    @Autowired
    private PostService postService;

    private Post post;

    private AdminPostResponseDto postResponseDto;

    private AdminPostRequestDto postRequestDto;

    @Before
    @Transactional
    public void init() {
        post = adminPostService.findById(44L);
        postRequestDto = new AdminPostRequestDto();
        postResponseDto = new AdminPostResponseDto();


        List<TagDto> tagDtoList = new ArrayList<>();
        List<Tag> tagSet = post.getTags();
        for (Tag tag : tagSet) {
            TagDto tagDto = new TagDto();
            tagDto.setId(tag.getId());
            tagDto.setName(tag.getName());
            tagDtoList.add(tagDto);
        }

        postRequestDto.setId(post.getId());
        postRequestDto.setTitle(post.getTitle());
        postRequestDto.setContent(post.getContent());
        postRequestDto.setExcerpt(post.getExcerpt());
        postRequestDto.setDisplayName(post.getDisplayName());
        postRequestDto.setTitleImageId(post.getTitleImageId());
        postRequestDto.setCategoryId(post.getCategory().getId());
        postRequestDto.setStatus(post.getStatus().toString());
        postRequestDto.setPublishedAt(post.getPublishedAt());
        postRequestDto.setTags(tagDtoList);
        if (post.getOriginal() != null) {
            postRequestDto.setOriginalId(post.getOriginal().getId());
        }

        postResponseDto.setId(post.getId());
        postResponseDto.setTitle(post.getTitle());
        postResponseDto.setContent(post.getContent());
        postResponseDto.setExcerpt(post.getExcerpt());
        postResponseDto.setDisplayName(post.getDisplayName());
        postResponseDto.setTitleImage(post.getTitleImage());
        postResponseDto.setCategoryId(post.getCategory().getId());

        postResponseDto.setStatus(post.getStatus().toString());
        postResponseDto.setTags(tagDtoList);
        postResponseDto.setCreatedAt(post.getCreatedAt());
        postResponseDto.setModifiedAt(post.getModifiedAt());
        postResponseDto.setPublishedAt(post.getPublishedAt());
        postResponseDto.setAuthorId(post.getAuthor().getId());
        postResponseDto.setEditorId(post.getEditor().getId());
        postResponseDto.setCommentsCount(post.getCommentsCount());
        postResponseDto.setLikesCount(post.getLikesCount());
    }

    @Test
    @Transactional
    public void testMapPostToPostResponse() {
        AdminPostResponseDto adminPostResponseDto =
                this.postMapperService.postToAdminPostResponseDto(this.post);
        assertEquals(this.postResponseDto.getAuthorId(), adminPostResponseDto.getAuthorId());
        assertEquals(this.postResponseDto.getEditorId(), adminPostResponseDto.getEditorId());
        assertEquals(this.postResponseDto.getTitle(), adminPostResponseDto.getTitle());
        assertEquals(this.postResponseDto.getContent(), adminPostResponseDto.getContent());
        assertEquals(this.postResponseDto.getExcerpt(), adminPostResponseDto.getExcerpt());
        assertEquals(this.postResponseDto.getDisplayName(), adminPostResponseDto.getDisplayName());
        assertEquals(this.postResponseDto.getStatus(), adminPostResponseDto.getStatus());
        assertEquals(this.postResponseDto.getTitleImage().getId(), adminPostResponseDto.getTitleImage().getId());
        assertEquals(this.postResponseDto.getPostType(), adminPostResponseDto.getPostType());
        assertEquals(this.postResponseDto.getCategoryId(), adminPostResponseDto.getCategoryId());
        assertEquals(this.postResponseDto.getModifiedAt(), adminPostResponseDto.getModifiedAt());
        assertEquals(this.postResponseDto.getCreatedAt(), adminPostResponseDto.getCreatedAt());
        assertEquals(this.postResponseDto.getPublishedAt(), adminPostResponseDto.getPublishedAt());
        assertEquals(this.postResponseDto.getOriginalId(), adminPostResponseDto.getOriginalId());
        assertEquals(this.postResponseDto.getCommentsCount(), adminPostResponseDto.getCommentsCount());
        assertEquals(this.postResponseDto.getLikesCount(), adminPostResponseDto.getLikesCount());

        List<TagDto> tagDtoListExpected = this.postResponseDto.getTags();
        List<TagDto> tagDtoListActual = adminPostResponseDto.getTags();
        assertEquals(tagDtoListExpected.size(), tagDtoListActual.size());
    }

    @Test
    @Transactional
    public void testMapAdminPostRequestToPost() {
        Post post = this.postMapperService.adminPostRequestDtoToPost(this.postRequestDto);
        assertEquals(this.post.getTitle(), post.getTitle());
        assertEquals(this.post.getExcerpt(), post.getExcerpt());
        assertEquals(this.post.getContent(), post.getContent());
        assertEquals(this.post.getDisplayName(), post.getDisplayName());
        assertEquals(this.post.getTitleImage().getId(), post.getTitleImage().getId());
        assertEquals(this.post.getPublishedAt(), post.getPublishedAt());
        assertEquals(this.post.getCategory().getId(), post.getCategory().getId());
        assertEquals(this.post.getTags().size(), post.getTags().size());
    }

    @Test
    @Transactional
    public void testGetPostResponse() {
        postService.findAll(0, 10, true);
    }

}
