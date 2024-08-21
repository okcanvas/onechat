package io.bizbee.onechat.server.tcp.listener.message;

import io.bizbee.onechat.common.core.enums.IMListenerType;
import io.bizbee.onechat.common.core.enums.IMSendCode;
import io.bizbee.onechat.common.core.enums.MessageType;
import io.bizbee.onechat.common.core.model.GroupMessageInfo;
import io.bizbee.onechat.common.core.model.SendResult;
import io.bizbee.onechat.sdk.annotation.IMListener;
import io.bizbee.onechat.sdk.listener.MessageListener;
import io.bizbee.onechat.server.common.contant.RedisKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

@Slf4j
@IMListener(type = IMListenerType.GROUP_MESSAGE)
public class GroupMessageListener implements MessageListener {

  @Autowired
  private RedisTemplate<String, Object> redisTemplate;

  @Override
  public void process(SendResult result) {
    GroupMessageInfo messageInfo = (GroupMessageInfo) result.getMessageInfo();
    if (messageInfo.getType().equals(MessageType.TIP.code())) {
      // 提示类数据不记录
      return;
    }

    // 保存该用户已拉取的最大消息id
    if (result.getCode().equals(IMSendCode.SUCCESS)) {
      String key =
          RedisKey.IM_GROUP_READED_POSITION + messageInfo.getGroupId() + ":" + result.getRecvId();
      redisTemplate.opsForValue().set(key, messageInfo.getId());
    }
  }

}
