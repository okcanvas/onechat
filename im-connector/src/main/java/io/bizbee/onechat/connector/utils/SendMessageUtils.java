package io.bizbee.onechat.connector.utils;

import io.netty.channel.ChannelHandlerContext;
import io.bizbee.onechat.common.core.enums.IMCmdType;
import io.bizbee.onechat.common.core.model.IMSendInfo;

public final class SendMessageUtils {

  public static boolean sendError(ChannelHandlerContext ctx, String errorInfo) {
    return send(ctx, IMSendInfo.builder().cmd(IMCmdType.ERROR.code()).data(errorInfo).build());
  }

  public static boolean send(ChannelHandlerContext ctx, IMSendInfo msg) {
    if (ctx == null || msg == null || !ctx.channel().isOpen()) {
      return false;
    }
    ctx.channel().writeAndFlush(msg);
    return true;
  }

  public static void close(ChannelHandlerContext ctx) {
    ctx.close();
  }

}
