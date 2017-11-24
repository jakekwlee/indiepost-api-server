package com.indiepost.service;

import com.indiepost.NewIndiepostApplication;
import com.indiepost.dto.post.AdminPostRequestDto;
import com.indiepost.dto.post.AdminPostResponseDto;
import com.indiepost.dto.post.AdminPostSummaryDto;
import com.indiepost.enums.Types.PostStatus;
import com.indiepost.model.ImageSet;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.*;
import static testHelper.JsonSerializer.printToJson;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = NewIndiepostApplication.class)
@WebAppConfiguration
@ContextConfiguration
public class AdminPostServiceTests {
    private static final int PAGE = 0;
    private static final int MAX_RESULTS = 50;
    private List<Long> createdPostIds = new ArrayList<>();

    @Autowired
    private AdminPostService adminPostService;

    @Test
    @WithMockUser("indiepost")
    public void retrievedPostShouldContainListOfTagIds() {
        // Example Post: <꿈과 현실 사이 경계의 시학, 데이빗 린치의 세계>
        AdminPostResponseDto responseDto = adminPostService.findOne(4557L);
        Long[] expectedTags = {4032L, 2062L, 1544L, 1206L, 1198L};
        assertArrayEquals(
                "Retrieved Post should contain titleImage properly",
                expectedTags, responseDto.getTagIds().toArray()
        );
    }

    @Test
    @WithMockUser("indiepost")
    public void retrievedPostShouldContainProperTitleImage() {
        AdminPostResponseDto responseDto = adminPostService.findOne(4557L);
        ImageSet imageSet = responseDto.getTitleImage();
        assertTrue(
                "Post titleImage should contain multiple image resolutions",
                imageSet.getImages().size() > 0
        );
    }

    @Test
    @WithMockUser("indiepost")
    public void retrievedPostShouldContainListOfContributorIds() {
        AdminPostResponseDto responseDto = adminPostService.findOne(4557L);
        Long[] expectedContributors = {1L, 2L};
        assertArrayEquals(
                "Post contributorIds contain array of contributor_id properly",
                expectedContributors,
                responseDto.getContributorIds().toArray()
        );
    }

    @Test
    @WithMockUser(username = "indiepost")
    public void findOneShouldReturnAnAdminPostResponseDtoProperly() {
        AdminPostResponseDto responseDto = adminPostService.findOne(4557L);
        assertNotNull("AdminPostService::findOne should return an AdminPostResponseDto properly", responseDto);
        printToJson(responseDto);
    }

    @Test
    @WithMockUser(username = "eunjechoi")
    public void resultSetRetrievedByUserHasEditorRoleShouldNotContainOtherUsersDraftPosts() {
        List<AdminPostSummaryDto> results = adminPostService.find(PAGE, MAX_RESULTS, true);
        List<String> draftStatusList = Arrays.asList(
                PostStatus.AUTOSAVE.toString(),
                PostStatus.DRAFT.toString(),
                PostStatus.TRASH.toString()
        );
        List<AdminPostSummaryDto> drafts = results.stream()
                .filter(post -> draftStatusList.contains(post.getStatus()))
                .collect(Collectors.toList());
        Assert.assertTrue("Draft list should not be empty", !drafts.isEmpty());

        for (AdminPostSummaryDto dto : drafts) {
            Assert.assertEquals(
                    "List<AdminPostSummaryDto> retrieved by user has editor role should not contain other user's drafts",
                    "Eunje Choi",
                    dto.getCreatorName()
            );
        }
    }

    @Test
    @WithMockUser(username = "indiepost")
    public void retrievedResultSetShouldHaveExactlySameSizeAsExpected() {
        List<AdminPostSummaryDto> results = adminPostService.find(PAGE, MAX_RESULTS, true);
        assertEquals(
                "Size of List<AdminPostSummaryDto> is exactly same as expected",
                MAX_RESULTS,
                results.size()
        );
    }

    @Test
    @WithMockUser(username = "indiepost")
    public void allRetrievedDtoShouldHaveUniqueId() {
        List<AdminPostSummaryDto> results = adminPostService.find(PAGE, MAX_RESULTS, true);
        Long id = 0L;
        boolean isUnique = true;
        for (AdminPostSummaryDto dto : results) {
            if (dto.getId().equals(id)) {
                isUnique = false;
            }
        }
        assertTrue("All AdminPostSummaryDto should have unique id", isUnique);
    }

    @Test
    @WithMockUser(username = "eunjechoi")
    public void allRetrievedDtoShouldHaveFieldsProperly() {
        List<AdminPostSummaryDto> results = adminPostService.find(PAGE, MAX_RESULTS, true);
        for (AdminPostSummaryDto dto : results) {
            assertNotNull("AdminPostSummaryDto should contain it's title", dto.getTitle());
            assertTrue("AdminPostSummaryDto should contain it's bylineName", StringUtils.isNotEmpty(dto.getBylineName()));
            assertNotNull("AdminPostSummaryDto should contain it's categoryName", dto.getCategoryName());
            assertNotNull("AdminPostSummaryDto should contain it's creatorName", dto.getCreatorName());
            assertNotNull("AdminPostSummaryDto should contain it's created time", dto.getCreatedAt());
            assertNotNull("AdminPostSummaryDto should contain it's modified time", dto.getModifiedAt());
            assertNotNull("AdminPostSummaryDto should contain it's modifiedUserName", dto.getModifiedUserName());
        }
    }

    @Test
    public void findAllBylineNamesShouldReturnListOfStrings() {
        List<String> bylineNames = adminPostService.findAllBylineNames();
        assertNotNull("findAllBylineNames should return list of strings", bylineNames);
        printToJson(bylineNames);
    }

    @Test
    @WithMockUser(username = "eunjechoi")
    public void createAutosaveFirstTimeShouldWorksProperly() {
        AdminPostRequestDto fromClient = new AdminPostRequestDto();
        fromClient.setTitle("Test Title");
        fromClient.setContributorIds(Arrays.asList(1L, 2L));
        fromClient.setTagIds(Arrays.asList(4280L, 4279L));
        fromClient.setTitleImageId(2047L);
        AdminPostResponseDto savedPost = adminPostService.createAutosave(fromClient);

        assertThat(savedPost.getId(), notNullValue());
        assertThat(savedPost.getTitle(), is(fromClient.getTitle()));
        assertThat(savedPost.getContent(), notNullValue());
        assertThat(savedPost.getExcerpt(), notNullValue());
        assertThat(savedPost.getBylineName(), notNullValue());
        assertThat(savedPost.getCreatorId(), notNullValue());
        assertThat(savedPost.getModifiedUserId(), notNullValue());
        assertThat(savedPost.getCreatedAt(), notNullValue());
        assertThat(savedPost.getModifiedAt(), notNullValue());
        assertThat(savedPost.getCreatedAt(), notNullValue());
        assertThat(savedPost.getTitleImage(), notNullValue());
        assertThat(savedPost.getTitleImage().getId(), is(fromClient.getTitleImageId()));
        assertThat(savedPost.getTagIds(), containsInAnyOrder(fromClient.getTagIds()));
        assertThat(savedPost.getContributorIds(), containsInAnyOrder(fromClient.getContributorIds()));
        assertThat(savedPost.getOriginalId(), nullValue());

        createdPostIds.add(savedPost.getId());
        printToJson(savedPost);
    }

    @After
    public void deleteAllCreatedTestPosts() {
        if (createdPostIds.isEmpty()) {
            return;
        }
        for (Long id : createdPostIds) {
            adminPostService.deleteById(id);
        }
    }
}
