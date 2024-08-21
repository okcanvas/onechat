package io.bizbee.onechat.sdk;

import io.bizbee.onechat.common.core.model.GroupMessageInfo;
import io.bizbee.onechat.common.core.model.PrivateMessageInfo;
import io.bizbee.onechat.sdk.sender.IMSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class IMClient {

  @Autowired
  private IMSender imSender;

  public void sendPrivateMessage(Long recvId, PrivateMessageInfo... messageInfo) {
    imSender.sendPrivateMessage(recvId, messageInfo);
  }

  public void sendGroupMessage(List<Long> recvIds, GroupMessageInfo... messageInfo) {
    imSender.sendGroupMessage(recvIds, messageInfo);
  }

}
