package org.springframe.backend.utils;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

@Component
public class JwtUtils {
    @Value("${spring.security.jwt.key}$")
    private String key;

    @Value("${spring.security.jwt.expire}")
    private int expire;






    public String createJwt(String uuid, UserDetails userDetails, Integer id, String username) {
        Algorithm algorithm = Algorithm.HMAC256(key);
        Date expire = expireTime();
        Date now = new Date();

        return JWT.create()
                .withJWTId(uuid)
                .withClaim("id", id)
                .withClaim("username", username)
                .withExpiresAt(expire)
                .withIssuedAt(now)
                .sign(algorithm);
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
    public DecodedJWT getJwt(String token) {
        try {
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(key)).build();
            return jwtVerifier.verify(token);
        } catch (JWTVerificationException e) {
            e.printStackTrace();
            return null;
        }
    }


    public Date expireTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, expire * 24);
        return calendar.getTime();
    }




}