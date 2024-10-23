package org.springframe.backend.controller;

import org.springframe.backend.domain.dto.LoginDTO;
import org.springframe.backend.domain.dto.UserDTO;
import org.springframe.backend.domain.dto.UserRegisterDTO;
import org.springframe.backend.domain.entity.User;
import org.springframe.backend.service.UserService;
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
    private UserService userService;




//    @PostMapping
//    public ResponseResult<User> add(@Validated @RequestBody UserDTO userDTO) {
//        User userAdd = userService.save(userDTO);
//        String token = userService.login(userDTO);
//
//        return ResponseResult.Success(userAdd,token);
//    }

//    @PostMapping("/login")
//    public ResponseResult login(@Validated @RequestBody UserDTO userDTO) {
//
//        String token = userService.login(userDTO);
//        return ResponseResult.Success(null,token);
//    }
    @PostMapping("/user/register")
    public ResponseResult<Void> register(@Validated @RequestBody UserRegisterDTO userRegisterDTO) {
        return userService.userRegister(userRegisterDTO);
    }


//    @PostMapping("/login")
//    public ResponseResult login(@RequestBody LoginDTO loginDTO) {
//        return userService.userLogin();
//    }

}
