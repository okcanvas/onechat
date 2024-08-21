package io.bizbee.onechat.common.publics.sensitive.core.support.allow;

import io.bizbee.onechat.common.publics.sensitive.common.annotation.ThreadSafe;
import io.bizbee.onechat.common.publics.sensitive.common.utils.FileUtils;
import io.bizbee.onechat.common.publics.sensitive.core.api.IWordAllow;

import java.util.List;

@ThreadSafe
public class SystemDefaultWordAllow implements IWordAllow {

  @Override
  public List<String> allow() {
    return FileUtils.readAllLinesForZip(
        SystemDefaultWordAllow.class.getResourceAsStream("/allow.zip"));
  }

}
