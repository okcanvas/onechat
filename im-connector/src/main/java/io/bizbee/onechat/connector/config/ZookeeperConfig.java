package io.bizbee.onechat.connector.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class ZookeeperConfig {

  @Autowired
  private AppConfigProperties appConfigProperties;

  @Bean(destroyMethod = "close")
  public CuratorFramework curatorFramework() {
    CuratorFramework client = CuratorFrameworkFactory.newClient(
        appConfigProperties.getZk().getAddress(),
        new RetryNTimes(10, 5000));
    client.start();
    log.info("zookeeper connected!");
    return client;
  }

}
