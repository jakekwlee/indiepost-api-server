package com.indiepost.serialization

import com.fasterxml.jackson.core.JsonProcessingException
import com.indiepost.IndiepostBackendApplication
import com.indiepost.helper.printToJson
import com.indiepost.service.PostService
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.context.web.WebAppConfiguration
import javax.inject.Inject

/**
 * Created by jake on 17. 1. 18.
 */
@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [IndiepostBackendApplication::class])
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
        printToJson(postDto)
    }

    /**
     * Usage: Homepage PostList
     *
     * @throws JsonProcessingException
     */
    @Test
    fun pagedPostSummaryDtoListShouldSerializeCorrectly() {
        // TODO 151 ms? it takes too much.. Size of results: 75.603kb
        val pagedResult = postService.findPublicPosts(PageRequest.of(3, 30))
        printToJson(pagedResult)
        println("Result Length: " + pagedResult.content.size)
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
