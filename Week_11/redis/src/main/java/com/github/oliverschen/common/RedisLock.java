package com.github.oliverschen.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author ck
 */
@Slf4j
@Component
public class RedisLock<K,V> {


    @Resource
    private RedisTemplate<K, V> redisTemplate;

    private K lockKey;

    private static final Long UNLOCK_SUCCESS = 1L;
    private static final String RELEASE_LOCK_LUA_SCRIPT = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";


    /**
     * redis 锁
     * @param key 加锁 key
     * @param expireTime 加锁时间
     * @param waitTime 等待时间
     */
    public String lock(K key,Long expireTime, Long waitTime) {
        this.lockKey = key;
        // 唯一ID，防止发生释放锁异常
        String lockId = UUID.randomUUID().toString().replace("-", "");
        // 超过 waitTime 还没有获取到就返回失败
        long endTime = System.currentTimeMillis() / 1000 + waitTime;
        while (System.currentTimeMillis() / 1000 < endTime) {
            Boolean result = redisTemplate.opsForValue().setIfAbsent(lockKey, (V) lockId, expireTime, TimeUnit.SECONDS);
            if (result != null && result) {
                return lockId;
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }
        return null;
    }

    /**
     * 解锁
     * @param lockId 加锁时返回的 key
     */
    public boolean unlock(String lockId) {
        if(lockId == null){
            return false;
        }
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>(RELEASE_LOCK_LUA_SCRIPT,Long.class);
        try {
            // 参数一：redisScript，参数二：key列表，参数三：arg（可多个）
            Long result = redisTemplate.execute(redisScript, Collections.singletonList(lockKey),lockId);
            log.info("release lock success, lockId :{}, result:{}", lockId, result);
            if (UNLOCK_SUCCESS.equals(result)) {
                return true;
            }
        }catch (Exception e){
            log.error("unlock due to error",e);
        }
        return false;
    }

}
