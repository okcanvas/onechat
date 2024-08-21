package io.bizbee.onechat.connector.remote.netty;

import io.netty.channel.ChannelHandlerContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserChannelCtxMap {

  private static Map<Long, ChannelHandlerContext> channelMap = new ConcurrentHashMap();

  public static void addChannelCtx(Long userId, ChannelHandlerContext ctx) {
    channelMap.put(userId, ctx);
  }

  public static void removeChannelCtx(Long userId) {
    if (userId != null) {
      channelMap.remove(userId);
    }
  }

  public static ChannelHandlerContext getChannelCtx(Long userId) {
    if (userId == null) {
      return null;
    }
    return channelMap.get(userId);
  }

}
