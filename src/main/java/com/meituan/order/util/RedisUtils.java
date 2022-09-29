package com.meituan.order.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtils {
    @Autowired
    private RedisTemplate redisTemplate;


    public boolean expire(String key, long time) {
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public Long getExpire(String key) {
        try {
            return redisTemplate.getExpire(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public Object get(String key) {
        try {
           return redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Set<String> keys(String patten) {
        try {
            return redisTemplate.keys(patten);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean flushdb() {
        try {
            redisTemplate.getConnectionFactory().getConnection().flushDb();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


}
