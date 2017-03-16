package com.indiepost.serialization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.indiepost.NewIndiepostApplication;
import com.indiepost.dto.PostDto;
import com.indiepost.dto.PostSummaryDto;
import com.indiepost.dto.RelatedPostResponseDto;
import com.indiepost.service.PostService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jake on 17. 1. 18.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NewIndiepostApplication.class)
@WebAppConfiguration
public class PostDtoSerializationTest {

    @Autowired
    private PostService postService;

    /**
     * Usage: Homepage ContentView
     *
     * @throws JsonProcessingException
     */
    @Test
    public void postDtoShouldSerializeCorrectly() throws JsonProcessingException {
        PostDto postDto = postService.findById(347L);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new Hibernate5Module());
        System.out.println("*** Start serialize PostDto ***");
        String result = objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true)
                .writeValueAsString(postDto);
        System.out.println(result);
    }

    /**
     * Usage: Homepage PostList
     *
     * @throws JsonProcessingException
     */
    @Test
    public void postSummaryListShouldSerializeCorrectly() throws JsonProcessingException {
        List<PostSummaryDto> postList = postService.findAll(3, 10, true);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new Hibernate5Module());
        System.out.println("\n\n*** Start serialize List<PostSummaryDto> ***\n\n");
        System.out.println("Result Length: " + postList.size());
        String result = objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true)
                .writeValueAsString(postList);
        System.out.println(result);
    }

    /**
     * Usage: RelatedPosts API
     *
     * @throws JsonProcessingException
     */
    @Test
    public void getRelatedPostsWorksCorrectly() throws JsonProcessingException {
        List<Long> ids = new ArrayList<>();
        ids.add(516L);
        ids.add(218L);
        List<RelatedPostResponseDto> postList = this.postService.getRelatedPosts(ids, true, true);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new Hibernate5Module());
        System.out.println("\n\n*** Start serialize List<RelatedPostResponseDto> ***\n\n");
        System.out.println("Result Length: " + postList.size());
        String result = objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true)
                .writeValueAsString(postList);
        System.out.println(result);
    }
}
