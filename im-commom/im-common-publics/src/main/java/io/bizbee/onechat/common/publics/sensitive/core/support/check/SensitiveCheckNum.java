package io.bizbee.onechat.common.publics.sensitive.core.support.check;

import io.bizbee.onechat.common.publics.sensitive.common.annotation.ThreadSafe;
import io.bizbee.onechat.common.publics.sensitive.common.core.ISensitiveCheck;
import io.bizbee.onechat.common.publics.sensitive.common.core.SensitiveCheckResult;
import io.bizbee.onechat.common.publics.sensitive.common.core.WordContext;
import io.bizbee.onechat.common.publics.sensitive.common.enums.ValidModeEnum;

@ThreadSafe
public class SensitiveCheckNum implements ISensitiveCheck {

  @Override
  public SensitiveCheckResult sensitiveCheck(String txt, int beginIndex, ValidModeEnum validModeEnum, WordContext wordContext) {
    int lengthCount = 0;
    int actualLength = 0;

    for (int i = beginIndex; i < txt.length(); i++) {
      char c = txt.charAt(i);
      char charKey = wordContext.formatChar(c);
      ;

      if (Character.isDigit(charKey)) {
        lengthCount++;

        if (lengthCount >= wordContext.getSensitiveCheckNumLen()) {
          actualLength = lengthCount;

          if (ValidModeEnum.FAIL_FAST.equals(validModeEnum)) {
            break;
          }
        }
      } else {
        break;
      }
    }

    return SensitiveCheckResult.of(actualLength, SensitiveCheckNum.class);
  }

}
