package org.springframe.backend.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframe.backend.domain.entity.LoginUser;
import org.springframe.backend.domain.entity.User;
import org.springframe.backend.domain.vo.AuthorizeVO;
import org.springframe.backend.enums.ResponseEnum;
import org.springframe.backend.utils.JwtUtils;
import org.springframe.backend.utils.ResponseResult;
import org.springframe.backend.utils.WebUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.UUID;
@Slf4j
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

    public void onGithubAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException {
        log.info("Authentication success with user: {}", authentication.getPrincipal());
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String uuid = UUID.randomUUID().toString();
        Integer id = oAuth2User.getAttribute("id");
        String name = oAuth2User.getAttribute( "login");

//        String token = jwtUtils.createJwt(uuid,null, id, name);
//        String redirectUrl = "http://localhost:5173/auth-callback?uuid="+uuid;
//        response.sendRedirect(redirectUrl);
        String token = jwtUtils.createJwt(uuid,null,id,name);
        Cookie cookie = new Cookie("tempToken",token);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        response.addCookie(cookie);
        response.sendRedirect("http://localhost:5173/article");
    }
}
