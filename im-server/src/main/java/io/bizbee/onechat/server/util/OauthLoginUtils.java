package io.bizbee.onechat.server.util;

import com.alibaba.fastjson.JSONObject;
import io.bizbee.onechat.server.common.enums.UserEnum;
import io.bizbee.onechat.server.config.properties.AppConfigInfo;
import io.bizbee.onechat.server.exception.BusinessException;
import me.zhyd.oauth.AuthRequestBuilder;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.request.AuthGithubRequest;
import me.zhyd.oauth.request.AuthRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OauthLoginUtils {

  @Autowired
  private AppConfigInfo appConfigInfo;

  public static String parseBio(JSONObject rawUserInfo) {
    try {
      String b1 = rawUserInfo.getString("bio");
      return b1;
    } catch (Exception e) {

    }
    return null;
  }

  public AuthRequest buildAuthRequest(String type) {
    UserEnum.RegisterRromEnum registerRromEnum = UserEnum.RegisterRromEnum.findByMsg(type);
    if (registerRromEnum == null) {
      throw new BusinessException("Unknown login method");
    }

    switch (registerRromEnum) {
      case GITEE:
        AppConfigInfo.Oauth2.Oauth2Node gitee = appConfigInfo.getAuth2().getGitee();
        return AuthRequestBuilder.builder().source("gitee").authConfig((source) -> {
          return AuthConfig.builder()
              .clientId(gitee.getClientId())
              .clientSecret(gitee.getClientSecret())
              .redirectUri(gitee.getRedirectUri())
              .build();
        }).build();
      case GITHUB:
        AppConfigInfo.Oauth2.Oauth2Node github = appConfigInfo.getAuth2().getGithub();
        AuthRequest authRequest = new AuthGithubRequest(AuthConfig.builder()
            .clientId(github.getClientId())
            .clientSecret(github.getClientSecret())
            .redirectUri(github.getRedirectUri())
            .build());
        return authRequest;
    }
    throw new BusinessException("No support for this method");
  }
}
