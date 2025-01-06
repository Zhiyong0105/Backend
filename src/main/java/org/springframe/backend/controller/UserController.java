package org.springframe.backend.controller;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframe.backend.constants.RedisConst;
import org.springframe.backend.domain.dto.LoginDTO;
import org.springframe.backend.domain.dto.UserDTO;
import org.springframe.backend.domain.dto.UserRegisterDTO;
import org.springframe.backend.domain.entity.User;
import org.springframe.backend.domain.vo.AuthorizeVO;
import org.springframe.backend.service.impl.CustomOAuth2UserService;
import org.springframe.backend.service.impl.UserServiceImpl;
import org.springframe.backend.utils.RedisCache;
import org.springframe.backend.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
//@RequestMapping("/user")
@Tag(name = "User Management", description = "Operations related to user management")
public class UserController {
    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private CustomOAuth2UserService customOAuth2UserService;


    @ApiResponse(responseCode = "200", description = "Successful operation")

    @PostMapping("/user/register")
    public ResponseResult<Void> register(@Validated @RequestBody UserRegisterDTO userRegisterDTO) {
        return userService.userRegister(userRegisterDTO);
    }

    @GetMapping("/user/github/verifyToken")
    public ResponseResult<?> verifyGithubToken(HttpServletRequest request, HttpServletResponse response) {
        return customOAuth2UserService.verifyGithubToken(request,response);

    }

    public ResponseResult<?> getUserInfo() {
        return null;
    }









}
