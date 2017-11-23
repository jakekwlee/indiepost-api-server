package com.indiepost.service;

import com.indiepost.NewIndiepostApplication;
import com.indiepost.dto.post.AdminPostResponseDto;
import com.indiepost.dto.post.AdminPostSummaryDto;
import com.indiepost.enums.Types.PostStatus;
import com.indiepost.model.ImageSet;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.junit.Assert.*;
import static testHelper.JsonSerializer.printToJson;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = NewIndiepostApplication.class)
@WebAppConfiguration
@ContextConfiguration
public class AdminPostServiceTests {

    @Autowired
    private AdminPostService adminPostService;

    @Test
    @WithMockUser(username = "indiepost")
    public void findOneShouldWorkCorrectly() {
        // Example Post: <꿈과 현실 사이 경계의 시학, 데이빗 린치의 세계>
        AdminPostResponseDto responseDto = adminPostService.findOne(4557L);
        assertTrue(
                "Post content should not be empty",
                isNotEmpty(responseDto.getContent())
        );
        Long[] expectedTags = {4032L, 2062L, 1544L, 1206L, 1198L};
        assertNotNull(
                "Post tagIds should not be null",
                responseDto.getTagIds()
        );
        assertArrayEquals(
                "Post tagIds contain array of tag_id properly",
                expectedTags, responseDto.getTagIds().toArray()
        );
        ImageSet imageSet = responseDto.getTitleImage();
        assertNotNull(
                "Post titleImage should be set properly",
                imageSet
        );
        assertTrue(
                "Post titleImage should contain multiple image resolutions",
                imageSet.getImages().size() > 0
        );
        Long[] expectedProfiles = {1L, 2L};
        assertNotNull(
                "Post contributorIds should not be null",
                responseDto.getContributorIds()
        );
        assertArrayEquals(
                "Post contributorIds contain array of contributor_id properly",
                expectedProfiles,
                responseDto.getContributorIds().toArray()
        );
        printToJson(responseDto);
    }

    @Test
    @WithMockUser(username = "eunjechoi")
    public void findShouldReturnProperResultSet() {
        final int PAGE = 0;
        final int MAX_RESULTS = 50;

        List<AdminPostSummaryDto> results = adminPostService.find(PAGE, MAX_RESULTS, true);

        assertEquals(
                "Size of List<AdminPostSummaryDto> is exactly same as expected",
                MAX_RESULTS,
                results.size()
        );

        List<String> draftStatusList =
                Arrays.asList(PostStatus.AUTOSAVE.toString(), PostStatus.DRAFT.toString(), PostStatus.TRASH.toString());
        List<AdminPostSummaryDto> drafts = results.stream()
                .filter(post -> draftStatusList.contains(post.getStatus()))
                .collect(Collectors.toList());

        for (AdminPostSummaryDto dto : drafts) {
            Assert.assertEquals(
                    "List<AdminPostSummaryDto> should contain only creator's posts",
                    "Eunje Choi",
                    dto.getCreatorName()
            );
        }

        Long id = 0L;
        boolean isUnique = true;
        for (AdminPostSummaryDto dto : results) {
            if (dto.getId().equals(id)) {
                isUnique = false;
            }
            assertNotNull("AdminPostSummaryDto should contain it's title", dto.getTitle());
            assertTrue("AdminPostSummaryDto should contain it's bylineName", StringUtils.isNotEmpty(dto.getBylineName()));
            assertNotNull("AdminPostSummaryDto should contain it's categoryName", dto.getCategoryName());
            assertNotNull("AdminPostSummaryDto should contain it's creatorName", dto.getCreatorName());
            assertNotNull("AdminPostSummaryDto should contain it's created time", dto.getCreatedAt());
            assertNotNull("AdminPostSummaryDto should contain it's modified time", dto.getModifiedAt());
            assertNotNull("AdminPostSummaryDto should contain it's modifiedUserName", dto.getModifiedUserName());
        }
        assertTrue("All AdminPostSummaryDto should have unique id", isUnique);
        printToJson(results);
    }
}
