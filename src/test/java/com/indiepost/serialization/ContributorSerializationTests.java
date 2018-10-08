package com.indiepost.serialization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.indiepost.NewIndiepostApplication;
import com.indiepost.dto.ContributorDto;
import com.indiepost.enums.Types;
import com.indiepost.service.ContributorService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.inject.Inject;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = NewIndiepostApplication.class)
@WebAppConfiguration
public class ContributorSerializationTests {

    @Inject
    private ContributorService contributorService;

    @Test
    public void pagedContributorDtoListShouldSerializeCorrectly() throws JsonProcessingException {
        PageRequest pageRequest = new PageRequest(0, 10);
        Page<ContributorDto> page = contributorService.find(Types.ContributorType.FeatureEditor, pageRequest);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new Hibernate5Module());
        System.out.println("*** Start serialize StaticPage<ContributorDto> ***");
        System.out.println("Result Length: " + page.getContent().size());
        String result = objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true)
                .writeValueAsString(page);
        System.out.println(result);
    }

    @Test
    public void contributorDtoShouldSerializeCorrectly() throws JsonProcessingException {
        ContributorDto dto = contributorService.findOne(1L);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new Hibernate5Module());
        System.out.println("*** Start serialize ContributorDto ***");
        String result = objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true)
                .writeValueAsString(dto);
        System.out.println(result);
    }
}
