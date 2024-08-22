package io.bizbee.onechat.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.bizbee.onechat.common.core.contant.AppConst;
import io.bizbee.onechat.common.core.contant.RedisKey;
import io.bizbee.onechat.common.core.enums.MessageStatus;
import io.bizbee.onechat.common.core.enums.MessageType;
import io.bizbee.onechat.common.core.enums.ResultCode;
import io.bizbee.onechat.common.core.model.PrivateMessageInfo;
import io.bizbee.onechat.sdk.IMClient;
import io.bizbee.onechat.server.adapter.SensitiveWordAdapter;
import io.bizbee.onechat.server.common.entity.PrivateMessage;
import io.bizbee.onechat.server.common.vo.message.MessageSendResp;
import io.bizbee.onechat.server.common.vo.message.PrivateMessageSendReq;
import io.bizbee.onechat.server.exception.GlobalException;
import io.bizbee.onechat.server.mapper.PrivateMessageMapper;
import io.bizbee.onechat.server.service.IFriendService;
import io.bizbee.onechat.server.service.IPrivateMessageService;
import io.bizbee.onechat.server.util.BeanUtils;
import io.bizbee.onechat.server.util.SessionContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PrivateMessageServiceImpl extends
    ServiceImpl<PrivateMessageMapper, PrivateMessage> implements IPrivateMessageService {

  @Autowired
  private IFriendService friendService;
  @Autowired
  private RedisTemplate<String, Object> redisTemplate;
  @Autowired
  private IMClient imClient;

  private final static Integer defaultQueryMessageCount = 15;

  @Autowired
  private SensitiveWordAdapter sensitiveWordAdapter;

  @Override
  public MessageSendResp sendMessage(PrivateMessageSendReq vo) {
    Long userId = SessionContext.getSession().getId();
    Boolean isFriends = friendService.isFriend(userId, vo.getRecvId());
    if (!isFriends) {
      throw new GlobalException(ResultCode.PROGRAM_ERROR, "친구를 찾을 수 없음");
    }
    String replaced = sensitiveWordAdapter.replace(vo.getContent());
    if (replaced.matches("^\\*+$")) {
      throw new GlobalException("보낼수 없는 메세지");
    }
    vo.setContent(replaced);

    PrivateMessage msg = BeanUtils.copyProperties(vo, PrivateMessage.class);
    msg.setSendId(userId);
    msg.setStatus(MessageStatus.UNREAD.code());
    msg.setSendTime(new Date());
    this.save(msg);

    PrivateMessageInfo msgInfo = BeanUtils.copyProperties(msg, PrivateMessageInfo.class);
    imClient.sendPrivateMessage(vo.getRecvId(), msgInfo);
    log.info("sendMessage id:{} friendId:{} content:{}", userId, vo.getRecvId(), vo.getContent());
    return MessageSendResp.builder().id(msg.getId()).content(msg.getContent()).build();
  }

  @Override
  public void recallMessage(Long id) {
    Long userId = SessionContext.getSession().getId();
    PrivateMessage msg = this.getById(id);
    if (msg == null) {
      throw new GlobalException(ResultCode.PROGRAM_ERROR, "");
    }
    if (msg.getSendId() != userId) {
      throw new GlobalException(ResultCode.PROGRAM_ERROR, "");
    }
    if (System.currentTimeMillis() - msg.getSendTime().getTime()
        > AppConst.ALLOW_RECALL_SECOND * 1000) {
      throw new GlobalException(ResultCode.PROGRAM_ERROR, "");
    }
    msg.setStatus(MessageStatus.RECALL.code());
    this.updateById(msg);

    PrivateMessageInfo msgInfo = BeanUtils.copyProperties(msg, PrivateMessageInfo.class);
    msgInfo.setType(MessageType.TIP.code());
    msgInfo.setSendTime(new Date());
    msgInfo.setContent("취소됨");
    imClient.sendPrivateMessage(msgInfo.getRecvId(), msgInfo);
    log.info("recallMessage id:{}, friendId:{}，content:{}", msg.getSendId(), msg.getRecvId(), msg.getContent());
  }

  @Override
  public List<PrivateMessageInfo> findHistoryMessage(Long friendId, Long lastMessageId) {
    Long userId = SessionContext.getSession().getId();
    LambdaQueryWrapper<PrivateMessage> wrapper = new LambdaQueryWrapper<>();
    wrapper
        .and(
            wrap -> wrap.and(wp -> wp.eq(PrivateMessage::getSendId, userId)
                    .eq(PrivateMessage::getRecvId, friendId))
                .or(wp -> wp.eq(PrivateMessage::getRecvId, userId)
                    .eq(PrivateMessage::getSendId, friendId)))
        .ne(PrivateMessage::getStatus, MessageStatus.RECALL.code());

    if (lastMessageId != null) {
      wrapper.lt(PrivateMessage::getId, lastMessageId);
    }
    wrapper.orderByDesc(PrivateMessage::getId).last("limit " + defaultQueryMessageCount);

    List<PrivateMessage> messages = this.list(wrapper);
    List<PrivateMessageInfo> messageInfos = messages.stream().map(m -> {
      PrivateMessageInfo info = BeanUtils.copyProperties(m, PrivateMessageInfo.class);
      return info;
    }).collect(Collectors.toList());

    log.info("findHistoryMessage id:{} friendId:{} message:{}", userId, friendId, messageInfos.size());
    return messageInfos;
  }

  @Override
  public void pullUnreadMessage() {
    Long userId = SessionContext.getSession().getId();
    String key = RedisKey.IM_USER_SERVER_ID + userId;
    Integer serverId = (Integer) redisTemplate.opsForValue().get(key);
    if (serverId == null) {
      throw new GlobalException(ResultCode.PROGRAM_ERROR, "");
    }
    QueryWrapper<PrivateMessage> queryWrapper = new QueryWrapper<>();
    queryWrapper.lambda().eq(PrivateMessage::getRecvId, userId)
        .eq(PrivateMessage::getStatus, MessageStatus.UNREAD);
    List<PrivateMessage> messages = this.list(queryWrapper);
    if (!messages.isEmpty()) {
      List<PrivateMessageInfo> messageInfos = messages.stream().map(m -> {
        PrivateMessageInfo msgInfo = BeanUtils.copyProperties(m, PrivateMessageInfo.class);
        return msgInfo;
      }).collect(Collectors.toList());
      PrivateMessageInfo[] infoArr = messageInfos.toArray(
          new PrivateMessageInfo[messageInfos.size()]);
      imClient.sendPrivateMessage(userId, infoArr);
      log.info("pullUnreadMessage id:{},count:{}", userId, infoArr.length);
    }
  }
}
