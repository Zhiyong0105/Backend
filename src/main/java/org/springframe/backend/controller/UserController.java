package org.springframe.backend.controller;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframe.backend.domain.dto.UserDTO;
import org.springframe.backend.domain.dto.UserRegisterDTO;
import org.springframe.backend.domain.dto.UserUpdateDTO;
import org.springframe.backend.domain.vo.UserListVO;
import org.springframe.backend.service.impl.CustomOAuth2UserService;
import org.springframe.backend.service.impl.UserServiceImpl;
import org.springframe.backend.utils.ControllerUtils;
import org.springframe.backend.utils.ResponseResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(name = "User Management", description = "Operations related to user management")
public class UserController {

    private final UserServiceImpl userService;

    private final CustomOAuth2UserService customOAuth2UserService;



    @ApiResponse(responseCode = "200", description = "Successful operation")
    @PostMapping("/user/register")
    public ResponseResult<Void> register(@Validated @RequestBody UserRegisterDTO userRegisterDTO) {
        return userService.userRegister(userRegisterDTO);
    }

    @GetMapping("/user/github/verifyToken")
    public ResponseResult<?> verifyGithubToken(HttpServletRequest request, HttpServletResponse response) {
        return customOAuth2UserService.verifyGithubToken(request,response);

    }
    @PutMapping("/user/auth/update/profile")
    public ResponseResult<?> updateInfo(@Validated @RequestBody UserUpdateDTO userUpdateDTO) {

        return userService.userUpdate(userUpdateDTO);
    }

    @PutMapping("/user/auth/resetPassword")
    public ResponseResult<?> resetPassword(@Validated @RequestBody UserDTO userDTO) {
        return null;
    }



    @GetMapping("/admin/auth/get/users")
    public ResponseResult<List<UserListVO>> getUsers(@RequestParam("ids") List<Integer> ids){
        return ControllerUtils.messageHandler(()->userService.getUsers(ids));
    }









}
