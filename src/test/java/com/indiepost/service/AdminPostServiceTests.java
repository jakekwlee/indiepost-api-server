package com.indiepost.service;

import com.indiepost.NewIndiepostApplication;
import com.indiepost.dto.post.AdminPostRequestDto;
import com.indiepost.dto.post.AdminPostResponseDto;
import com.indiepost.dto.post.AdminPostSummaryDto;
import com.indiepost.enums.Types.PostStatus;
import com.indiepost.model.Post;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
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

    @Test
    @WithMockUser("indiepost")
    public void retrievedPostShouldContainListOfTagIds() {
        // Example Post: <세 가지 연기와 사랑: 쥘리에트 비노슈의 연기상 수상작들>
        AdminPostResponseDto responseDto = adminPostService.findOne(5789L);
        printToJson(responseDto);
        List<String> expectedTags = Arrays.asList(
                "프랑스", "줄리엣비노쉬", "쥘리에트비노슈", "세가지색블루", "잉글리쉬페이션트", "사랑을카피하다", "러브앤아트"
        );

        assertThat(responseDto.getTags())
                .isEqualTo(expectedTags);
    }

    @Test
    @WithMockUser("indiepost")
    public void retrievedPostShouldContainProperTitleImage() {
        AdminPostResponseDto responseDto = adminPostService.findOne(1945L);
        assertThat(responseDto.getTitleImage())
                .isNotNull();
    }

    @Test
    @WithMockUser("indiepost")
    public void retrievedPostShouldContainListOfContributorIds() {
        AdminPostResponseDto responseDto = adminPostService.findOne(5495L);
        List<String> expectedContributorNames = Arrays.asList("최은제", "김유영");
        assertThat(responseDto.getContributors())
                .isEqualTo(expectedContributorNames);
    }

    @Test
    @WithMockUser(username = "indiepost")
    public void findOneShouldReturnAnAdminPostResponseDtoProperly() {
        AdminPostResponseDto responseDto = adminPostService.findOne(5891L);
        printToJson(responseDto);
        assertThat(responseDto)
                .isNotNull();
    }

    @Test
    @WithMockUser(username = "eunjechoi")
    public void resultSetRetrievedByUserHasEditorRoleShouldNotContainOtherUsersDraftPosts() {
        Page<AdminPostSummaryDto> results = adminPostService.find(
                PostStatus.AUTOSAVE,
                new PageRequest(PAGE, MAX_RESULTS)
        );
        printToJson(results);
        List<String> draftStatusList = Arrays.asList(
                PostStatus.AUTOSAVE.toString(),
                PostStatus.DRAFT.toString(),
                PostStatus.TRASH.toString()
        );
        List<AdminPostSummaryDto> drafts = results.getContent().stream()
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
        Page<AdminPostSummaryDto> results = adminPostService.find(
                PostStatus.PUBLISH, new PageRequest(PAGE, MAX_RESULTS)
        );
        printToJson(results);
        assertThat(results.getContent().size())
                .isEqualTo(MAX_RESULTS);
    }

    @Test
    @WithMockUser(username = "indiepost")
    public void allRetrievedDtoShouldHaveUniqueId() {
        Page<AdminPostSummaryDto> results = adminPostService.find(
                PostStatus.PUBLISH,
                new PageRequest(PAGE, MAX_RESULTS)
        );
        printToJson(results);
        Long id = 0L;
        boolean isUnique = true;
        for (AdminPostSummaryDto dto : results.getContent()) {
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
        Page<AdminPostSummaryDto> results = adminPostService.find(
                PostStatus.PUBLISH,
                new PageRequest(PAGE, MAX_RESULTS)
        );
        printToJson(results);
        for (AdminPostSummaryDto dto : results.getContent()) {
            assertThat(dto.getTitle()).isNotNull();
            assertThat(dto.getBylineName()).isNotNull();
            assertThat(dto.getCategoryName()).isNotNull();
            assertThat(dto.getCreatorName()).isNotNull();
            assertThat(dto.getCreatedAt()).isNotNull();
            assertThat(dto.getModifiedAt()).isNotNull();
            assertThat(dto.getModifiedUserName()).isNotNull();
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

        assertThat(autosave).isNotNull();
        assertThat(autosave.getId()).isNotNull();
        assertThat(autosave.getOriginalId()).isNull();
        assertThat(autosave.getTitle()).isEqualTo(post.getTitle());
        assertThat(autosave.getExcerpt()).isEqualTo(post.getExcerpt());
        assertThat(autosave.getContent()).isEqualTo(post.getContent());
    }

    @Test
    @WithMockUser(username = "eunjechoi")
    public void createAutosaveWithExistPostIdWorksProperly() {
        Long originalPostId = 5791L;
        AdminPostResponseDto original = adminPostService.findOne(originalPostId);
        AdminPostResponseDto autosave = adminPostService.createAutosave(originalPostId);
        printToJson(autosave);

        assertThat(autosave).isNotNull();
        assertThat(autosave.getId())
                .isNotNull()
                .isNotEqualTo(original.getId());
        assertThat(autosave.getOriginalId())
                .isNotNull()
                .isEqualTo(original.getId());
        assertThat(autosave.getTitle()).isEqualTo(original.getTitle());

        assertThat(autosave.getExcerpt()).isEqualTo(original.getExcerpt());
        assertThat(autosave.getContent()).isEqualTo(original.getContent());
        assertThat(autosave.getBylineName()).isEqualTo(original.getBylineName());
        assertThat(autosave.getTitleImageId()).isEqualTo(original.getTitleImageId());
        assertThat(autosave.getTitleImage()).isNotNull();
        assertThat(autosave.getTitleImage().getId()).isEqualTo(original.getTitleImageId());
        assertThat(original.getContributors()).isEqualTo(autosave.getContributors());
        assertThat(original.getTags()).isEqualTo(autosave.getTags());
    }

    @Test
    @WithMockUser(username = "eunjechoi")
    public void updateAutosaveToAutosaveOrDraft() {
        Long originalPostId = 5791L;
        AdminPostResponseDto autosave = adminPostService.createAutosave(originalPostId);
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
        requestDto.setContributors(Arrays.asList("최은제", "김유영"));
        requestDto.setTags(Arrays.asList("레오까락스", "미셀공드리", "단편영화"));
        requestDto.setStatus(PostStatus.AUTOSAVE.toString());

        // update
        log.info("Start updating autosave post: " + autosave.getId());
        adminPostService.update(requestDto);

        AdminPostResponseDto updated = adminPostService.findOne(autosave.getId());
        printToJson(updated);
        assertThat(updated).isNotNull();
        assertThat(updated.getId()).isEqualTo(requestDto.getId());
        assertThat(updated.getOriginalId()).isEqualTo(requestDto.getOriginalId());
        assertThat(updated.getCategoryId()).isEqualTo(requestDto.getCategoryId());
        assertThat(updated.getTitleImageId()).isEqualTo(requestDto.getTitleImageId());
        assertThat(updated.getTitle()).isEqualTo(requestDto.getTitle());

        assertThat(updated.getExcerpt()).isEqualTo(requestDto.getExcerpt());
        assertThat(updated.getBylineName()).isEqualTo(requestDto.getBylineName());
        assertThat(updated.getContent()).isEqualTo(requestDto.getContent());
        assertThat(updated.getStatus()).isEqualTo(requestDto.getStatus());
        assertThat(requestDto.getTags()).isEqualTo(updated.getTags());
        assertThat(requestDto.getContributors()).isEqualTo(updated.getContributors());

        log.info("Serialization start: " + updated.getId());
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
        requestDto.setContributors(Arrays.asList("김유영", "최은제"));
        requestDto.setTags(Arrays.asList("vaporwave", "개봉예정작", "토요일", "DJ미소시루토MC고항", "Antal", "두경"));
        requestDto.setStatus(PostStatus.PUBLISH.toString());

        // update
        log.info("Start updating original post: " + autosave.getOriginalId());
        adminPostService.update(requestDto);

        AdminPostResponseDto deleted = adminPostService.findOne(autosave.getId());
        assertThat(deleted).isNull();

        AdminPostResponseDto updatedOriginal = adminPostService.findOne(originalPostId);

        assertThat(updatedOriginal.getId()).isEqualTo(requestDto.getOriginalId());
        assertThat(updatedOriginal.getOriginalId()).isNull();
        assertThat(updatedOriginal.getCategoryId()).isEqualTo(requestDto.getCategoryId());
        assertThat(updatedOriginal.getTitleImageId()).isEqualTo(requestDto.getTitleImageId());
        assertThat(updatedOriginal.getTitle()).isEqualTo(requestDto.getTitle());
        assertThat(updatedOriginal.getExcerpt()).isEqualTo(requestDto.getExcerpt());
        assertThat(updatedOriginal.getBylineName()).isEqualTo(requestDto.getBylineName());

        assertThat(updatedOriginal.getContent()).isEqualTo(requestDto.getContent());
        assertThat(updatedOriginal.getStatus()).isEqualTo(requestDto.getStatus());
        assertThat(requestDto.getTags()).isEqualTo(updatedOriginal.getTags());
        assertThat(requestDto.getContributors()).isEqualTo(updatedOriginal.getContributors());

        log.info("Serialization start: " + updatedOriginal.getId());
        printToJson(updatedOriginal);
    }
}
