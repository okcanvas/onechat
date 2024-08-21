package io.bizbee.onechat.common.publics.sensitive.core.support.deny;

import io.bizbee.onechat.common.publics.sensitive.common.annotation.ThreadSafe;
import io.bizbee.onechat.common.publics.sensitive.common.utils.FileUtils;
import io.bizbee.onechat.common.publics.sensitive.core.api.IWordDeny;

import java.util.List;

@ThreadSafe
public class SystemDefaultWordDeny implements IWordDeny {

  @Override
  public List<String> deny() {
    return FileUtils.readAllLinesForZip(
        SystemDefaultWordDeny.class.getResourceAsStream("/deny.zip"));
  }

}
