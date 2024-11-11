package org.springframe.backend.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
public class RedisCache {
    @Autowired
    public RedisTemplate redisTemplate;


    public Long increment(String key, long delta) {
        return redisTemplate.opsForValue().increment(key, delta);
    }
    public <T> void setCacheObject(final String key, final T  value) {
        redisTemplate.opsForValue().set(key, value);
    }

   public <T> void setCacheObject(final String key,final T value, final Integer timeout, final TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
   }

   public boolean expire(final String key, final long timeout) {
        return redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
   }

   public boolean expire(final String key, final long timeout, final TimeUnit timeUnit) {
        return redisTemplate.expire(key, timeout, timeUnit);
   }

   public <T> T getCacheObject(final String key) {
            ValueOperations<String, T> ops = redisTemplate.opsForValue();
            return ops.get(key);

   }

   public boolean deleteObject(final String key) {
        return redisTemplate.delete(key);
   }

   public long deleleteObject(final Collection collection) {
        return redisTemplate.delete(collection);
   }

   public <T> long setCacheList(final String key, final List<T> list) {
        Long count = redisTemplate.opsForList().rightPushAll(key, list);
        return count==null?0:count;
   }

   public <T> List<T> getCacheList(final String key) {
        return redisTemplate.opsForList().range(key, 0, -1);
   }

   public <T> BoundSetOperations<String, T> setCacheSet(final String key, final Set<T> set) {
        BoundSetOperations<String, T> ops = redisTemplate.boundSetOps(key);
        Iterator<T> iterator = set.iterator();
        while (iterator.hasNext()) {
            ops.add(iterator.next());
        }
        return ops;
   }

   public <T> Set<T> getCacheSet(final String key) {
        return redisTemplate.opsForSet().members(key);
   }

   public <T> void setCacheMap(final String key, final Map<String, T> map) {
        if (map != null) {
            redisTemplate.opsForHash().putAll(key, map);
        }
   }

   public <T> Map<String, T> getCacheMap(final String key) {
        return redisTemplate.opsForHash().entries(key);
   }

   public <T> void setCacheMapValue(final String key, final String hkey, final T value) {
        redisTemplate.opsForHash().put(key,hkey,value);
   }

   public <T> T getCacheMapValue(final String key, final String hkey) {
        HashOperations<String, String, T> ops = redisTemplate.opsForHash();
        return ops.get(key,hkey);
   }

   public void incrementCacheMapValue(String key, String hkey,int value){
        redisTemplate.opsForHash().increment(key,hkey,value);
   }

   public void delCacheMapValue(final String key, final String hkey) {
        HashOperations ops = redisTemplate.opsForHash();
        ops.delete(key,hkey);

   }

   public <T> List<T> getMulCacheMapValue(final String key, final Collection<Object> hkeys) {
        return redisTemplate.opsForHash().multiGet(key, hkeys);
   }

   public Collection<String> keys(final String pattern) {
        return redisTemplate.keys(pattern);
   }

   public boolean isHasKey(final String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
   }
}
