package io.bizbee.onechat.connector.task.handler;

import io.netty.channel.ChannelHandlerContext;
import io.bizbee.onechat.common.cache.AppCache;
import io.bizbee.onechat.common.core.contant.RedisKey;
import io.bizbee.onechat.common.core.enums.IMCmdType;
import io.bizbee.onechat.common.core.enums.IMSendCode;
import io.bizbee.onechat.common.core.model.IMSendInfo;
import io.bizbee.onechat.common.core.model.PrivateMessageInfo;
import io.bizbee.onechat.common.core.model.SendResult;
import io.bizbee.onechat.connector.remote.netty.UserChannelCtxMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PrivateMessageHandler implements MessageHandler<PrivateMessageInfo> {

  @Autowired
  private AppCache appCache;

  @Override
  public void handler(PrivateMessageInfo messageInfo) {
    Long recvId = messageInfo.getRecvId();
    log.info("message sender:{}, receiver:{}content:{}", messageInfo.getSendId(), recvId,
        messageInfo.getContent());
    IMSendCode code = null;
    try {
      ChannelHandlerContext channelCtx = UserChannelCtxMap.getChannelCtx(recvId);
      if (channelCtx != null && channelCtx.channel().isOpen()) {
        // 
        IMSendInfo<Object> imSendInfo = IMSendInfo.builder()
            .cmd(IMCmdType.PRIVATE_MESSAGE.code())
            .data(messageInfo)
            .build();
        channelCtx.channel().writeAndFlush(imSendInfo);
        // 
        code = IMSendCode.SUCCESS;
      } else {
        // 
        code = IMSendCode.NOT_FIND_CHANNEL;
        log.error("message sender:{}, receiver:{}content:{}", messageInfo.getSendId(), recvId,
            messageInfo.getContent());
      }
    } catch (Exception e) {
      // 
      code = IMSendCode.UNKONW_ERROR;
      log.error("message sender:{}, receiver:{}content:{}", messageInfo.getSendId(), recvId,
          messageInfo.getContent(), e);
    }
    SendResult<Object> sendResult = SendResult.builder().recvId(recvId).messageInfo(messageInfo)
        .code(code).build();
    appCache.listPush(RedisKey.IM_RESULT_PRIVATE_QUEUE, sendResult);
  }

}
