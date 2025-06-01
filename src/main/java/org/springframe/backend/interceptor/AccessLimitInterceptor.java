package org.springframe.backend.interceptor;

import io.lettuce.core.RedisConnectionException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframe.backend.annotation.AccessLimit;
import org.springframe.backend.constants.RedisConst;
import org.springframe.backend.enums.BlackListPolicy;
import org.springframe.backend.repository.BlackListRepository;
import org.springframe.backend.service.impl.BlackListServiceImpl;
import org.springframe.backend.utils.*;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class AccessLimitInterceptor implements HandlerInterceptor {
    private final RedisCache redisCache;
    private final BlackListRepository blackListRepository;
    private final BlackListServiceImpl blackListService;

    private static final String EXPIRE_TIME_KEY_PREFIX = "expire_time_";

    @Override
    public boolean preHandle(@NotNull HttpServletRequest request,@NotNull HttpServletResponse response,@NotNull Object handler) throws Exception {
        boolean result = true;

        if(handler instanceof HandlerMethod handlerMethod) {
            AccessLimit accessLimit = handlerMethod.getMethodAnnotation(AccessLimit.class);
            if(accessLimit == null) {
                return result;
            }
            int seconds = accessLimit.seconds();
            int maxCount = accessLimit.maxCount();

            String ip = IpUtils.getIpAddr(request);
            String method = request.getMethod();
            String uri = request.getRequestURI();
            String key = "limit:" + method + ":" + uri + ":" + ip;
            try{
                String expiredTimeKey = EXPIRE_TIME_KEY_PREFIX + key;

                Long expireTime = redisCache.getCacheObject(expiredTimeKey);

                Long count = redisCache.increment(key,1L);

                if(count == 1){
                    redisCache.expire(expiredTimeKey,seconds, TimeUnit.SECONDS);
                    expireTime = System.currentTimeMillis();
                    redisCache.setCacheObject(expiredTimeKey,expireTime);
                }
                if(isBlocked(response,ip,uri,count,expireTime)){
                    return false;
                }
                if(count > maxCount){
                    WebUtil.renderString(response, ResponseResult.Fail().asJsonString());
                    log.warn("User IP[" + ip +"]");
                    result = false;
                }
            }catch (RedisConnectionException e){
                log.error(e.getMessage(),e);
                result = false;
            }
        }
        return result;
    }
    private Boolean isBlocked(HttpServletResponse response, String ip, String uri,Long count,Long expireTime){
        Long timestampByIP = redisCache.getCacheMapValue(RedisConst.BLACK_LIST_IP_KEY,ip);
        Long timestampByUID = redisCache.getCacheMapValue(RedisConst.BLACK_LIST_UID_KEY,String.valueOf(SecurityUtils.getUserId()));


        for(BlackListPolicy policy : BlackListPolicy.values()){
            if (count == policy.getRequestThreshold()){
                return true;
            }
        }
        return false;
    }
    private boolean handleBlackList(Long expireTime, Long timestampByIP,String message,String reason,HttpServletResponse response){
        return true;
    }
}
