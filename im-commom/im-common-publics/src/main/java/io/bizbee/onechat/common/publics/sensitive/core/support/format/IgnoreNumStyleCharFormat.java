package io.bizbee.onechat.common.publics.sensitive.core.support.format;

import io.bizbee.onechat.common.publics.sensitive.common.annotation.ThreadSafe;
import io.bizbee.onechat.common.publics.sensitive.common.core.ICharFormat;
import io.bizbee.onechat.common.publics.sensitive.common.utils.NumUtils;

@ThreadSafe
public class IgnoreNumStyleCharFormat implements ICharFormat {

  @Override
  public char format(char original) {
    return NumUtils.getMappingChar(original);
  }

}
