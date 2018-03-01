package com.indiepost.service;

import com.indiepost.NewIndiepostApplication;
import com.indiepost.dto.UserDto;
import com.indiepost.dto.UserUpdateDto;
import com.indiepost.model.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by jake on 17. 11. 13.
 */
@RunWith(SpringRunner.class)
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
            Assert.assertNotEquals(id, user.getId());
            id = user.getId();
        }
    }

    @Test
    @WithMockUser("auth0|5a88547af5c8213cb27caf41")
    public void createOrUpdate_ShouldUpdateUserProperlyWhenUserAlreadyExists() {
        UserDto dto = new UserDto();

        dto.setUsername("auth0|5a88547af5c8213cb27caf41");

        dto.setDisplayName("Test Name");
        dto.setEmail("sysadmin@indiepost.co.kr");
        dto.setGender("MALE");
        dto.setPicture("https://www.google.co.kr/images/branding/googlelogo/2x/googlelogo_color_272x92dp.png");
        dto.setUpdatedAt(LocalDateTime.of(2018, 2, 26, 12, 0));
        dto.setGender("FEMALE");
        dto.setRoles(Arrays.asList("Administrator", "Editor", "User"));

        UserUpdateDto userUpdateDto = userService.createOrUpdate(dto);
        UserDto resultDto = userUpdateDto.getUser();

        assertThat(userUpdateDto.isNew()).isFalse();
        assertThat(resultDto).isNotNull();
        assertThat(resultDto.getId()).isNotNull();
        assertThat(resultDto.getEmail()).isEqualTo(dto.getEmail());
        assertThat(resultDto.getDisplayName()).isEqualTo(dto.getDisplayName());
        assertThat(resultDto.getPicture()).isEqualTo(dto.getPicture());
        assertThat(resultDto.getUpdatedAt()).isEqualTo(dto.getUpdatedAt());
        assertThat(resultDto.getGender()).isEqualTo(dto.getGender());
        assertThat(resultDto.getUsername()).isEqualTo(dto.getUsername());
        assertThat(resultDto.getUpdatedAt()).isEqualTo(dto.getUpdatedAt());

        assertThat(resultDto.getRoles()).containsSequence(dto.getRoles());
    }
}
