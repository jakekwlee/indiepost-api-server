package DtoSerializationTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.indiepost.NewIndiepostApplication;
import com.indiepost.dto.AdminPostResponseDto;
import com.indiepost.dto.AdminPostSummaryDto;
import com.indiepost.dto.PostDto;
import com.indiepost.dto.PostSummaryDto;
import com.indiepost.service.AdminPostService;
import com.indiepost.service.PostService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

/**
 * Created by jake on 17. 1. 18.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NewIndiepostApplication.class)
@WebAppConfiguration
public class PostDtoSerializationTest {

    @Autowired
    private AdminPostService adminPostService;

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
        List<PostSummaryDto> postList = postService.find(3, 10, true);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new Hibernate5Module());
        System.out.println("\n\n*** Start serialize List<PostSummaryDto> ***\n\n");
        System.out.println("Result Length: " + postList.size());
        String result = objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true)
                .writeValueAsString(postList);
        System.out.println(result);
    }

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
