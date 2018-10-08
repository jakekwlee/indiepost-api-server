package com.indiepost.migration;

import com.indiepost.NewIndiepostApplication;
import com.indiepost.enums.Types;
import com.indiepost.model.User;
import com.indiepost.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = NewIndiepostApplication.class)
@WebAppConfiguration
@Transactional
public class UserRoleMigration {
    @Inject
    private UserRepository repository;

    @Test
    @Rollback(false)
    public void migrateUserRoles() {
        List<User> userList = repository.findAll(PageRequest.of(0, Integer.MAX_VALUE));
        // warning! this code produces n + 1 select problem
        for (User user : userList) {
            Types.UserRole role = user.getHighestRole();
            if (user.getRoleType() == null || !user.getRoleType().equals(role)) {
                user.setRoleType(role);
            }
        }
    }
}
