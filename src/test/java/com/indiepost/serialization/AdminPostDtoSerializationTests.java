package com.indiepost.serialization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.indiepost.NewIndiepostApplicationKt;
import com.indiepost.dto.post.AdminPostResponseDto;
import com.indiepost.dto.post.AdminPostSummaryDto;
import com.indiepost.enums.Types;
import com.indiepost.service.AdminPostService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.inject.Inject;

/**
 * Created by jake on 17. 1. 22.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NewIndiepostApplicationKt.class)
@WebAppConfiguration
public class AdminPostDtoSerializationTests {

    @Inject
    private AdminPostService adminPostService;

    /**
     * Usage: CMS Datatable
     *
     * @throws JsonProcessingException
     */
    public void adminPostSummaryDtoListShouldSerializeCorrectly() throws JsonProcessingException {
        Page<AdminPostSummaryDto> page = adminPostService.find(Types.PostStatus.PUBLISH, new PageRequest(0, 10));
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new Hibernate5Module());
        System.out.println("*** Start serialize List<AdminPostResponseDto> ***");
        System.out.println("Result Length: " + page.getContent().size());
        String result = objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true)
                .writeValueAsString(page);

        System.out.println(result);
    }

    /**
     * Usage: CMS PostEditor, PostPreview
     *
     * @throws JsonProcessingException
     */
    @Test
    public void adminPostResponseDtoShouldSerializeCorrectly() throws JsonProcessingException {
        AdminPostResponseDto dto = adminPostService.findOne(291L);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new Hibernate5Module());
        System.out.println("*** Start serialize AdminPostResponseDto ***");
        String result = objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true)
                .writeValueAsString(dto);

        System.out.println(result);
    }

}
