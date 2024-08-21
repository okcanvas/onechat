package io.bizbee.onechat.common.publics.sensitive.core.support.format;

import io.bizbee.onechat.common.publics.sensitive.common.annotation.ThreadSafe;
import io.bizbee.onechat.common.publics.sensitive.common.core.ICharFormat;
import io.bizbee.onechat.common.publics.sensitive.common.utils.CharUtils;

@ThreadSafe
public class IgnoreEnglishStyleFormat implements ICharFormat {

  @Override
  public char format(char original) {
    return CharUtils.getMappingChar(original);
  }

}
