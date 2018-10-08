package com.indiepost.serialization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.indiepost.NewIndiepostApplication;
import com.indiepost.dto.post.PostDto;
import com.indiepost.dto.post.PostSummaryDto;
import com.indiepost.service.PostService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.inject.Inject;

/**
 * Created by jake on 17. 1. 18.
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = NewIndiepostApplication.class)
@WebAppConfiguration
public class PostDtoSerializationTest {

    @Inject
    private PostService postService;

    /**
     * Usage: Homepage ContentView
     *
     * @throws JsonProcessingException
     */
    @Test
    public void postDtoShouldSerializeCorrectly() throws JsonProcessingException {
        PostDto postDto = postService.findOne(347L);
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
    public void PagedPostSummaryDtoListShouldSerializeCorrectly() throws JsonProcessingException {
        Page<PostSummaryDto> pagedResult = postService.find(PageRequest.of(3, 10));
        ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json().featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS).modules(new JavaTimeModule()).build();
        objectMapper.registerModule(new Hibernate5Module());
        System.out.println("\n\n*** Start serialize List<PostSummaryDto> ***\n\n");
        System.out.println("Result Length: " + pagedResult.getContent().size());
        String result = objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true)
                .writeValueAsString(pagedResult);
        System.out.println(result);
    }

    /**
     * Usage: RelatedPosts API
     *
     * @throws JsonProcessingException
     */
    public void getRelatedPostsWorksCorrectly() throws JsonProcessingException {
//        List<Long> ids = new ArrayList<>();
//        ids.add(516L);
//        ids.add(218L);
//        List<RelatedPostResponseDto> postList = this.postService.getRelatedPosts(ids, true, true);
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.registerModule(new Hibernate5Module());
//        System.out.println("\n\n*** Start serialize List<RelatedPostResponseDto> ***\n\n");
//        System.out.println("Result Length: " + postList.size());
//        String result = objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true)
//                .writeValueAsString(postList);
//        System.out.println(result);
    }
}
