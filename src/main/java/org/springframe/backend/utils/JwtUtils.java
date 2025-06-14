package org.springframe.backend.utils;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframe.backend.constants.RedisConst;
import org.springframe.backend.domain.entity.LoginUser;
import org.springframe.backend.domain.entity.User;
import org.springframe.backend.domain.vo.AuthorizeVO;
import org.springframe.backend.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import java.util.concurrent.TimeUnit;
@Slf4j
@Component
public class JwtUtils {
    @Value("${spring.security.jwt.key}$")
    private String key;

    @Value("${spring.security.jwt.expire}")
    private int expire;

    @Resource
    private RedisCache redisCache;
    @Autowired
    private UserRepository userRepository;


    public boolean invaildateJwt(String headerToken){
        String token = this.convertToken(headerToken);
        if(token == null){ return false;}
        Algorithm algorithm = Algorithm.HMAC256(key);
        JWTVerifier verifier = JWT.require(algorithm).build();
        try{
            DecodedJWT jwt = verifier.verify(token);
            String id = jwt.getId();
            return  deleteToken(id);
        } catch (JWTVerificationException e) {
            return false;
        }
    }

    public String createJwt(String uuid, UserDetails userDetails, Integer id, String username) {
        Algorithm algorithm = Algorithm.HMAC256(key);
        Date expire = expireTime();
        Date now = new Date();

         String jwt =  JWT.create()
                .withJWTId(uuid)
                .withClaim("id", id)
                .withClaim("name", username)
                .withExpiresAt(expire)
                .withIssuedAt(now)
                .sign(algorithm);
        long durationMillis = expire.getTime() - now.getTime();
        int durationMinutes = (int) (durationMillis / 1000 / 60 ); // 转成分钟
        redisCache.setCacheObject(RedisConst.JWT_WHITE_LIST + uuid,jwt,durationMinutes, TimeUnit.SECONDS);
         log.info("过期时间{} min",durationMinutes);
         return jwt;
    }



    public boolean deleteToken(String uuid){
        if(this.isInvalidToken(uuid)){return false;}
        redisCache.deleteObject(RedisConst.JWT_WHITE_LIST + uuid);
        return true;
    }

    public boolean verifyJwt(String jwt) {
        try{
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(key))
                    .build();
            jwtVerifier.verify(jwt);
            return true;
        }catch (JWTVerificationException e){
            e.printStackTrace();
            return false;
        }
    }

    public DecodedJWT resolveJwt(String headerToken) {
        String token = this.convertToken(headerToken);
        if(token == null){
            return null;
        }
        Algorithm algorithm = Algorithm.HMAC256(key);
        JWTVerifier jwtVerifier = JWT.require(algorithm).build();
        try{
            DecodedJWT jwt = jwtVerifier.verify(token);
            if(this.isInvalidToken(jwt.getId())){
                return null;
            }
            Date expiresAt = jwt.getExpiresAt();
            return new Date().after(expiresAt) ? null : jwt;

        }catch (JWTVerificationException e){
            return null;
        }

    }


    public AuthorizeVO githubUserInfo(String token){
        Algorithm algorithm = Algorithm.HMAC256(key);
        JWTVerifier jwtVerifier = JWT.require(algorithm).build();
        DecodedJWT jwt = jwtVerifier.verify(token);
        Integer id = jwt.getClaim("id").asInt();
        User user = userRepository.findById(id).orElse(null);
        AuthorizeVO authorizeVO = new AuthorizeVO();
        BeanUtils.copyProperties(user,authorizeVO);
        authorizeVO.setToken(token);
        authorizeVO.setExpireTime(expireTime());
        authorizeVO.setUserId(toId(jwt).longValue());
         return  authorizeVO;

    }

    public UserDetails toUser(DecodedJWT jwt) {
        Map<String, Claim> claims = jwt.getClaims();


        return new LoginUser()
                .setUser
                        (
                                new User()
                                        .setUsername(claims.get("name").asString())
                                        .setId(toId(jwt))
                        )
                .setPermissions(null);

    }

    public Integer toId(DecodedJWT jwt) {
        Map<String, Claim> claims = jwt.getClaims();
        return claims.get("id").asInt();
    }


    public Date expireTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, expire * 100);
        return calendar.getTime();
    }

    private boolean isInvalidToken(String uuid){
        return !Boolean.TRUE.equals(redisCache.isHasKey(RedisConst.JWT_WHITE_LIST + uuid));
    }

    private String convertToken(String headerToken) {
        if (headerToken == null || !headerToken.startsWith("Bearer ")) {
            return null;
        }
        return headerToken.substring(7);
    }




}
