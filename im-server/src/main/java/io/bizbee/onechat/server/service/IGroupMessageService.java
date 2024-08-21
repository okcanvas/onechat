package io.bizbee.onechat.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.bizbee.onechat.common.core.model.GroupMessageInfo;
import io.bizbee.onechat.server.common.entity.GroupMessage;
import io.bizbee.onechat.server.common.vo.message.GroupMessageSendReq;
import io.bizbee.onechat.server.common.vo.message.MessageSendResp;

import java.util.List;

public interface IGroupMessageService extends IService<GroupMessage> {

  MessageSendResp sendMessage(GroupMessageSendReq vo);

  void recallMessage(Long id);

  void pullUnreadMessage();

  List<GroupMessageInfo> findHistoryMessage(Long groupId, Long lastMessageId);
}
