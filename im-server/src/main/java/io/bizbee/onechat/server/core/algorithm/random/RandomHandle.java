package io.bizbee.onechat.server.core.algorithm.random;

import io.bizbee.onechat.common.core.enums.NetProtocolEnum;
import io.bizbee.onechat.server.core.NodeContainer;
import io.bizbee.onechat.server.core.algorithm.RouteHandle;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Function: 路由策略， 随机
 */
public class RandomHandle implements RouteHandle {

  @Override
  public NodeContainer.WNode routeServer(NetProtocolEnum protocolEnum,
      List<NodeContainer.WNode> values, String key) {
    int size = values.size();
    if (size == 0) {
      throw new RuntimeException();
    }
    int offset = ThreadLocalRandom.current().nextInt(size);

    return values.get(offset);
  }

}
