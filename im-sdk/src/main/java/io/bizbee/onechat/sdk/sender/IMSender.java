package io.bizbee.onechat.sdk.sender;

import cn.hutool.core.collection.CollUtil;
import io.bizbee.onechat.common.core.contant.RedisKey;
import io.bizbee.onechat.common.core.enums.IMListenerType;
import io.bizbee.onechat.common.core.enums.IMSendCode;
import io.bizbee.onechat.common.core.model.GroupMessageInfo;
import io.bizbee.onechat.common.core.model.PrivateMessageInfo;
import io.bizbee.onechat.common.core.model.SendResult;
import io.bizbee.onechat.sdk.listener.MessageListenerMulticaster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class IMSender {

  @Autowired
  private RedisTemplate redisTemplate;

  @Autowired
  private MessageListenerMulticaster listenerMulticaster;

  public void sendPrivateMessage(Long recvId, PrivateMessageInfo... messageInfos) {
    if (recvId == null || messageInfos == null || messageInfos.length < 1) {
      return;
    }

    String key = RedisKey.IM_USER_SERVER_ID + recvId;
    Integer serverId = (Integer) redisTemplate.opsForValue().get(key);
    if (serverId != null) {
      List<PrivateMessageInfo> recvInfos = Arrays.stream(messageInfos).map(e -> {
        e.setRecvId(recvId);
        return e;
      }).collect(Collectors.toList());
      redisTemplate.opsForList()
          .rightPushAll(RedisKey.IM_UNREAD_PRIVATE_QUEUE + serverId, recvInfos);
      // todo next
      return;
    }

    // 
    for (PrivateMessageInfo messageInfo : messageInfos) {
      SendResult<Object> sendResult = SendResult.builder()
          .messageInfo(messageInfo)
          .recvId(recvId)
          .code(IMSendCode.NOT_ONLINE)
          .build();
      listenerMulticaster.multicast(IMListenerType.PRIVATE_MESSAGE, sendResult);
    }
  }

  public void sendGroupMessage(List<Long> recvIds, GroupMessageInfo... messageInfos) {
    if (CollUtil.isEmpty(recvIds) || messageInfos == null || messageInfos.length < 1) {
      return;
    }
    // 
    List<Long> offLineIds = Collections.synchronizedList(new LinkedList<Long>());
    Map<Integer, List<Long>> serverMap = new ConcurrentHashMap<>();
    recvIds.parallelStream().forEach(id -> {
      String key = RedisKey.IM_USER_SERVER_ID + id;
      Integer serverId = (Integer) redisTemplate.opsForValue().get(key);
      if (serverId != null) {
        // 
        synchronized (serverMap) {
          if (serverMap.containsKey(serverId)) {
            serverMap.get(serverId).add(id);
          } else {
            List<Long> list = Collections.synchronizedList(new LinkedList<Long>());
            list.add(id);
            serverMap.put(serverId, list);
          }
        }
      } else {
        offLineIds.add(id);
      }
    });
    // 
    for (Map.Entry<Integer, List<Long>> entry : serverMap.entrySet()) {
      List<GroupMessageInfo> recvInfos = Arrays.stream(messageInfos).map(e -> {
        e.setRecvIds(entry.getValue());
        return e;
      }).collect(Collectors.toList());
      String key = RedisKey.IM_UNREAD_GROUP_QUEUE + entry.getKey();
      redisTemplate.opsForList().rightPushAll(key, recvInfos);
    }
    // 
    for (GroupMessageInfo messageInfo : messageInfos) {
      for (Long id : offLineIds) {
        SendResult<Object> sendResult = SendResult.builder()
            .messageInfo(messageInfo)
            .recvId(id)
            .code(IMSendCode.NOT_ONLINE)
            .build();
        listenerMulticaster.multicast(IMListenerType.GROUP_MESSAGE, sendResult);
      }
    }
  }
}
