package io.bizbee.onechat.connector.task;

import io.bizbee.onechat.common.cache.AppCache;
import io.bizbee.onechat.common.core.contant.RedisKey;
import io.bizbee.onechat.common.core.model.PrivateMessageInfo;
import io.bizbee.onechat.connector.remote.IMServerGroup;
import io.bizbee.onechat.connector.task.handler.PrivateMessageHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class PullUnreadPrivateMessageTask extends AbstractPullMessageTask {

  @Autowired
  private AppCache appCache;

  @Autowired
  private PrivateMessageHandler messageHandler;

  @Override
  public void pullMessage() {
    if (IMServerGroup.serverId < 0) {
      return;
    }
    String key = RedisKey.IM_UNREAD_PRIVATE_QUEUE + IMServerGroup.serverId;
    List<Object> messageInfos = appCache.listPop(key, ONES_PULL_MESSAGE_COUNT);
    for (Object o : messageInfos) {
      if (messageHandler != null) {
        PrivateMessageInfo recvInfo = (PrivateMessageInfo) o;
        messageHandler.handler(recvInfo);
      }
    }
  }

}
