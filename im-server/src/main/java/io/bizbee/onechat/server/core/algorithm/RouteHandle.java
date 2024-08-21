package io.bizbee.onechat.server.core.algorithm;

import io.bizbee.onechat.common.core.enums.NetProtocolEnum;
import io.bizbee.onechat.server.core.NodeContainer;

import java.util.List;

/**
 * Function:
 */
public interface RouteHandle {

  /**
   * 在一批服务器里进行路由
   *
   * @param values
   * @param key
   * @return
   */
  NodeContainer.WNode routeServer(NetProtocolEnum protocolEnum, List<NodeContainer.WNode> values,
      String key);
}
