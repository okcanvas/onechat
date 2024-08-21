package io.bizbee.onechat.common.core.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class PageReq implements Serializable {

  private Long pageNo;

  private Long pageSize;

}
