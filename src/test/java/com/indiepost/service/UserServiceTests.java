package com.indiepost.service;

import com.indiepost.NewIndiepostApplication;
import com.indiepost.model.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

/**
 * Created by jake on 17. 11. 13.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NewIndiepostApplication.class)
@WebAppConfiguration
public class UserServiceTests {

    @Autowired
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
}
