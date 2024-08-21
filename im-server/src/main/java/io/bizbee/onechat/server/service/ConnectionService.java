package io.bizbee.onechat.server.service;

import io.bizbee.onechat.common.core.enums.NetProtocolEnum;
import io.bizbee.onechat.server.common.vo.connector.NodeInfoResp;

import java.util.List;

/**
 * @author XDD
 * @project onechat
 * @date 2021/7/24
 * @description Good Good Study,Day Day Up.
 */
public interface ConnectionService {

  List<NodeInfoResp> nodeList();

  NodeInfoResp node(NetProtocolEnum netProtocolEnum, Long identify);
}
