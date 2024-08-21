package io.bizbee.onechat.connector.listener;

import io.bizbee.onechat.common.cache.AppCache;
import io.bizbee.onechat.common.core.contant.RedisKey;
import io.bizbee.onechat.common.core.enums.IMCmdType;
import io.bizbee.onechat.common.core.model.IMSendInfo;
import io.bizbee.onechat.connector.listener.event.UserEvent;
import io.bizbee.onechat.connector.remote.IMServerGroup;
import io.bizbee.onechat.connector.utils.SendMessageUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


@Component
@Slf4j
public class UserOnlineStateListener {

  @Resource
  private AppCache appCache;

  @SneakyThrows
  @EventListener(classes = UserEvent.class)
  public void onApplicationEvent(UserEvent event) {

    if (UserEvent.Event.ONLINE.equals(event.getEvent())) {
      String key = RedisKey.IM_USER_SERVER_ID + event.getUserId();
      appCache.put(key, IMServerGroup.serverId);

      SendMessageUtils.send(event.getCtx(), IMSendInfo.create(IMCmdType.LOGIN, "success"));
    } else if (UserEvent.Event.OFFLINE.equals(event.getEvent())) {
      String key = RedisKey.IM_USER_SERVER_ID + event.getUserId();
      appCache.remove(key);
    }

  }

}
