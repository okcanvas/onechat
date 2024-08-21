package io.bizbee.onechat.common.core.model;

import lombok.Data;

import java.util.List;

@Data
public class GroupMessageInfo extends CommonMessageInfo {

  private Long groupId;
  private List<Long> recvIds;

}
