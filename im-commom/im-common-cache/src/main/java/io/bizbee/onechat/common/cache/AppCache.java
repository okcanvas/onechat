package io.bizbee.onechat.common.cache;

import org.springframework.data.redis.core.ZSetOperations;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public interface AppCache {

  /**
   * Get an item from the cache, nontransactionally
   *
   * @param key 
   * @return the cached object or <tt>null</tt>
   */
  Object get(Object key);

  /**
   * Get an item from the cache, nontransactionally
   *
   * @param key
   * @return the cached object or <tt>null</tt>
   */
  String getString(Object key);

  /**
   * multiGet
   *
   * @param keys
   * @return
   */
  List multiGet(Collection keys);

  /**
   * 
   *
   * @param map 
   */
  void multiSet(Map map);

  /**
   * 
   *
   * @param keys 
   */
  void multiDel(Collection keys);

  /**
   * 
   *
   * @param key  
   * @param value 
   */
  void put(Object key, Object value);

  /**
   * 
   *
   * @param key   
   * @param value 
   * @param exp   
   */
  void put(Object key, Object value, Long exp);

  /**
   * 
   *
   * @param key      
   * @param value    
   * @param exp      
   * @param timeUnit 
   */
  void put(Object key, Object value, Long exp, TimeUnit timeUnit);

  /**
   * 
   *
   * @param key 
   */
  Boolean remove(Object key);

  /**
   * 
   *
   * @param key 
   */
  void vagueDel(Object key);

  /**
   * Clear the cache
   */
  void clear();

  /**
   *
   *
   * @param key       
   * @param hashKey   
   * @param hashValue 
   */
  void putHash(Object key, Object hashKey, Object hashValue);

  /**
   *
   *
   * @param key 
   * @param map 
   */
  void putAllHash(Object key, Map map);

  /**
   * 
   *
   * @param key     
   * @param hashKey 
   * @return 
   */
  Object getHash(Object key, Object hashKey);

  /**
   * 
   *
   * @param key 
   * @return 
   */
  Map<Object, Object> getHash(Object key);

  /**
   * 
   *
   * @param key 
   * @return 
   */
  boolean hasKey(Object key);

  /**
   * 
   *
   * @param pattern 
   * @return 
   */
  List<Object> keys(String pattern);

  /**
   *  
   *
   * @param pattern 
   * @return 
   */
  List<Object> keysBlock(String pattern);

  /**
   * 
   *
   * @param key   
   * @param value 
   * @return 
   */
  Long cumulative(Object key, Object value);

  /**
   * 
   
   *
   * @param key 
   * @return 
   */
  Long counter(Object key);

  /**
   * 
   *
   * @param keys 
   * @return 
   */
  List multiCounter(Collection keys);

  /**
   * 
   
   *
   * @param key 
   * @return 
   */
  Long mergeCounter(Object... key);
  // --------------------------------------------------------------------------------------------


  // -----------------------------------------------redis---------------------------------------------
  /**
   * 
   *
   * @param key      
   * @param liveTime 
   * @return 
   */
  Long incr(String key, long liveTime);

  /**
   * 
   *
   * @param key 
   * @return 
   */
  Long incr(String key);
  // -----------------------------------------------redis---------------------------------------------

  /**
   * 
   *
   * @param sortedSetName 
   * @param keyword       
   */
  void incrementScore(String sortedSetName, String keyword);

  /**
   * 
   *
   * @param sortedSetName 
   * @param keyword       
   * @param score         
   */
  void incrementScore(String sortedSetName, String keyword, Integer score);

  /**
   * 
   *
   * @param sortedSetName 
   * @param start         
   * @param end           
   * @return 
   */
  Set<ZSetOperations.TypedTuple<Object>> reverseRangeWithScores(String sortedSetName, Integer start, Integer end);

  /**
   * 
   *
   * @param sortedSetName 
   * @param count         
   * @return 
   */
  Set<ZSetOperations.TypedTuple<Object>> reverseRangeWithScores(String sortedSetName, Integer count);

  /**
   * 
   *
   * @param key   
   * @param score 
   * @param value 
   * @return 
   */
  boolean zAdd(String key, long score, String value);

  /**
   * 
   *
   * @param key  
   * @param from 
   * @param to   
   * @return 
   */
  Set<ZSetOperations.TypedTuple<Object>> zRangeByScore(String key, int from, long to);

  /**
   * 
   *
   * @param key   key
   * @param value
   * @return
   */
  Long zRemove(String key, String... value);

  /**
   * 
   *
   * @param key
   * @param length
   * @return
   */
  List<Object> listPop(String key, int length);

  /**
   * 
   *
   * @param key
   * @param obj
   * @return
   */
  boolean listPush(String key, Object obj);
}
