package io.bizbee.onechat.connector.processor;

import com.alibaba.fastjson.JSONObject;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import io.bizbee.onechat.common.core.enums.IMCmdType;
import io.bizbee.onechat.common.core.model.IMSendInfo;
import io.bizbee.onechat.common.core.model.LoginInfo;
import io.bizbee.onechat.common.core.utils.SpringContextHolder;
import io.bizbee.onechat.connector.contant.ConnectorConst;
import io.bizbee.onechat.connector.listener.event.UserEvent;
import io.bizbee.onechat.connector.remote.netty.UserChannelCtxMap;
import io.bizbee.onechat.connector.utils.SendMessageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LoginProcessor implements MessageProcessor {

  @Override
  synchronized public void process(ChannelHandlerContext ctx, Object obj) {
    LoginInfo loginInfo = JSONObject.parseObject(JSONObject.toJSONString(obj), LoginInfo.class);

    Long userId = parseUserId(ctx, loginInfo.getToken());

    log.info("userId:{}", userId);
    ChannelHandlerContext context = UserChannelCtxMap.getChannelCtx(userId);
    if (context != null) {
      SendMessageUtils.send(context, IMSendInfo.create(IMCmdType.FORCE_LOGUT, "logout"));
      SendMessageUtils.close(context);
    }

    UserChannelCtxMap.addChannelCtx(userId, ctx);
    ctx.channel().attr(AttributeKey.valueOf(ConnectorConst.USER_ID)).set(userId);
    ctx.channel().attr(AttributeKey.valueOf(ConnectorConst.HEARTBEAT_TIMES)).set(0L);
    SpringContextHolder.sendEvent(UserEvent.buildOnlineEvent(userId, ctx));
  }
}
