package io.bizbee.onechat.server;

import io.bizbee.onechat.common.publics.sensitive.core.config.SensitiveWordConfig;
import io.bizbee.onechat.common.publics.sensitive.core.main.SWDispatcherDefault;

public class SensitiveTest {

  public static void main(String[] args) {
    SensitiveWordConfig sensitiveWordConfig = SensitiveWordConfig.defaultConfig();
    SWDispatcherDefault swDispatcherDefault = SWDispatcherDefault.newInstance(sensitiveWordConfig);
    String replaced = swDispatcherDefault.replace("bizbee");
    System.out.println(replaced);
  }
}
