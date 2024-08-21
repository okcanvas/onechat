package io.bizbee.onechat.common.publics.sensitive.core.support.replace;

import io.bizbee.onechat.common.publics.sensitive.common.annotation.ThreadSafe;
import io.bizbee.onechat.common.publics.sensitive.common.utils.CharUtil;
import io.bizbee.onechat.common.publics.sensitive.core.api.ISensitiveWordReplace;
import io.bizbee.onechat.common.publics.sensitive.core.api.ISensitiveWordReplaceContext;

@ThreadSafe
public class SensitiveWordReplaceChar implements ISensitiveWordReplace {

  private final char replaceChar;

  public SensitiveWordReplaceChar(char replaceChar) {
    this.replaceChar = replaceChar;
  }

  @Override
  public String replace(ISensitiveWordReplaceContext context) {
    int wordLength = context.wordLength();

    return CharUtil.repeat(replaceChar, wordLength);
  }

}
