package io.bizbee.onechat.common.core.model;

import lombok.Data;

import java.util.List;

@Data
public class IMRecvInfo<T> {

  private Integer cmd;
  private List<Long> recvIds;
  private T data;
}
