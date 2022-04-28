package com.by.commons.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Redis service
 * @author by.
 */
@SuppressWarnings(value = "rawtype")
@Service
public class RedisService<K,V> {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 基础一对一K-V存储服务
     * @param key
     * @param value
     * @param livetime 在多长时间（暂定为分钟）后清除
     */
    public void setBasicData(K key, V value, int livetime){
        redisTemplate.opsForValue().set(key,value,livetime,TimeUnit.MINUTES);
    }
    /**
     * 高级K-V存储：设置指定过期时间
     */
    public void setAdvanceData(K key, V value, Date expiredTime){
        redisTemplate.opsForValue().set(key,value,expiredTime.getTime()-System.currentTimeMillis(),TimeUnit.MILLISECONDS);
    }
    /**
     * 根据Key值获取对应Value
     */
    public V getBasicData(K key){
        try{
            return (V)redisTemplate.opsForValue().get(key);
        }catch (Exception e){
            return null;
        }
    }
    /**
     * 根据K值更新V值
     */
    public void updateValue(K key,V value){
        redisTemplate.opsForValue().setIfPresent(key,value);
    }
    /**
     * 移除K-V
     */
    public void remove(K key){
        redisTemplate.delete(key);
    }
    /**
     * 更新基础内容
     */
    public void updateBasicData(K key, V value){
        long expireTime = redisTemplate.opsForValue().getOperations().getExpire(key,TimeUnit.MILLISECONDS);
        redisTemplate.opsForValue().set(key, value,expireTime,TimeUnit.MILLISECONDS);
    }

    /**
     * 数据专用设置方法
     */
    public void updateNumberValue(K key, int value){
        if(value<0){
            redisTemplate.opsForValue().decrement(key);
        }
        else if(value>0) {
            redisTemplate.opsForValue().increment(key);
        }

    }
}
