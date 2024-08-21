package io.bizbee.onechat.connector.task.handler;

import io.netty.channel.ChannelHandlerContext;
import io.bizbee.onechat.common.cache.AppCache;
import io.bizbee.onechat.common.core.contant.RedisKey;
import io.bizbee.onechat.common.core.enums.IMCmdType;
import io.bizbee.onechat.common.core.enums.IMSendCode;
import io.bizbee.onechat.common.core.model.GroupMessageInfo;
import io.bizbee.onechat.common.core.model.IMSendInfo;
import io.bizbee.onechat.common.core.model.SendResult;
import io.bizbee.onechat.connector.remote.netty.UserChannelCtxMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class GroupMessageHandler implements MessageHandler<GroupMessageInfo> {

  @Autowired
  private AppCache appCache;

  @Override
  public void handler(GroupMessageInfo messageInfo) {
    List<Long> recvIds = messageInfo.getRecvIds();
    log.info("message sender:{}, group:{}, receiver:{}content:{}",
        messageInfo.getSendId(),
        messageInfo.getGroupId(),
        recvIds,
        messageInfo.getContent());
    for (Long recvId : recvIds) {
      IMSendCode code = null;
      try {
        ChannelHandlerContext channelCtx = UserChannelCtxMap.getChannelCtx(recvId);
        if (channelCtx != null && channelCtx.channel().isOpen()) {
          // 
          IMSendInfo<Object> sendInfo = IMSendInfo.builder()
              .cmd(IMCmdType.GROUP_MESSAGE.code())
              .data(messageInfo)
              .build();
          channelCtx.channel().writeAndFlush(sendInfo);
          code = IMSendCode.SUCCESS;
        } else {
          // 
          code = IMSendCode.NOT_FIND_CHANNEL;
          log.error("message sender:{}, group:{}, receiver:{}content:{}",
              messageInfo.getSendId(),
              messageInfo.getGroupId(),
              recvIds,
              messageInfo.getContent());
        }
      } catch (Exception e) {
        // 
        code = IMSendCode.UNKONW_ERROR;
        log.error("message sender:{}, group:{}, receiver:{}content:{}",
            messageInfo.getSendId(),
            messageInfo.getGroupId(),
            recvIds,
            messageInfo.getContent());
      }
      // 
      SendResult<Object> sendResult = SendResult.builder()
          .code(code)
          .recvId(recvId)
          .messageInfo(messageInfo)
          .build();
      appCache.listPush(RedisKey.IM_RESULT_GROUP_QUEUE, sendResult);
    }
  }
}
