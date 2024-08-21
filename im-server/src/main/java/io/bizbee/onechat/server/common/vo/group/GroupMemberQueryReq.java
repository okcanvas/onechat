package io.bizbee.onechat.server.common.vo.group;

import io.bizbee.onechat.common.core.model.PageReq;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author okcanvas
 * @date 2024-08-21
 */
@Data
public class GroupMemberQueryReq extends PageReq {

  @NotNull(message = "群聊id不能为空")
  private Long groupId;

  private String search;
}
