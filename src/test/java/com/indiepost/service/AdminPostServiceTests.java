package com.indiepost.service;

import com.indiepost.NewIndiepostApplication;
import com.indiepost.dto.post.AdminPostResponseDto;
import com.indiepost.dto.post.AdminPostSummaryDto;
import com.indiepost.dto.post.PostQuery;
import com.indiepost.enums.Types;
import com.indiepost.model.ImageSet;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.junit.Assert.*;
import static testHelper.JsonSerializer.printToJson;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = NewIndiepostApplication.class)
@WebAppConfiguration
public class AdminPostServiceTests {

    @Autowired
    private AdminPostService adminPostService;

    @Test
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
    public void findShouldReturnProperResultSet() {
        final int PAGE = 0;
        final int MAX_RESULTS = 24;
        final long CREATOR_ID = 10L;
        final Types.PostStatus POST_STATUS = Types.PostStatus.PUBLISH;

        PostQuery query = new PostQuery();
        query.setPage(PAGE);
        query.setMaxResults(MAX_RESULTS);
        query.setStatus(POST_STATUS);
        query.setCreatorId(CREATOR_ID);
        List<AdminPostSummaryDto> results = adminPostService.find(query);

        assertEquals(
                "Size of List<AdminPostSummaryDto> is exactly same as expected",
                MAX_RESULTS,
                results.size()
        );

        Long id = 0L;
        boolean isUnique = true;
        for (AdminPostSummaryDto dto : results) {
            if (dto.getId().equals(id)) {
                isUnique = false;
            }
            assertNotNull("AdminPostSummaryDto should contain it's title", dto.getTitle());
            assertNotNull("AdminPostSummaryDto should contain it's displayName", dto.getDisplayName());
            assertNotNull("AdminPostSummaryDto should contain it's categoryName", dto.getCategoryName());
            assertNotNull("AdminPostSummaryDto should contain it's creatorName", dto.getCreatorName());
            assertNotNull("AdminPostSummaryDto should contain it's created time", dto.getCreatedAt());
            assertNotNull("AdminPostSummaryDto should contain it's modified time", dto.getModifiedAt());
            assertNotNull("AdminPostSummaryDto should contain it's modifiedUserName", dto.getModifiedUserName());
            assertTrue(
                    "Status of AdminPostSummaryDto should match with expected value",
                    Types.PostStatus.valueOf(dto.getStatus()).equals(POST_STATUS)
            );
        }
        assertTrue("All AdminPostSummaryDto should have unique id", isUnique);
        printToJson(results);
    }
}
