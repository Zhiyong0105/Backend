package org.springframe.backend.controller;

import org.springframe.backend.domain.dto.LoginDTO;
import org.springframe.backend.domain.dto.UserDTO;
import org.springframe.backend.domain.dto.UserRegisterDTO;
import org.springframe.backend.domain.entity.User;
import org.springframe.backend.service.impl.UserServiceImpl;
import org.springframe.backend.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@RequestMapping("/user")

public class UserController {
    @Autowired
    private UserServiceImpl userService;


    
    @PostMapping("/user/register")
    public ResponseResult<Void> register(@Validated @RequestBody UserRegisterDTO userRegisterDTO) {
        return userService.userRegister(userRegisterDTO);
    }




}
