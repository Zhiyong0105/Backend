package org.springframe.backend.service.impl;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframe.backend.constants.RedisConst;
import org.springframe.backend.domain.entity.User;
import org.springframe.backend.domain.vo.AuthorizeVO;
import org.springframe.backend.enums.ResponseEnum;
import org.springframe.backend.repository.UserRepository;
import org.springframe.backend.utils.JwtUtils;
import org.springframe.backend.utils.RedisCache;
import org.springframe.backend.utils.ResponseResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Map;
@Slf4j
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;
    private final RedisCache redisCache;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;


    public CustomOAuth2UserService(UserRepository userRepository, RedisCache redisCache, PasswordEncoder passwordEncoder, JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.redisCache = redisCache;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = new DefaultOAuth2UserService().loadUser(userRequest);

        Map<String, Object> attributes = oAuth2User.getAttributes();
        Integer userId = (Integer) attributes.get("id");

        String name  = (String) attributes.get("login");
        User user = userRepository.findById(userId).orElseGet(()->createrUser(userId,name));

        return new DefaultOAuth2User(
                oAuth2User.getAuthorities(),
                attributes,
                "login"
        );
    }


    private User createrUser(Integer id,String name){
        String encodePassword = passwordEncoder.encode(id.toString());
        String email = "github_" + id + "@gmail.com";
        User user = User.builder()
                .email(email)
                .password(encodePassword)
                .username(name)
                .id(id)
                .build();
//        log.info("{}",user);
        userRepository.save(user);
        return user;

    }

    public ResponseResult<?> verifyGithubToken(HttpServletRequest request, HttpServletResponse response){
        String token = Arrays.stream(request.getCookies())
                .filter(cookie -> "tempToken".equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
//        log.info("token:{}",token);
        if(jwtUtils.verifyJwt(token)){
            AuthorizeVO authorizeVO = jwtUtils.githubUserInfo(token);

            Cookie clearedCookie = new Cookie("tempToken", null);
            clearedCookie.setPath("/"); // 确保路径与原始 cookie 匹配
            clearedCookie.setHttpOnly(true);
            clearedCookie.setMaxAge(0); // 设置过期时间为 0 即为删除
            response.addCookie(clearedCookie);

            return ResponseResult.Success(authorizeVO);
        }
        return ResponseResult.error(ResponseEnum.FAIL);

    }










}
