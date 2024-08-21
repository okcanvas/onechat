package io.bizbee.onechat.common.core.model;

import io.bizbee.onechat.common.core.enums.IMSendCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SendResult<T> {

  private Long recvId;
  private IMSendCode code;
  private T messageInfo;

}
