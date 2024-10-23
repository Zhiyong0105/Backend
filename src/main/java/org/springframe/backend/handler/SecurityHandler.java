package org.springframe.backend.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframe.backend.domain.entity.LoginUser;
import org.springframe.backend.domain.vo.AuthorizeVO;
import org.springframe.backend.enums.ResponseEnum;
import org.springframe.backend.utils.JwtUtils;
import org.springframe.backend.utils.ResponseResult;
import org.springframe.backend.utils.WebUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import java.io.IOException;
import java.util.UUID;
@Component
public class SecurityHandler {
    @Autowired
    private JwtUtils jwtUtils;


    public void onAuthenticationSuccess(
             HttpServletRequest request,
             HttpServletResponse response,
             Authentication authentication
    ) {
        handlerOnAuthenticationSuccess(request,response,(LoginUser) authentication.getPrincipal());
    }
    public void handlerOnAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            LoginUser loginUser
    ){
        Integer id = loginUser.getUser().getId();
        String name = loginUser.getUser().getUsername();
        String uuid = UUID.randomUUID().toString();


        String token = jwtUtils.createJwt(uuid,loginUser,id,name);

        AuthorizeVO authorizeVO = new AuthorizeVO();
        authorizeVO.setToken(token);
        authorizeVO.setExpireTime(jwtUtils.expireTime());


        ResponseResult responseResult = new ResponseResult();

        WebUtil.renderString(response, responseResult.loginSuccess(authorizeVO).asJsonString());

    }

    public void onAuthenticationFailure(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception
    )throws IOException {
        WebUtil.renderString(response,ResponseResult.LoginError(ResponseEnum.LOGIN_FAIL.getCode(), null,ResponseEnum.LOGIN_FAIL.getMsg()).asJsonString());
    }
}
