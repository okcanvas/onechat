package io.bizbee.onechat.admin.dto;

import lombok.Data;

import java.util.List;

@Data
public class ServerInfoVo {

  private Integer serverCount;

  private List<ServerConnectionInfoItemVo> items;

}
