package io.bizbee.onechat.server.common.vo.message;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@ApiModel("私聊消息VO")
public class PrivateMessageSendReq {

  @NotNull(message = "接收用户id不可为空")
  @ApiModelProperty(value = "接收用户id")
  private Long recvId;

  @Length(max = 1024, message = "内容长度不得大于1024")
  @NotEmpty(message = "发送内容不可为空")
  @ApiModelProperty(value = "发送内容")
  private String content;

  @NotNull(message = "消息类型不可为空")
  @ApiModelProperty(value = "消息类型")
  private Integer type;

}
