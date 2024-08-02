package com.lzf.bibackend.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    void userRegister() {
        String userAccount = "test";
        String userPassword = "test";
        String checkPassword = "123456";
        try{
            long result = userService.userRegister(userAccount, userPassword, checkPassword);
            assertTrue(result > 0);
        }catch(Exception e){

        }
    }

    @Test
    void userLogin() {
    }
}