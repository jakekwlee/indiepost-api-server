package com.indiepost.service;

import com.indiepost.NewIndiepostApplication;
import com.indiepost.dto.user.SyncAuthorizationResponse;
import com.indiepost.dto.user.UserDto;
import com.indiepost.dto.user.UserProfileDto;
import com.indiepost.model.User;
import com.indiepost.utils.DateUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.inject.Inject;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by jake on 17. 11. 13.
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = NewIndiepostApplication.class)
@WebAppConfiguration
public class UserServiceTests {

    @Inject
    private UserService userService;

    @Test
    public void usersShouldHaveUniqueId() {
        List<User> users = userService.findAllUsers(0, 100, true);
        Long id = -1L;
        for (User user : users) {
            assertThat(user.getId()).isEqualTo(id);
            id = user.getId();
        }
    }

    //    @Test
    @WithMockUser("auth0|5a88547af5c8213cb27caf41")
    public void createOrUpdate_ShouldUpdateUserProperlyWhenUserAlreadyExists() {
        UserDto dto = new UserDto();

        dto.setUsername("auth0|5a88547af5c8213cb27caf41");

        dto.setDisplayName("Test Name");
        dto.setGender("MALE");
        dto.setPicture("https://www.google.co.kr/images/branding/googlelogo/2x/googlelogo_color_272x92dp.png");

        Instant updatedAt = DateUtil.localDateTimeToInstant(LocalDateTime.of(2018, 2, 26, 12, 0));
        dto.setUpdatedAt(updatedAt);
        dto.setGender("FEMALE");
        dto.setRoles(Arrays.asList("Administrator", "Editor", "User"));

        SyncAuthorizationResponse syncAuthorizationResponse = userService.syncAuthorization(dto);
        UserDto resultDto = syncAuthorizationResponse.getUser();

        assertThat(syncAuthorizationResponse.isNewUser()).isTrue();
        assertThat(resultDto).isNotNull();
        assertThat(resultDto.getId()).isNotNull();
        assertThat(resultDto.getEmail()).isEqualTo(dto.getEmail());
        assertThat(resultDto.getDisplayName()).isEqualTo(dto.getDisplayName());
        assertThat(resultDto.getPicture()).isEqualTo(dto.getPicture());
        assertThat(resultDto.getUpdatedAt()).isEqualTo(dto.getUpdatedAt());
        assertThat(resultDto.getGender()).isEqualTo(dto.getGender());
        assertThat(resultDto.getUsername()).isEqualTo(dto.getUsername());
        assertThat(resultDto.getRoleType()).isEqualTo(dto.getRoleType());
        assertThat(resultDto.getUpdatedAt()).isEqualTo(dto.getUpdatedAt());

        assertThat(resultDto.getRoles()).containsOnlyElementsOf(dto.getRoles());
    }

    @Test
    @WithMockUser(username = "auth0|5a88547af5c8213cb27caf41")
    public void update_shouldWorkProperly() {
        UserProfileDto dto = new UserProfileDto();
        dto.setId(18L);
        dto.setUsername("oauth2|naver|23423834");
        dto.setEmail("bwv1050@gmail.com");
        dto.setDisplayName("바보");
        userService.update(dto);
    }
}
