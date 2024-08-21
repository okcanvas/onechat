package io.bizbee.onechat.server.tcp.listener.zk;

import io.bizbee.onechat.common.core.enums.NetProtocolEnum;
import io.bizbee.onechat.server.core.NodeContainer;

import java.util.Collection;

public interface INodeUpdateNodeEventListener {

  default void delete(NetProtocolEnum netProtocolEnum, NodeContainer.WNode node) {

  }

  default void add(NetProtocolEnum netProtocolEnum, Collection<NodeContainer.WNode> node) {

  }

  default void list(NetProtocolEnum netProtocolEnum, Collection<NodeContainer.WNode> nodes) {

  }

}
