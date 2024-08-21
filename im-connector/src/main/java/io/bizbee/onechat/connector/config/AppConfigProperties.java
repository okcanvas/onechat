package io.bizbee.onechat.connector.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = AppConfigProperties.PRE)
public class AppConfigProperties {

  public final static String PRE = "app";

  private String ip;

  private TcpNode ws;

  private TcpNode tcp;

  private ZkNode zk;

  @Data
  public static class TcpNode {

    private Boolean enable;

    private Integer port;
  }

  @Data
  public static class ZkNode {

    private String path;

    private String address;
  }

}
