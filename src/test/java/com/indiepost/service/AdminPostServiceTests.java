package com.indiepost.service;

import com.indiepost.NewIndiepostApplication;
import com.indiepost.dto.post.AdminPostRequestDto;
import com.indiepost.dto.post.AdminPostResponseDto;
import com.indiepost.dto.post.AdminPostSummaryDto;
import com.indiepost.dto.post.PostSearch;
import com.indiepost.enums.Types.PostStatus;
import com.indiepost.model.Post;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
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
    private static final Logger log = LoggerFactory.getLogger(AdminPostServiceTests.class);

    @Inject
    private AdminPostService adminPostService;

    protected static boolean areListContentsEqual(List a, List b) {
        return a.containsAll(b) && b.containsAll(a);
    }

    @Test
    @WithMockUser("indiepost")
    public void retrievedPostShouldContainListOfTagIds() {
        // Example Post: <꿈과 현실 사이 경계의 시학, 데이빗 린치의 세계>
        AdminPostResponseDto responseDto = adminPostService.findOne(4557L);
        printToJson(responseDto);
        List<Long> expectedTags = Arrays.asList(1198L, 1206L, 1544L, 2062L, 4032L);
        assertThat(responseDto.getTagIds())
                .isEqualTo(expectedTags);
    }

    @Test
    @WithMockUser("indiepost")
    public void retrievedPostShouldContainProperTitleImage() {
        AdminPostResponseDto responseDto = adminPostService.findOne(4557L);
        assertThat(responseDto.getTitleImage())
                .isNotNull();
    }

    @Test
    @WithMockUser("indiepost")
    public void retrievedPostShouldContainListOfContributorIds() {
        AdminPostResponseDto responseDto = adminPostService.findOne(4557L);
        List<Long> expectedContributorIds = Arrays.asList(1L, 2L);
        assertThat(responseDto.getContributorIds())
                .isEqualTo(expectedContributorIds);
    }

    @Test
    @WithMockUser(username = "indiepost")
    public void findOneShouldReturnAnAdminPostResponseDtoProperly() {
        AdminPostResponseDto responseDto = adminPostService.findOne(4557L);
        printToJson(responseDto);
        assertThat(responseDto)
                .isNotNull();
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
        assertThat(drafts)
                .isNotEmpty();

        String editorName = "Eunje Choi";
        for (AdminPostSummaryDto dto : drafts) {
            assertThat(dto.getCreatorName())
                    .withFailMessage("List<AdminPostSummaryDto> retrieved " +
                            "by user has editor role should not contain other user's drafts")
                    .isEqualTo(editorName);
        }
    }

    @Test
    @WithMockUser(username = "indiepost")
    public void retrievedResultSetShouldHaveExactlySameSizeAsExpected() {
        List<AdminPostSummaryDto> results = adminPostService.find(PAGE, MAX_RESULTS, true);
        assertThat(results.size())
                .isEqualTo(MAX_RESULTS);
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
        assertThat(isUnique)
                .isTrue();
    }

    @Test
    @WithMockUser(username = "eunjechoi")
    public void allRetrievedDtoShouldHaveFieldsProperly() {
        List<AdminPostSummaryDto> results = adminPostService.find(PAGE, MAX_RESULTS, true);
        for (AdminPostSummaryDto dto : results) {
            Assert.assertThat(dto.getTitle(), notNullValue());
            assertTrue(StringUtils.isNotEmpty(dto.getBylineName()));
            Assert.assertThat(dto.getCategoryName(), notNullValue());
            Assert.assertThat(dto.getCreatorName(), notNullValue());
            Assert.assertThat(dto.getCreatedAt(), notNullValue());
            Assert.assertThat(dto.getModifiedAt(), notNullValue());
            Assert.assertThat(dto.getModifiedUserName(), notNullValue());
        }
    }

    @Test
    @WithMockUser(username = "eunjechoi")
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

        Assert.assertThat(autosave, notNullValue());
        Assert.assertThat(autosave.getId(), notNullValue());
        Assert.assertThat(autosave.getOriginalId(), nullValue());
        Assert.assertThat(autosave.getTitle(), is(post.getTitle()));
        Assert.assertThat(autosave.getExcerpt(), is(post.getExcerpt()));
        Assert.assertThat(autosave.getContent(), is(post.getContent()));
    }

    @Test
    @WithMockUser(username = "eunjechoi")
    public void createAutosaveWithExistPostIdWorksProperly() {
        Long originalPostId = 4557L;
        AdminPostResponseDto original = adminPostService.findOne(originalPostId);
        AdminPostResponseDto autosave = adminPostService.createAutosave(originalPostId);
        printToJson(autosave);

        Assert.assertThat(autosave, notNullValue());
        Assert.assertThat(autosave.getId(), allOf(notNullValue(), not(original.getId())));
        Assert.assertThat(autosave.getOriginalId(), allOf(notNullValue(), is(original.getId())));
        Assert.assertThat(autosave.getTitle(), is(original.getTitle()));
        Assert.assertThat(autosave.getExcerpt(), is(original.getExcerpt()));
        Assert.assertThat(autosave.getContent(), is(original.getContent()));
        Assert.assertThat(autosave.getBylineName(), is(original.getBylineName()));
        Assert.assertThat(autosave.getTitleImageId(), is(original.getTitleImageId()));
        Assert.assertThat(autosave.getTitleImage(), notNullValue());
        Assert.assertThat(autosave.getTitleImage().getId(), is(original.getTitleImageId()));
        assertTrue(areListContentsEqual(original.getContributorIds(), autosave.getContributorIds()));
        assertTrue(areListContentsEqual(original.getTagIds(), autosave.getTagIds()));
    }

    @Test
    @WithMockUser(username = "eunjechoi")
    public void updateAutosaveToAutosaveOrDraft() {
        Long autosaveId = 5402L;
        AdminPostResponseDto autosave = adminPostService.findOne(autosaveId);
        AdminPostRequestDto requestDto = new AdminPostRequestDto();

        // unchanged
        requestDto.setId(autosave.getId());
        requestDto.setOriginalId(autosave.getOriginalId());
        requestDto.setCategoryId(autosave.getCategoryId());
        requestDto.setTitleImageId(autosave.getTitleImageId());

        // changed
        requestDto.setBylineName(RandomStringUtils.randomAlphabetic(10));
        requestDto.setTitle(RandomStringUtils.randomAlphabetic(10));
        requestDto.setExcerpt(RandomStringUtils.randomAlphabetic(10));
        requestDto.setContent(RandomStringUtils.randomAlphabetic(10));
        requestDto.setContributorIds(Arrays.asList(1L, 2L));
        requestDto.setTagIds(Arrays.asList(4280L, 4279L));
        requestDto.setStatus(PostStatus.AUTOSAVE.toString());

        // update
        log.info("Start updating autosave post: " + autosave.getId());
        adminPostService.update(requestDto);

        AdminPostResponseDto updated = adminPostService.findOne(autosave.getId());
        Assert.assertThat(updated, notNullValue());

        Assert.assertThat(updated.getId(), is(requestDto.getId()));
        Assert.assertThat(updated.getOriginalId(), is(requestDto.getOriginalId()));
        Assert.assertThat(updated.getCategoryId(), is(requestDto.getCategoryId()));
        Assert.assertThat(updated.getTitleImageId(), is(requestDto.getTitleImageId()));
        Assert.assertThat(updated.getTitle(), is(requestDto.getTitle()));
        Assert.assertThat(updated.getExcerpt(), is(requestDto.getExcerpt()));
        Assert.assertThat(updated.getBylineName(), is(requestDto.getBylineName()));
        Assert.assertThat(updated.getContent(), is(requestDto.getContent()));
        Assert.assertThat(updated.getStatus(), is(requestDto.getStatus()));
        assertTrue(areListContentsEqual(requestDto.getTagIds(), updated.getTagIds()));
        assertTrue(areListContentsEqual(requestDto.getContributorIds(), updated.getContributorIds()));

        log.info("Serialization start: " + updated.getId());
        printToJson(updated);

    }

    @Test
    @WithMockUser(username = "eunjechoi")
    public void mergeAutosaveIntoPublishedOriginalWorksProperly() {
        Long originalPostId = 3943L;
        AdminPostResponseDto autosave = adminPostService.createAutosave(originalPostId);
        log.info("Autosave created: " + autosave.getId());
        AdminPostRequestDto requestDto = new AdminPostRequestDto();

        // unchanged
        requestDto.setId(autosave.getId());
        requestDto.setOriginalId(autosave.getOriginalId());
        requestDto.setCategoryId(autosave.getCategoryId());
        requestDto.setTitleImageId(autosave.getTitleImageId());

        // changed
        requestDto.setBylineName(RandomStringUtils.randomAlphabetic(10));
        requestDto.setTitle(RandomStringUtils.randomAlphabetic(10));
        requestDto.setExcerpt(RandomStringUtils.randomAlphabetic(10));
        requestDto.setContent(RandomStringUtils.randomAlphabetic(10));
        requestDto.setContributorIds(Arrays.asList(2L, 1L));
        requestDto.setTagIds(Arrays.asList(3173L, 3163L, 4178L, 2287L, 2115L, 28L, 604L));
        requestDto.setStatus(PostStatus.PUBLISH.toString());

        // update
        log.info("Start updating original post: " + autosave.getOriginalId());
        adminPostService.update(requestDto);

        AdminPostResponseDto deleted = adminPostService.findOne(autosave.getId());
        Assert.assertThat(deleted, nullValue());

        AdminPostResponseDto updatedOriginal = adminPostService.findOne(originalPostId);

        Assert.assertThat(updatedOriginal.getId(), is(requestDto.getOriginalId()));
        Assert.assertThat(updatedOriginal.getOriginalId(), nullValue());
        Assert.assertThat(updatedOriginal.getCategoryId(), is(requestDto.getCategoryId()));
        Assert.assertThat(updatedOriginal.getTitleImageId(), is(requestDto.getTitleImageId()));
        Assert.assertThat(updatedOriginal.getTitle(), is(requestDto.getTitle()));
        Assert.assertThat(updatedOriginal.getExcerpt(), is(requestDto.getExcerpt()));
        Assert.assertThat(updatedOriginal.getBylineName(), is(requestDto.getBylineName()));
        Assert.assertThat(updatedOriginal.getContent(), is(requestDto.getContent()));
        Assert.assertThat(updatedOriginal.getStatus(), is(requestDto.getStatus()));
        assertEquals(requestDto.getTagIds(), updatedOriginal.getTagIds());
        assertTrue(areListContentsEqual(requestDto.getContributorIds(), updatedOriginal.getContributorIds()));

        log.info("Serialization start: " + updatedOriginal.getId());
        printToJson(updatedOriginal);
    }

    @After
    public void deleteAllCreatedTestPosts() {
        PostSearch search = new PostSearch();
        search.setStatus(null);
        search.setCreatedAfter(LocalDateTime.of(2017, 11, 21, 0, 0));
        List<AdminPostSummaryDto> posts = adminPostService.search(search, 0, 1000, true);
        for (AdminPostSummaryDto post : posts) {
            adminPostService.deleteById(post.getId());
            log.info("Test post deleted: " + post.getId());
        }

    }
}
