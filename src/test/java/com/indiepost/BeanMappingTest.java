package com.indiepost;

import com.indiepost.mapper.PostMapper;
import com.indiepost.model.Post;
import com.indiepost.model.Tag;
import com.indiepost.model.User;
import com.indiepost.service.PostService;
import com.indiepost.service.TagService;
import com.indiepost.service.UserService;
import dto.TagDto;
import dto.request.AdminPostRequestDto;
import dto.response.AdminPostResponseDto;
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
import java.util.Set;

import static org.junit.Assert.assertEquals;

/**
 * Created by jake on 16. 12. 14.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NewIndiepostApplication.class)
@WebAppConfiguration
public class BeanMappingTest {

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private TagService tagService;

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    private Post post;

    private AdminPostResponseDto postResponseDto;

    private AdminPostRequestDto postRequestDto;


    @Test
    @Transactional
    public void testMapPostToPostResponse() {
        AdminPostResponseDto adminPostResponseDto =
                this.postMapper.postToAdminPostResponseDto(this.post);
        assertEquals(this.postResponseDto.getId(), adminPostResponseDto.getId());
        assertEquals(this.postResponseDto.getAuthorId(), adminPostResponseDto.getAuthorId());
        assertEquals(this.postResponseDto.getEditorId(), adminPostResponseDto.getEditorId());
        assertEquals(this.postResponseDto.getTitle(), adminPostResponseDto.getTitle());
        assertEquals(this.postResponseDto.getContent(), adminPostResponseDto.getContent());
        assertEquals(this.postResponseDto.getExcerpt(), adminPostResponseDto.getExcerpt());
        assertEquals(this.postResponseDto.getDisplayName(), adminPostResponseDto.getDisplayName());
        assertEquals(this.postResponseDto.getStatus(), adminPostResponseDto.getStatus());
        assertEquals(this.postResponseDto.getFeaturedImage(), adminPostResponseDto.getFeaturedImage());
        assertEquals(this.postResponseDto.getPostType(), adminPostResponseDto.getPostType());
        assertEquals(this.postResponseDto.getCategoryId(), adminPostResponseDto.getCategoryId());
        assertEquals(this.postResponseDto.getModifiedAt(), adminPostResponseDto.getModifiedAt());
        assertEquals(this.postResponseDto.getCreatedAt(), adminPostResponseDto.getCreatedAt());
        assertEquals(this.postResponseDto.getPublishedAt(), adminPostResponseDto.getPublishedAt());
        assertEquals(this.postResponseDto.getOriginalId(), adminPostResponseDto.getOriginalId());
        assertEquals(this.postResponseDto.getCommentsCount(), adminPostResponseDto.getCommentsCount());
        assertEquals(this.postResponseDto.getLikesCount(), adminPostResponseDto.getLikesCount());

        List<TagDto> tagDtosExpected = this.postResponseDto.getTags();
        List<TagDto> tagDtosActual = adminPostResponseDto.getTags();
        assertEquals(tagDtosExpected.size(), tagDtosActual.size());
    }

    @Test
    @Transactional
    public void testMapAdminPostRequestToPost() {
        Post post = this.postMapper.adminPostRequestDtoToPost(this.postRequestDto);
        assertEquals(this.post.getId(), post.getId());
        assertEquals(this.post.getTitle(), post.getTitle());
        assertEquals(this.post.getExcerpt(), post.getExcerpt());
        assertEquals(this.post.getContent(), post.getContent());
        assertEquals(this.post.getDisplayName(), post.getDisplayName());
        assertEquals(this.post.getFeaturedImage(), post.getFeaturedImage());
        assertEquals(this.post.getPublishedAt(), post.getPublishedAt());
        assertEquals(this.post.getCategory().getId(), post.getCategory().getId());
        assertEquals(this.post.getTags().size(), post.getTags().size());
    }

    @Before
    @Transactional
    public void initPost() {
        Tag tag1 = tagService.findById(1L);
        User user = userService.findById(1L);
        post = postService.findById(44L);
        postRequestDto = new AdminPostRequestDto();
        postResponseDto = new AdminPostResponseDto();


        List<TagDto> tagDtoList = new ArrayList<>();
        Set<Tag> tagSet = post.getTags();
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
        postRequestDto.setFeaturedImage(post.getFeaturedImage());
        postRequestDto.setCategoryId(post.getCategory().getId());
        postRequestDto.setStatus(post.getStatus().toString());
        postRequestDto.setPublishedAt(post.getPublishedAt());
        postRequestDto.setTags(tagDtoList);
        if (post.getOriginal() != null) {
            postRequestDto.setOriginalId(post.getOriginal().getId());
            postRequestDto.setOriginalId(post.getOriginal().getId());
        }

        postResponseDto.setId(post.getId());
        postResponseDto.setTitle(post.getTitle());
        postResponseDto.setContent(post.getContent());
        postResponseDto.setExcerpt(post.getExcerpt());
        postResponseDto.setDisplayName(post.getDisplayName());
        postResponseDto.setFeaturedImage(post.getFeaturedImage());
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
        postResponseDto.setPostType(post.getPostType().toString());

    }

}
