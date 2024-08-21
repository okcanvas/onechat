package io.bizbee.onechat.server.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author okcanvas
 * @date 2024-08-21
 */
@Data
@Component
@ConfigurationProperties(prefix = AppConfigInfo.PRE)
public class AppConfigInfo {

  public final static String PRE = "app";

  private ZkNode zk;

  private Oauth2 auth2;

  @Data
  public static class Oauth2 {

    private Oauth2Node gitee;

    private Oauth2Node github;

    private String loginRedirectUri;

    @Data
    public static class Oauth2Node {

      private String clientId;

      private String clientSecret;

      private String redirectUri;
    }

  }

  @Data
  public static class ZkNode {

    private String path;

    private String address;

  }
}
