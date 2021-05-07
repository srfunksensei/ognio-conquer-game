package com.mb.service.impl;

import com.mb.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Random;

@SpringBootTest
public class UserServiceImplTest {

    @Autowired
    private UserService userService;

    @Test
    public void increasePoints_notExistingUser() {
        final long userId = new Random().nextLong(), point = 2;
        userService.increasePoints(userId, point);

        final long result = userService.getPoints(userId);
        Assertions.assertEquals(point, result, "Expected different point value");
    }

    @Test
    public void increasePoints_existingUser() {
        final long userId = new Random().nextLong(), point = 2;
        userService.increasePoints(userId, point);
        userService.increasePoints(userId, point);

        final long result = userService.getPoints(userId);
        Assertions.assertEquals(point * 2, result, "Expected different point value");
    }

    @Test
    public void getPoints_notExistingUser() {
        final long userId = new Random().nextLong();
        final long result = userService.getPoints(userId);
        Assertions.assertEquals(0, result, "Expected zero points for non existing user");
    }

    @Test
    public void getPoints_existingUser() {
        final long userId = new Random().nextLong(), point = 2;
        userService.increasePoints(userId, point);

        final long result = userService.getPoints(userId);
        Assertions.assertEquals(point, result, "Expected different point value");
    }
}
