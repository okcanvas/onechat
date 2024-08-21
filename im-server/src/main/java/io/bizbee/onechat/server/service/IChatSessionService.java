package io.bizbee.onechat.server.service;

import io.bizbee.onechat.common.core.utils.Result;
import io.bizbee.onechat.server.common.vo.user.ChatSessionAddReq;
import io.bizbee.onechat.server.common.vo.user.ChatSessionInfoResp;
import io.bizbee.onechat.server.common.vo.user.ChatSessionUpdateReq;

import java.util.Set;

/**
 * @author okcanvas
 * @date 2024-08-21
 */
public interface IChatSessionService {

  boolean save(Long userId, ChatSessionAddReq vo);

  Result<Set<ChatSessionInfoResp>> list();

  boolean del(ChatSessionUpdateReq vo);
}
