package io.bizbee.onechat.server.common.vo.user;

import io.bizbee.onechat.common.core.enums.ChatType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author okcanvas
 * @date 2024-08-21
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ChatSessionAddReq implements Serializable {

  @NotNull(message = "对方id不能为空")
  private Long targetId;

  @NotNull(message = "聊天类型不能为空")
  private ChatType chatType;

}
