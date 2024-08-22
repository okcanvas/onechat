package io.bizbee.onechat.common.cache.impl;

import io.bizbee.onechat.common.cache.AppCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@Slf4j
public class RedisCacheImpl implements AppCache {

  private RedisTemplate<Object, Object> redisTemplate;

  public RedisCacheImpl(RedisTemplate redisTemplate) {
    this.redisTemplate = redisTemplate;
  }

  @Override
  public Object get(Object key) {

    return redisTemplate.opsForValue().get(key);
  }

  @Override
  public String getString(Object key) {
    try {
      return redisTemplate.opsForValue().get(key).toString();
    } catch (Exception e) {
      return null;
    }
  }

  @Override
  public List multiGet(Collection keys) {
    return redisTemplate.opsForValue().multiGet(keys);

  }

  @Override
  public void multiSet(Map map) {
    redisTemplate.opsForValue().multiSet(map);
  }

  @Override
  public void multiDel(Collection keys) {
    redisTemplate.delete(keys);
  }

  @Override
  public void put(Object key, Object value) {
    redisTemplate.opsForValue().set(key, value);
  }

  @Override
  public void put(Object key, Object value, Long exp) {
    put(key, value, exp, TimeUnit.SECONDS);
  }

  @Override
  public void put(Object key, Object value, Long exp, TimeUnit timeUnit) {
    redisTemplate.opsForValue().set(key, value, exp, timeUnit);
  }

  @Override
  public Boolean remove(Object key) {

    return redisTemplate.delete(key);
  }

  @Override
  public void vagueDel(Object key) {
    List keys = this.keys(key + "*");
    redisTemplate.delete(keys);
  }

  @Override
  public void clear() {
    List keys = this.keys("*");
    redisTemplate.delete(keys);
  }

  @Override
  public void putHash(Object key, Object hashKey, Object hashValue) {
    redisTemplate.opsForHash().put(key, hashKey, hashValue);
  }

  @Override
  public void putAllHash(Object key, Map map) {
    redisTemplate.opsForHash().putAll(key, map);
  }

  @Override
  public Object getHash(Object key, Object hashKey) {
    return redisTemplate.opsForHash().get(key, hashKey);
  }

  @Override
  public Map<Object, Object> getHash(Object key) {
    return this.redisTemplate.opsForHash().entries(key);
  }

  @Override
  public boolean hasKey(Object key) {
    return this.redisTemplate.opsForValue().get(key) != null;
  }

  @Override
  public List<Object> keys(String pattern) {
    List<Object> keys = new ArrayList<>();
    this.scan(pattern, item -> {
      String key = new String(item, StandardCharsets.UTF_8);
      keys.add(key);
    });
    return keys;
  }

  @Override
  public List<Object> keysBlock(String pattern) {
    Set<Object> set = redisTemplate.keys(pattern);
    List<Object> list = new ArrayList<>();
    list.addAll(set);
    return list;
  }

  private void scan(String pattern, Consumer<byte[]> consumer) {
    this.redisTemplate.execute((RedisConnection connection) -> {
      try (Cursor<byte[]> cursor = connection
          .scan(ScanOptions.scanOptions().count(Long.MAX_VALUE).match(pattern).build())) {
        cursor.forEachRemaining(consumer);
        return null;

      } catch (Exception e) {
        log.error("scan错误", e);
        throw new RuntimeException(e);
      }
    });
  }

  @Override
  public Long cumulative(Object key, Object value) {
    HyperLogLogOperations<Object, Object> operations = redisTemplate.opsForHyperLogLog();
    return operations.add(key, value);

  }

  @Override
  public Long counter(Object key) {
    HyperLogLogOperations<Object, Object> operations = redisTemplate.opsForHyperLogLog();

    return operations.size(key);
  }

  @Override
  public List multiCounter(Collection keys) {
    if (keys == null) {
      return new ArrayList();
    }
    List<Long> result = new ArrayList<>();
    for (Object key : keys) {
      result.add(counter(key));
    }
    return result;
  }

  @Override
  public Long mergeCounter(Object... key) {
    HyperLogLogOperations<Object, Object> operations = redisTemplate.opsForHyperLogLog();
    return operations.union(key[0], key);
  }

  @Override
  public Long incr(String key, long liveTime) {
    RedisAtomicLong entityIdCounter = new RedisAtomicLong(key,
        redisTemplate.getConnectionFactory());
    Long increment = entityIdCounter.getAndIncrement();
    if (increment == 0 && liveTime > 0) {
      entityIdCounter.expire(liveTime, TimeUnit.SECONDS);
    }

    return increment;
  }

  @Override
  public Long incr(String key) {
    RedisAtomicLong entityIdCounter = new RedisAtomicLong(key,
        redisTemplate.getConnectionFactory());
    return entityIdCounter.getAndIncrement();
  }

  @Override
  public void incrementScore(String sortedSetName, String keyword) {
    redisTemplate.opsForZSet().incrementScore(sortedSetName, keyword, 1);
  }

  @Override
  public void incrementScore(String sortedSetName, String keyword, Integer score) {
    redisTemplate.opsForZSet().incrementScore(sortedSetName, keyword, score);
  }

  @Override
  public Set<ZSetOperations.TypedTuple<Object>> reverseRangeWithScores(String sortedSetName,
      Integer start,
      Integer end) {
    return this.redisTemplate.opsForZSet().reverseRangeWithScores(sortedSetName, start, end);
  }

  @Override
  public Set<ZSetOperations.TypedTuple<Object>> reverseRangeWithScores(String sortedSetName,
      Integer count) {
    return this.redisTemplate.opsForZSet().reverseRangeWithScores(sortedSetName, 0, count);
  }

  @Override
  public boolean zAdd(String key, long score, String value) {
    return redisTemplate.opsForZSet().add(key, value, score);

  }

  @Override
  public Set<ZSetOperations.TypedTuple<Object>> zRangeByScore(String key, int from, long to) {
    Set<ZSetOperations.TypedTuple<Object>> set = redisTemplate.opsForZSet()
        .rangeByScoreWithScores(key, from, to);
    return set;
  }

  @Override
  public Long zRemove(String key, String... value) {
    return redisTemplate.opsForZSet().remove(key, value);
  }

  @Override
  public List<Object> listPop(String key, int length) {
    List<Object> result = new ArrayList<>();
    for (int i = 0; i < length; i++) {
      Object value = redisTemplate.opsForList().leftPop(key);
      if (value != null) {
        result.add(value);
      } else {
        break; 
      }
    }
    return result;
  }
  /*
  java 9 이상에서 동작
  public List<Object> listPop(String key, int length) {
    return redisTemplate.opsForList().leftPop(key, length);
  }
  */




  @Override
  public boolean listPush(String key, Object obj) {
    Long aLong = redisTemplate.opsForList().rightPush(key, obj);
    return aLong != null && aLong > 0;
  }
}
