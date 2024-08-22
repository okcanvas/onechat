package io.bizbee.onechat.server.common.vo.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@ApiModel("VO")
public class UserVO {

  @NotNull(message = "")
  @ApiModelProperty(value = "")
  private Long id;

  @NotEmpty(message = "")
  @Length(max = 64, message = "")
  @ApiModelProperty(value = "")
  private String userName;

  @NotEmpty(message = "")
  @Length(max = 64, message = "")
  @ApiModelProperty(value = "")
  private String nickName;

  @ApiModelProperty(value = "")
  private Integer sex;

  @Length(max = 64, message = "")
  @ApiModelProperty(value = "")
  private String signature;

  @ApiModelProperty(value = "")
  private String headImage;

  @ApiModelProperty(value = "")
  private String headImageThumb;

  @ApiModelProperty(value = "")
  private Boolean online;

  @ApiModelProperty(value = "")
  private Integer accountType;

  @ApiModelProperty(value = "")
  private String ipAddress;

}
