package com.indiepost.dtoSerializationTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.indiepost.NewIndiepostApplication;
import com.indiepost.dto.AdminPostResponseDto;
import com.indiepost.dto.AdminPostSummaryDto;
import com.indiepost.service.AdminPostService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

/**
 * Created by jake on 17. 1. 22.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NewIndiepostApplication.class)
@WebAppConfiguration
public class AdminPostDtoSerializaionTest {

    @Autowired
    private AdminPostService adminPostService;

    /**
     * Usage: CMS Datatable
     *
     * @throws JsonProcessingException
     */
    @Test
    public void adminPostSummaryDtoListShouldSerializeCorrectly() throws JsonProcessingException {
        List<AdminPostSummaryDto> postList = adminPostService.find(20, 10, true);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new Hibernate5Module());
        System.out.println("*** Start serialize List<AdminPostResponseDto> ***");
        System.out.println("Result Length: " + postList.size());
        String result = objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true)
                .writeValueAsString(postList);

        System.out.println(result);
    }

    /**
     * Usage: CMS PostEditor, PostPreview
     *
     * @throws JsonProcessingException
     */
    @Test
    public void adminPostResponseDtoShouldSerializeCorrectly() throws JsonProcessingException {
        AdminPostResponseDto dto = adminPostService.getDtoById(291L);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new Hibernate5Module());
        System.out.println("*** Start serialize AdminPostResponseDto ***");
        String result = objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true)
                .writeValueAsString(dto);

        System.out.println(result);
    }

}
