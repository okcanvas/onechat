package io.bizbee.onechat.connector.listener;

import cn.hutool.core.util.StrUtil;
import io.bizbee.onechat.common.core.enums.NetProtocolEnum;
import io.bizbee.onechat.common.core.utils.MixUtils;
import io.bizbee.onechat.connector.config.AppConfigProperties;
import io.bizbee.onechat.connector.listener.event.NodeRegisterEvent;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.nodes.PersistentEphemeralNode;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class NodeRegisterListener {

  @Autowired
  private AppConfigProperties appConfigProperties;

  @Autowired
  private CuratorFramework client;

  @SneakyThrows
  @EventListener(classes = NodeRegisterEvent.class)
  public void onApplicationEvent(NodeRegisterEvent event) {

    NetProtocolEnum protocolEnum = event.getNetProtocolEnum();
    Integer port = event.getPort();

    String protocolPath = appConfigProperties.getZk().getPath() + "/" + protocolEnum;
    checkZkPath(protocolPath);

    String nodePath = protocolPath + "/" + buildNodeInfo(port);
    delPath(nodePath);
    client.create().withMode(CreateMode.EPHEMERAL)
        .forPath(nodePath, MixUtils.LongToBytes(event.getRegisterTime()));
    log.info("nodePath:{}", nodePath);

    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      try {
        client.delete().forPath(nodePath);
        log.info("{}", nodePath);
      } catch (Exception e) {
        log.info("error", e);
        throw new RuntimeException(e);
      }
    }));

  }

  private void delPath(String nodePath) throws Exception {
    Stat stat = client.checkExists().forPath(nodePath);
    if (stat != null) {
      client.delete().forPath(nodePath);
    }
  }

  private void checkZkPath(String zkPath) throws Exception {
    String[] paths = zkPath.split("/");
    String parentPath = "";
    for (String p : paths) {
      if (StrUtil.isBlank(p)) {
        continue;
      }
      parentPath += "/" + p;
      if (client.checkExists().forPath(parentPath) == null) {
        client.create().forPath(parentPath);
      }
    }
  }

  private String buildNodeInfo(Integer port) {
    String ip = appConfigProperties.getIp();
    if (StrUtil.isBlank(ip)) {
      ip = MixUtils.getInet4Address();
    }
    return ip + ":" + port;
  }
}
