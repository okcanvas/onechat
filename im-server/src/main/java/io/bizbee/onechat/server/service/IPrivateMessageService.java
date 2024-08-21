package io.bizbee.onechat.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.bizbee.onechat.common.core.model.PrivateMessageInfo;
import io.bizbee.onechat.server.common.entity.PrivateMessage;
import io.bizbee.onechat.server.common.vo.message.MessageSendResp;
import io.bizbee.onechat.server.common.vo.message.PrivateMessageSendReq;

import java.util.List;

public interface IPrivateMessageService extends IService<PrivateMessage> {

  MessageSendResp sendMessage(PrivateMessageSendReq vo);

  void recallMessage(Long id);

  List<PrivateMessageInfo> findHistoryMessage(Long friendId, Long lastMessageId);

  void pullUnreadMessage();

}
