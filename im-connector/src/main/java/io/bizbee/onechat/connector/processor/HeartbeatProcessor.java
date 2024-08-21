package io.bizbee.onechat.connector.processor;

import com.alibaba.fastjson.JSONObject;
import io.netty.channel.ChannelHandlerContext;
import io.bizbee.onechat.common.core.enums.IMCmdType;
import io.bizbee.onechat.common.core.model.IMSendInfo;
import io.bizbee.onechat.common.core.model.LoginInfo;
import io.bizbee.onechat.common.core.utils.SpringContextHolder;
import io.bizbee.onechat.connector.listener.event.UserEvent;
import io.bizbee.onechat.connector.utils.SendMessageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class HeartbeatProcessor implements MessageProcessor {

  @Override
  public void process(ChannelHandlerContext ctx, Object obj) {
    SendMessageUtils.send(ctx, IMSendInfo.create(IMCmdType.HEART_BEAT));
  }

}
