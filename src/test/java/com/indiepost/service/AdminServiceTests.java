package com.indiepost.service;

import com.indiepost.NewIndiepostApplication;
import com.indiepost.dto.AdminInitialResponse;
import com.indiepost.dto.CategoryDto;
import com.indiepost.dto.ContributorDto;
import com.indiepost.dto.TagDto;
import com.indiepost.dto.post.Title;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.inject.Inject;

import static com.indiepost.testHelper.JsonSerializer.printToJson;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = NewIndiepostApplication.class)
@WebAppConfiguration
public class AdminServiceTests {
    @Inject
    private AdminService service;

    @Test
    @WithMockUser("auth0|5b213cd8064de34cde981b47")
    public void buildInitialResponse_shouldReturnInitialDataProperly() {
        AdminInitialResponse dto = service.buildInitialResponse();
        assertThat(dto.getAuthorNames())
                .isNotNull()
                .hasAtLeastOneElementOfType(String.class);
        assertThat(dto.getContributors())
                .isNotNull()
                .hasAtLeastOneElementOfType(ContributorDto.class);
        assertThat(dto.getCategories())
                .isNotNull()
                .hasAtLeastOneElementOfType(CategoryDto.class);
        assertThat(dto.getCurrentUser()).isNotNull();
        assertThat(dto.getTags())
                .isNotNull()
                .hasAtLeastOneElementOfType(TagDto.class);
        assertThat(dto.getPostTitles())
                .isNotNull()
                .hasAtLeastOneElementOfType(Title.class);
        printToJson(dto);
    }
}
