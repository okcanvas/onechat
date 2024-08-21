package io.bizbee.onechat.server.common.dto;

import io.bizbee.onechat.common.core.enums.ChatType;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author okcanvas
 * @date 2024-08-21
 */
@Data
public class ChatSessionInfoDto implements Serializable {

  @NotNull(message = "对方id不能为空")
  private Long targetId;

  @NotNull(message = "聊天类型不能为空")
  private ChatType chatType;

  private Long createTime;

}
