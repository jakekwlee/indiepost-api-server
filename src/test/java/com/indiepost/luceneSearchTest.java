package com.indiepost;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.indiepost.dto.PostSummaryDto;
import com.indiepost.service.PostService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by jake on 17. 3. 12.
 */
public class luceneSearchTest {

    @Autowired
    private PostService postService;

    @Test
    @Transactional
    public void testLuceneSearch() throws JsonProcessingException {
        List<PostSummaryDto> dtoList = postService.search("인어", 0, 100);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new Hibernate5Module());
        String result = objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true)
                .writeValueAsString(dtoList);
        System.out.println(result);
        System.out.println(dtoList.size());
    }
}
