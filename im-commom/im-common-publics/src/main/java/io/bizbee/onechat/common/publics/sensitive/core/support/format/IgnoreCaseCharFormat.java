package io.bizbee.onechat.common.publics.sensitive.core.support.format;

import io.bizbee.onechat.common.publics.sensitive.common.annotation.ThreadSafe;
import io.bizbee.onechat.common.publics.sensitive.common.core.ICharFormat;

@ThreadSafe
public class IgnoreCaseCharFormat implements ICharFormat {

  @Override
  public char format(char original) {
    return Character.toLowerCase(original);
  }

}
