package com.indiepost.repository;

import com.indiepost.NewIndiepostApplicationKt;
import com.indiepost.enums.Types;
import com.indiepost.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

import static com.indiepost.testHelper.JsonSerializer.printToJson;
import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = NewIndiepostApplicationKt.class)
@WebAppConfiguration
@Transactional
public class UserRepositoryTests {
    @Inject
    private UserRepository repository;

    @Test
    public void getTotalUsers_shouldReturnTotalUserCountProperly() {
        Long totalUsers = repository.getTotalUsers();
        assertThat(totalUsers).isNotNull().isGreaterThanOrEqualTo(50L);
        System.out.println(totalUsers);
    }

    @Test
    public void getTotalUsers_withUserRoleArgs_shouldReturnTotalUserCountProperly() {
        Long totalUsers = repository.getTotalUsers(Types.UserRole.Editor);
        assertThat(totalUsers).isNotNull().isLessThan(20L);
        System.out.println(totalUsers);
    }

    @Test
    public void getTotalUsers_withDateArgs_shouldReturnTotalUserCountProperly() {
        Long totalUsers = repository.getTotalUsers(LocalDateTime.now().minusDays(7), LocalDateTime.now());
        assertThat(totalUsers).isNotNull().isGreaterThanOrEqualTo(0);
        System.out.println(totalUsers);
    }

    @Test
    public void findByUserRole_shouldReturnUserListProperly() {
        Types.UserRole role = Types.UserRole.User;
        int size = 20;
        List<User> userList = repository.findByUserRole(role, PageRequest.of(0, size));
        assertThat(userList).isNotNull().hasSize(20);
    }

    @Test
    public void search_shouldReturnUserListProperly() {
        Types.UserRole role = Types.UserRole.User;
        int size = 20;
        List<User> userList = repository.search("Lee", role, PageRequest.of(0, size));
        assertThat(userList).isNotNull().hasAtLeastOneElementOfType(User.class);
        assertThat(userList.size()).isLessThan(size);
        printToJson(userList);
    }

    @Test
    public void searchTotal_shouldReturnSearchTotalElementsProperly() {
        Types.UserRole role = Types.UserRole.User;
        String text = "Lee";
        Long count = repository.searchTotal(text, role);
        assertThat(count).isNotNull().isGreaterThanOrEqualTo(1);

        int size = 20;
        List<User> userList = repository.search(text, role, PageRequest.of(0, size));
        assertThat(count).isEqualTo(userList.size());
    }

}
