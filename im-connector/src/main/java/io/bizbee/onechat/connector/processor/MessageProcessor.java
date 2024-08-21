package io.bizbee.onechat.connector.processor;

import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.exceptions.JWTVerificationException;
import io.netty.channel.ChannelHandlerContext;
import io.bizbee.onechat.common.core.contant.AppConst;
import io.bizbee.onechat.common.core.utils.JwtUtil;
import io.bizbee.onechat.connector.utils.SendMessageUtils;

public interface MessageProcessor {

  void process(ChannelHandlerContext ctx, Object data);

  default Long parseUserId(ChannelHandlerContext ctx, String token) {
    if (StrUtil.isEmpty(token)) {
      SendMessageUtils.sendError(ctx, "not login");
      throw new IllegalArgumentException("not login");
    }

    try {
      JwtUtil.checkSign(token, AppConst.ACCESS_TOKEN_SECRET);
    } catch (JWTVerificationException e) {
      SendMessageUtils.sendError(ctx, "token invalidate");
      throw new IllegalArgumentException("token invalidate");
    }

    try {
      return JwtUtil.getUserId(token);
    } catch (Exception e) {
      SendMessageUtils.sendError(ctx, "token mistake");
      throw new IllegalArgumentException("token mistake");
    }
  }

}
