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
import org.springframework.security.access.AccessDeniedException;
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

    public void onAccessDeny(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException exception
    ){
        WebUtil.renderString(response,ResponseResult.Fail().asJsonString());
    }


    public void onUnAuthenticated(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception
    )throws IOException{
        WebUtil.renderString(response,ResponseResult.Fail(ResponseEnum.NOT_LOGIN.getCode(), ResponseEnum.NOT_LOGIN.getMsg()).asJsonString());
    }

    public void onLogoutSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ){
            boolean invalidateJwt = jwtUtils.invaildateJwt(request.getHeader("Authorization"));
            if (invalidateJwt){
                WebUtil.renderString(response,ResponseResult.logoutSuccess().asJsonString());
                return;
            }
            WebUtil.renderString(response,ResponseResult.error(ResponseEnum.LOGOUT_FAIL.getCode(),ResponseEnum.LOGOUT_FAIL.getMsg()).asJsonString());
    }
}
