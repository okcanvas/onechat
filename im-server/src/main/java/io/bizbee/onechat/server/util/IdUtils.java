package io.bizbee.onechat.server.util;

import cn.hutool.core.lang.Snowflake;
import io.bizbee.onechat.common.cache.AppCache;
import io.bizbee.onechat.common.core.utils.SpringContextHolder;

public final class IdUtils {

  private static Snowflake snowflake = new Snowflake(0, 0);

  private static AppCache cache = SpringContextHolder.getBean(AppCache.class);

  public static String generatorId() {
    String key = "id-" + IdUtils.class.getSimpleName();
    Long aLong = cache.incr(key);
    return aLong + snowflake.nextIdStr();
  }

}
