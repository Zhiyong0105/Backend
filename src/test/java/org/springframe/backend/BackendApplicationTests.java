package org.springframe.backend;

import org.junit.jupiter.api.Test;
import org.springframe.backend.domain.entity.User;
import org.springframe.backend.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class BackendApplicationTests {
    @Autowired
    private UserServiceImpl userService;
    @Test
    public void testUserById(){
        boolean isExit = userService.userIsExist("string1","string1@gmail.com");
        assertTrue(isExit);
    }

}
