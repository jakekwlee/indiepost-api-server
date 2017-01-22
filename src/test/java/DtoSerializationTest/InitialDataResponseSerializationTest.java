package DtoSerializationTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.indiepost.NewIndiepostApplication;
import com.indiepost.dto.InitialResponse;
import com.indiepost.service.InitialDataService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Created by jake on 17. 1. 22.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NewIndiepostApplication.class)
@WebAppConfiguration
public class InitialDataResponseSerializationTest {

    @Autowired
    private InitialDataService initialDataService;

    @Test
    public void initialDataShouldSerializeCorrectly() throws JsonProcessingException {
        InitialResponse initialResponse = initialDataService.getInitialData();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new Hibernate5Module());
        System.out.println("\n\n*** Start serialize InitialResponse ***\n\n");
        String result = objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true)
                .writeValueAsString(initialResponse);
        System.out.println(result);
    }
}
