package io.bizbee.onechat.server.common.vo.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@ApiModel("VO")
public class GroupVO {

  @NotNull(message = "")
  @ApiModelProperty(value = "id")
  private Long id;

  @Length(max = 20, message = "")
  @NotEmpty(message = "")
  @ApiModelProperty(value = "")
  private String name;

  @NotNull(message = "")
  @ApiModelProperty(value = "")
  private Long ownerId;

  @ApiModelProperty(value = "")
  private String headImage;

  @ApiModelProperty(value = "")
  private String headImageThumb;

  @Length(max = 1024, message = "")
  @ApiModelProperty(value = "")
  private String notice;

  @Length(max = 20, message = "")
  @ApiModelProperty(value = "")
  private String aliasName;

  @Length(max = 20, message = "")

  private String remark;

  @ApiModelProperty(value = "0정상,1익명")
  private Integer groupType;

  /**
   * 成员数量
   */
  private Integer memberCount;
}
