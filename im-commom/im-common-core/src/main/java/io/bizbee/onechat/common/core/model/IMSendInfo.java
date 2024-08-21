package io.bizbee.onechat.common.core.model;

import io.bizbee.onechat.common.core.enums.IMCmdType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IMSendInfo<T> {

  private Integer cmd;
  private T data;

  public static IMSendInfo create(IMCmdType cmdType) {
    IMSendInfo sendInfo = new IMSendInfo();
    sendInfo.setCmd(cmdType.code());
    return sendInfo;
  }

  public static IMSendInfo create(IMCmdType cmdType, String msg) {
    IMSendInfo sendInfo = new IMSendInfo();
    sendInfo.setCmd(cmdType.code());
    sendInfo.setData(msg);
    return sendInfo;
  }

}
