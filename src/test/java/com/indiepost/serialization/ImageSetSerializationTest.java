package com.indiepost.serialization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.indiepost.NewIndiepostApplication;
import com.indiepost.model.ImageSet;
import com.indiepost.service.ImageService;
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
public class ImageSetSerializationTest {

    @Autowired
    private ImageService imageService;

    /**
     * Usage: CMS MediaExplorer
     *
     * @throws JsonProcessingException
     */
    @Test
    public void imageSetShouldSerializeCorrectly() throws JsonProcessingException {
        List<ImageSet> imageSetList = imageService.findAll(2, 10);
        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println("*** Start serialize List<ImageSet> ***");
        System.out.println("Result Length: " + imageSetList.size());
        String result = objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true)
                .writeValueAsString(imageSetList);

        System.out.println(result);
    }
}
