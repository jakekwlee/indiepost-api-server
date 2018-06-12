package com.indiepost.mapper;

import com.indiepost.dto.UserDto;
import com.indiepost.model.User;
import org.junit.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Arrays;

import static com.indiepost.mapper.UserMapper.userDtoToUser;
import static com.indiepost.utils.DateUtil.localDateTimeToInstant;
import static org.assertj.core.api.Assertions.assertThat;

public class UserMapperUnitTests {

    @Test
    public void userDtoToUser_shouldWorkProperly() {
        UserDto dto = new UserDto();

        dto.setUsername("auth0|5a88547af5c8213cb27caf41");

        dto.setDisplayName("Test Name");
        dto.setEmail("sysadmin@indiepost.co.kr");
        dto.setGender("MALE");
        dto.setPicture("https://www.google.co.kr/images/branding/googlelogo/2x/googlelogo_color_272x92dp.png");
        Instant updated = localDateTimeToInstant(LocalDateTime.of(2018, 2, 10, 12, 0));
        dto.setUpdatedAt(updated);
        dto.setRoles(Arrays.asList("Administrator", "Editor", "User"));
        dto.setGender("MALE");
        User user = userDtoToUser(dto);

        assertThat(user).isNotNull();
        assertThat(user.getEmail()).isEqualTo(dto.getEmail());
        assertThat(user.getDisplayName()).isEqualTo(dto.getDisplayName());
        assertThat(user.getPicture()).isEqualTo(dto.getPicture());
        assertThat(localDateTimeToInstant(user.getUpdatedAt())).isEqualTo(dto.getUpdatedAt());
        assertThat(user.getGender().toString()).isEqualTo(dto.getGender());
        assertThat(user.getUsername()).isEqualTo(dto.getUsername());
        assertThat(localDateTimeToInstant(user.getUpdatedAt())).isEqualTo(dto.getUpdatedAt());

        assertThat(user.getRoles().size()).isZero();
    }

}
