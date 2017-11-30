package com.indiepost.service;

import com.indiepost.NewIndiepostApplication;
import com.indiepost.dto.post.AdminPostRequestDto;
import com.indiepost.dto.post.AdminPostResponseDto;
import com.indiepost.dto.post.AdminPostSummaryDto;
import com.indiepost.enums.Types.PostStatus;
import com.indiepost.model.Post;
import org.apache.commons.lang3.RandomStringUtils;
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

    protected static boolean areListContentsEqual(List a, List b) {
        return a.containsAll(b) && b.containsAll(a);
    }

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
        assertNotNull(
                "Post titleImage should contain multiple image resolutions",
                responseDto.getTitleImage()
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
            assertThat(dto.getTitle(), notNullValue());
            assertTrue(StringUtils.isNotEmpty(dto.getBylineName()));
            assertThat(dto.getCategoryName(), notNullValue());
            assertThat(dto.getCreatorName(), notNullValue());
            assertThat(dto.getCreatedAt(), notNullValue());
            assertThat(dto.getModifiedAt(), notNullValue());
            assertThat(dto.getModifiedUserName(), notNullValue());
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
    public void createAutosaveWithNoParamShouldReturnAutosavedPostResponseDtoProperly() {
        Post post = new Post();
        AdminPostResponseDto autosave = adminPostService.createAutosave();
        printToJson(autosave);

        assertThat(autosave, notNullValue());
        assertThat(autosave.getId(), notNullValue());
        assertThat(autosave.getOriginalId(), nullValue());
        assertThat(autosave.getTitle(), is(post.getTitle()));
        assertThat(autosave.getExcerpt(), is(post.getExcerpt()));
        assertThat(autosave.getContent(), is(post.getContent()));

        createdPostIds.add(autosave.getId());
    }

    @Test
    @WithMockUser(username = "eunjechoi")
    public void createAutosaveWithExistPostIdWorksProperly() {
        Long originalPostId = 4557L;
        AdminPostResponseDto original = adminPostService.findOne(originalPostId);
        AdminPostResponseDto autosave = adminPostService.createAutosave(originalPostId);
        printToJson(autosave);

        assertThat(autosave, notNullValue());
        assertThat(autosave.getId(), allOf(notNullValue(), not(original.getId())));
        assertThat(autosave.getOriginalId(), allOf(notNullValue(), is(original.getId())));
        assertThat(autosave.getTitle(), is(original.getTitle()));
        assertThat(autosave.getExcerpt(), is(original.getExcerpt()));
        assertThat(autosave.getContent(), is(original.getContent()));
        assertThat(autosave.getBylineName(), is(original.getBylineName()));
        assertThat(autosave.getTitleImageId(), is(original.getTitleImageId()));
        assertThat(autosave.getTitleImage(), notNullValue());
        assertThat(autosave.getTitleImage().getId(), is(original.getTitleImageId()));
        assertTrue(areListContentsEqual(original.getContributorIds(), autosave.getContributorIds()));
        assertTrue(areListContentsEqual(original.getTagIds(), autosave.getTagIds()));

        createdPostIds.add(autosave.getId());
    }

    @Test
    @WithMockUser(username = "eunjechoi")
    public void updateAutosavedCopyToPublicStatusWorksProperly() {
        Long originalPostId = 3943L;
        AdminPostResponseDto autosave = adminPostService.createAutosave(originalPostId);

        AdminPostRequestDto requestDto = new AdminPostRequestDto();

        // Unchanged
        requestDto.setId(autosave.getId());
        requestDto.setOriginalId(autosave.getOriginalId());
        requestDto.setCategoryId(autosave.getCategoryId());
        requestDto.setTitleImageId(autosave.getTitleImageId());

        // Changed
        requestDto.setBylineName(RandomStringUtils.randomAlphabetic(10));
        requestDto.setTitle(RandomStringUtils.randomAlphabetic(10));
        requestDto.setExcerpt(RandomStringUtils.randomAlphabetic(10));
        requestDto.setContent(RandomStringUtils.randomAlphabetic(10));
        requestDto.setContributorIds(Arrays.asList(1L, 2L));
        requestDto.setTagIds(Arrays.asList(4280L, 4279L));
        requestDto.setStatus(PostStatus.PUBLISH.toString());
        adminPostService.update(requestDto);

        AdminPostResponseDto deleted = adminPostService.findOne(autosave.getId());
        assertThat(deleted, nullValue());

        AdminPostResponseDto updatedOriginal = adminPostService.findOne(originalPostId);
        printToJson(updatedOriginal);

        assertThat(updatedOriginal.getId(), is(requestDto.getOriginalId()));
        assertThat(updatedOriginal.getOriginalId(), nullValue());
        assertThat(updatedOriginal.getCategoryId(), is(requestDto.getCategoryId()));
        assertThat(updatedOriginal.getTitleImageId(), is(requestDto.getTitleImageId()));
        assertThat(updatedOriginal.getTitle(), is(requestDto.getTitle()));
        assertThat(updatedOriginal.getExcerpt(), is(requestDto.getExcerpt()));
        assertThat(updatedOriginal.getBylineName(), is(requestDto.getBylineName()));
        assertThat(updatedOriginal.getContent(), is(requestDto.getContent()));
        assertThat(updatedOriginal.getStatus(), is(requestDto.getStatus()));
        assertTrue(areListContentsEqual(requestDto.getTagIds(), updatedOriginal.getTagIds()));
        assertTrue(areListContentsEqual(requestDto.getContributorIds(), updatedOriginal.getContributorIds()));
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
