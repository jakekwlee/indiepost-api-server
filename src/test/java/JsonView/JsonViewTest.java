package JsonView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.indiepost.JsonView.Views;
import com.indiepost.NewIndiepostApplication;
import com.indiepost.model.ImageSet;
import com.indiepost.model.Post;
import com.indiepost.service.AdminPostService;
import com.indiepost.service.ImageService;
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
public class JsonViewTest {

    @Autowired
    private ImageService imageService;

    @Autowired
    private AdminPostService adminPostService;

    @Autowired
    private PostService postService;

    /**
     * Usage: MediaExplorer
     *
     * @throws JsonProcessingException
     */
    @Test
    public void imageSetShouldSerializeForAdminCorrectly() throws JsonProcessingException {
        List<ImageSet> imageSetList = imageService.findAll(2, 10);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(MapperFeature.DEFAULT_VIEW_INCLUSION);
        System.out.println("*** Start serialize List<ImageSet> to @JsonView({Views.Admin.class}) ***");
        String result = objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true)
                .writerWithView(Views.Admin.class)
                .writeValueAsString(imageSetList);

        System.out.println(result);
    }

    /**
     * Usage: Datatable
     *
     * @throws JsonProcessingException
     */
    @Test
    public void postListShouldSerializeForAdminListCorrectly() throws JsonProcessingException {
        List<Post> postList = adminPostService.find(20, 10, true);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(MapperFeature.DEFAULT_VIEW_INCLUSION);
        objectMapper.registerModule(new Hibernate5Module());
        System.out.println("*** Start serialize List<Post> to @JsonView({Views.AdminList.class}) ***");
        String result = objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true)
                .writerWithView(Views.AdminList.class)
                .writeValueAsString(postList);

        System.out.println(result);
    }

    /**
     * Usage: PostEditor, PostPreview
     *
     * @throws JsonProcessingException
     */
    @Test
    public void postShouldSerializeForAdminCorrectly() throws JsonProcessingException {
        Post post = adminPostService.findById(520L);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new Hibernate5Module());
        objectMapper.disable(MapperFeature.DEFAULT_VIEW_INCLUSION);
        System.out.println("*** Start serialize Post to @JsonView({Views.Admin.class}) ***");
        String result = objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true)
                .writerWithView(Views.Admin.class)
                .writeValueAsString(post);

        System.out.println(result);
    }

    /**
     * Usage: ContentView
     *
     * @throws JsonProcessingException
     */
    @Test
    public void postShouldSerializeForPublicCorrectly() throws JsonProcessingException {
        Post post = postService.findById(347L);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new Hibernate5Module());
        objectMapper.disable(MapperFeature.DEFAULT_VIEW_INCLUSION);
        System.out.println("*** Start serialize Post to @JsonView({Views.Public.class}) ***");
        String result = objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true)
                .writerWithView(Views.Public.class)
                .writeValueAsString(post);
        System.out.println(result);
    }

    /**
     * Usage: HomePostList
     *
     * @throws JsonProcessingException
     */
    @Test
    public void postListShouldSerializeForPublicListCorrectly() throws JsonProcessingException {
        List<Post> postList = postService.find(5, 10, true);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new Hibernate5Module());
        objectMapper.disable(MapperFeature.DEFAULT_VIEW_INCLUSION);
        System.out.println("\n\n*** Start serialize List<Post> to @JsonView({Views.PublicList.class}) ***\n\n");
        System.out.println("Result Length: " + postList.size());
        String result = objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true)
                .writerWithView(Views.PublicList.class)
                .writeValueAsString(postList);
        System.out.println(result);
    }

}
