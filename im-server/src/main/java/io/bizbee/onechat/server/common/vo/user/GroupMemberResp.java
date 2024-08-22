package io.bizbee.onechat.server.common.vo.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("")
public class GroupMemberResp {

  @ApiModelProperty("")
  private Long userId;

  @ApiModelProperty("")
  private String aliasName;

  @ApiModelProperty("")
  private String headImage;

  @ApiModelProperty("")
  private Boolean quit;

  @ApiModelProperty("")
  private String remark;

  @ApiModelProperty(value = "")
  private String ipAddress;

  private Boolean onlineState;

}
