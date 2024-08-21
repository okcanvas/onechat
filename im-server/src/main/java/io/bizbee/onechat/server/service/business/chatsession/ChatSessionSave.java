package io.bizbee.onechat.server.service.business.chatsession;

import io.bizbee.onechat.server.common.dto.ChatSessionInfoDto;

import java.util.Set;

/**
 * @author okcanvas
 * @date 2024-08-21
 */
public interface ChatSessionSave {

  boolean add(Long userId, ChatSessionInfoDto vo);

  Set<ChatSessionInfoDto> list(Long userId);

  boolean del(Long userId, ChatSessionInfoDto vo);
}
