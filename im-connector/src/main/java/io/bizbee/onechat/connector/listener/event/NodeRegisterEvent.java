package io.bizbee.onechat.connector.listener.event;

import io.bizbee.onechat.common.core.enums.NetProtocolEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description:
 * @author okcanvas
 * @date 2024-08-21
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NodeRegisterEvent {

  /**
   * 协议类型
   */
  private NetProtocolEnum netProtocolEnum;

  /**
   * 端口
   */
  private Integer port;

  /**
   * 注册时间
   */
  private Long registerTime;

}
