package com.mb.service.impl;

import com.mb.service.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Random;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceImplTest {

    @Autowired
    private UserService userService;

    @Test
    public void increasePoints_notExistingUser() {
        final long userId = new Random().nextLong(), point = 2;
        userService.increasePoints(userId, point);

        final long result = userService.getPoints(userId);
        Assert.assertEquals("Expected different point value", point, result);
    }

    @Test
    public void increasePoints_existingUser() {
        final long userId = new Random().nextLong(), point = 2;
        userService.increasePoints(userId, point);
        userService.increasePoints(userId, point);

        final long result = userService.getPoints(userId);
        Assert.assertEquals("Expected different point value", point * 2, result);
    }

    @Test
    public void getPoints_notExistingUser() {
        final long userId = new Random().nextLong();
        final long result = userService.getPoints(userId);
        Assert.assertEquals("Expected zero points for non existing user", 0, result);
    }

    @Test
    public void getPoints_existingUser() {
        final long userId = new Random().nextLong(), point = 2;
        userService.increasePoints(userId, point);

        final long result = userService.getPoints(userId);
        Assert.assertEquals("Expected different point value", point, result);
    }
}
