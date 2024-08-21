package io.bizbee.onechat.server.controller;

import io.bizbee.onechat.common.core.enums.NetProtocolEnum;
import io.bizbee.onechat.common.core.enums.ResultCode;
import io.bizbee.onechat.common.core.utils.Result;
import io.bizbee.onechat.common.core.utils.ResultUtils;
import io.bizbee.onechat.common.log.annotation.ApiLog;
import io.bizbee.onechat.server.common.vo.connector.NodeInfoResp;
import io.bizbee.onechat.server.service.ConnectionService;
import io.bizbee.onechat.server.util.SessionContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author okcanvas
 * @date 2024-08-21
 */
@Api(tags = "连接信息")
@RestController
@RequestMapping("/connector")
public class ConnectionController {

  @Autowired
  private ConnectionService connectionService;

  @ApiOperation("查看所有在线的长连接机器")
  @GetMapping("/list")
  public Result<List<NodeInfoResp>> conneList() {
    List<NodeInfoResp> nodeInfoVoList = connectionService.nodeList();
    return ResultUtils.success(nodeInfoVoList);
  }

  @ApiLog
  @ApiOperation("选择一台可用的长连接")
  @GetMapping("/node")
  public Result<NodeInfoResp> node(NetProtocolEnum protocol) {
    if (protocol == null) {
      protocol = NetProtocolEnum.WS;
    }
    NodeInfoResp nodeInfoVo = connectionService.node(protocol, SessionContext.getUserIdIfExist());
    if (nodeInfoVo == null) {
      return ResultUtils.error(ResultCode.NO_AVAILABLE_SERVICES);
    }
    return ResultUtils.success(nodeInfoVo);
  }

}
