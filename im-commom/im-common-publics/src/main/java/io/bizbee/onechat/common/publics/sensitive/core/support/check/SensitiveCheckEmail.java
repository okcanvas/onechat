package io.bizbee.onechat.common.publics.sensitive.core.support.check;

import io.bizbee.onechat.common.publics.sensitive.common.annotation.ThreadSafe;
import io.bizbee.onechat.common.publics.sensitive.common.core.ISensitiveCheck;
import io.bizbee.onechat.common.publics.sensitive.common.core.SensitiveCheckResult;
import io.bizbee.onechat.common.publics.sensitive.common.core.WordContext;
import io.bizbee.onechat.common.publics.sensitive.common.enums.ValidModeEnum;
import io.bizbee.onechat.common.publics.sensitive.common.utils.CharUtil;
import io.bizbee.onechat.common.publics.sensitive.common.utils.RegexUtil;

@ThreadSafe
public class SensitiveCheckEmail implements ISensitiveCheck {

  @Override
  public SensitiveCheckResult sensitiveCheck(String txt, int beginIndex, ValidModeEnum validModeEnum, WordContext wordContext) {
    int lengthCount = 0;
    int actualLength = 0;

    StringBuilder stringBuilder = new StringBuilder();
    for (int i = beginIndex; i < txt.length(); i++) {
      char currentChar = txt.charAt(i);
      char mappingChar = wordContext.formatChar(currentChar);

      if (CharUtil.isEmilChar(mappingChar)) {
        lengthCount++;
        stringBuilder.append(currentChar);

        if (isCondition(stringBuilder.toString())) {
          actualLength = lengthCount;
        }
      } else {
        break;
      }
    }

    return SensitiveCheckResult.of(actualLength, SensitiveCheckEmail.class);
  }

  private boolean isCondition(final String string) {
    return RegexUtil.isEmail(string);
  }

}
