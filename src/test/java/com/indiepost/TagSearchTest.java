package com.indiepost;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.indiepost.dto.PostSummaryDto;
import com.indiepost.service.PostService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

/**
 * Created by jake on 17. 3. 16.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NewIndiepostApplication.class)
@WebAppConfiguration
public class TagSearchTest {
    @Autowired
    private PostService postService;

    @Test
    public void tagSearchShouldReturnPostsCorrectly() throws JsonProcessingException {
        List<PostSummaryDto> dtoList = postService.findByTagName("독립영화");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new Hibernate5Module());
        System.out.println("\n\n*** Start serialize PostsByTagName ***\n\n");
        String result = objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true)
                .writeValueAsString(dtoList);
        System.out.println("Length of results: " + (dtoList.size()) + " posts");
        System.out.println("Size of results: " + (result.getBytes().length / 1024.0) + " kb");
        System.out.println(result);
    }
}
