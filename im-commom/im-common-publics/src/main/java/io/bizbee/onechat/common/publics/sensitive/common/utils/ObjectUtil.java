package io.bizbee.onechat.common.publics.sensitive.common.utils;

public class ObjectUtil {

  public static <V> boolean isNull(Object values) {
    return values == null;
  }

  public static boolean isNotNull(Object obj) {
    return !isNull(obj);
  }
}
