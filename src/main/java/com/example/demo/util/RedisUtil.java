package com.example.demo.util;

import lombok.RequiredArgsConstructor;
import org.redisson.api.*;
import org.redisson.api.listener.MessageListener;
import org.redisson.client.codec.StringCodec;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Component
public class RedisUtil {

    private final RedissonClient redissonClient;

    // ===================== String / RBucket =====================
    public boolean set(String key, Object value) {
        try {
            RBucket<Object> bucket = redissonClient.getBucket(key);
            bucket.set(value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean set(String key, Object value, long timeSeconds) {
        try {
            RBucket<Object> bucket = redissonClient.getBucket(key);
            if (timeSeconds > 0) {
                bucket.set(value, timeSeconds, TimeUnit.SECONDS);
            } else {
                bucket.set(value);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Object get(String key) {
        RBucket<Object> bucket = redissonClient.getBucket(key);
        return bucket.get();
    }

    public boolean delete(String key) {
        return redissonClient.getBucket(key).delete();
    }

    public boolean expire(String key, long timeSeconds) {
        return redissonClient.getBucket(key).expire(timeSeconds, TimeUnit.SECONDS);
    }

    public long getExpire(String key) {
        long expire = redissonClient.getBucket(key).remainTimeToLive();
        return expire < 0 ? 0 : TimeUnit.MILLISECONDS.toSeconds(expire);
    }

    public boolean hasKey(String key) {
        return redissonClient.getKeys().countExists(key) > 0;
    }

    public long incr(String key, long delta) {
        return redissonClient.getAtomicLong(key).addAndGet(delta);
    }

    public long decr(String key, long delta) {
        return redissonClient.getAtomicLong(key).addAndGet(-delta);
    }

    // ===================== Hash / RMap =====================
    public boolean hset(String key, String field, Object value) {
        try {
            RMap<String, Object> map = redissonClient.getMap(key);
            map.put(field, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean hset(String key, Map<String, Object> map, long timeSeconds) {
        try {
            RMap<String, Object> rmap = redissonClient.getMap(key);
            rmap.putAll(map);
            if (timeSeconds > 0) {
                rmap.expire(timeSeconds, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Object hget(String key, String field) {
        RMap<String, Object> map = redissonClient.getMap(key);
        return map.get(field);
    }

    public Map<String, Object> hgetAll(String key) {
        RMap<String, Object> map = redissonClient.getMap(key);
        return map.readAllMap();
    }

    public boolean hdel(String key, String field) {
        return redissonClient.getMap(key).remove(field) != null;
    }

    public boolean hHasKey(String key, String field) {
        return redissonClient.getMap(key).containsKey(field);
    }

    // ===================== Set / RSet =====================
    public boolean sAdd(String key, Object... values) {
        RSet<Object> set = redissonClient.getSet(key);
        return set.addAll(Set.of(values));
    }

    public boolean sAdd(String key, long timeSeconds, Object... values) {
        RSet<Object> set = redissonClient.getSet(key);
        boolean added = set.addAll(Set.of(values));
        if (timeSeconds > 0) {
            set.expire(timeSeconds, TimeUnit.SECONDS);
        }
        return added;
    }

    public Set<Object> sGet(String key) {
        return redissonClient.getSet(key).readAll();
    }

    public boolean sHasKey(String key, Object value) {
        return redissonClient.getSet(key).contains(value);
    }

    public boolean sRemove(String key, Object... values) {
        RSet<Object> set = redissonClient.getSet(key);
        return set.removeAll(Set.of(values));
    }

    public long sSize(String key) {
        return redissonClient.getSet(key).size();
    }

    // ===================== List / RList =====================
    public boolean lSet(String key, Object value) {
        RList<Object> list = redissonClient.getList(key);
        return list.add(value);
    }

    public boolean lSet(String key, Collection<Object> values) {
        RList<Object> list = redissonClient.getList(key);
        return list.addAll(values);
    }

    public java.util.List<Object> lGet(String key, int start, int end) {
        RList<Object> list = redissonClient.getList(key);
        int size = list.size();
        start = Math.max(start, 0);
        end = Math.min(end, size - 1);
        return list.subList(start, end + 1);
    }

    public Object lGetIndex(String key, int index) {
        RList<Object> list = redissonClient.getList(key);
        return list.get(index);
    }

    public boolean lUpdateIndex(String key, int index, Object value) {
        RList<Object> list = redissonClient.getList(key);
        list.set(index, value);
        return true;
    }

    public long lRemove(String key, int count, Object value) {
        RList<Object> list = redissonClient.getList(key);
        long removed = 0;
        for (int i = 0; i < count; i++) {
            if (list.remove(value)) removed++;
        }
        return removed;
    }



}