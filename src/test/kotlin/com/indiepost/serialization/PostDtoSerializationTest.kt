package com.indiepost.serialization

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.indiepost.NewIndiepostApplication
import com.indiepost.service.PostService
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.context.web.WebAppConfiguration
import javax.inject.Inject

/**
 * Created by jake on 17. 1. 18.
 */
@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = arrayOf(NewIndiepostApplication::class))
@WebAppConfiguration
class PostDtoSerializationTest {

    @Inject
    private lateinit var postService: PostService

    /**
     * Usage: Homepage ContentView
     *
     * @throws JsonProcessingException
     */
    @Test
    fun postDtoShouldSerializeCorrectly() {
        val postDto = postService.findOne(347L)
        val objectMapper = ObjectMapper()
        objectMapper.registerModule(Hibernate5Module())
        println("*** Start serialize PostDto ***")
        val result = objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true)
                .writeValueAsString(postDto)
        println(result)
    }

    /**
     * Usage: Homepage PostList
     *
     * @throws JsonProcessingException
     */
    @Test
    fun PagedPostSummaryDtoListShouldSerializeCorrectly() {
        val pagedResult = postService.find(PageRequest.of(3, 10))
        val objectMapper = Jackson2ObjectMapperBuilder.json().featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS).modules(JavaTimeModule()).build<ObjectMapper>()
        objectMapper.registerModule(Hibernate5Module())
        println("\n\n*** Start serialize List<PostSummaryDto> ***\n\n")
        println("Result Length: " + pagedResult.content.size)
        val result = objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true)
                .writeValueAsString(pagedResult)
        println(result)
    }

    /**
     * Usage: RelatedPosts API
     *
     * @throws JsonProcessingException
     */
    fun getRelatedPostsWorksCorrectly() {
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
