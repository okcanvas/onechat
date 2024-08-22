package io.bizbee.onechat.admin.controller;

import io.bizbee.onechat.admin.dto.ServerInfoVo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/connector")
public class ConnectorNodeController {

  /**
   * @return
   */
  @RequestMapping("/info")
  public String info(Model model) {
    ServerInfoVo serverInfo = new ServerInfoVo();

    System.out.println(serverInfo);

    // List<ServerConnectionInfoItemVo> items = new ArrayList<>();
    // for(String l:AppConst.TCP_NODES){
    // ServerConnectionInfoItemVo vo = new ServerConnectionInfoItemVo();
    // vo.setProtocol(NetProtocolEnum.TCP.name());
    // vo.setNetAddress(l);
    // vo.setConnectorCount(-1L);
    // items.add(vo);
    // }
    // for(String l:AppConst.WS_NODES){
    // ServerConnectionInfoItemVo vo = new ServerConnectionInfoItemVo();
    // vo.setProtocol(NetProtocolEnum.WS.name());
    // vo.setNetAddress(l);
    // vo.setConnectorCount(-1L);
    // items.add(vo);
    // }
    // serverInfo.setServerCount(items.size());
    // serverInfo.setItems(items);
    model.addAttribute("datas", serverInfo);
    return "info";
  }

}
