package io.bizbee.onechat.common.publics.sensitive.core.support.check;

import io.bizbee.onechat.common.publics.sensitive.common.annotation.ThreadSafe;
import io.bizbee.onechat.common.publics.sensitive.common.core.ISensitiveCheck;
import io.bizbee.onechat.common.publics.sensitive.common.core.SensitiveCheckResult;
import io.bizbee.onechat.common.publics.sensitive.common.core.WordContext;
import io.bizbee.onechat.common.publics.sensitive.common.enums.ValidModeEnum;
import io.bizbee.onechat.common.publics.sensitive.common.utils.CharUtil;
import io.bizbee.onechat.common.publics.sensitive.common.utils.RegexUtil;

@ThreadSafe
public class SensitiveCheckUrl implements ISensitiveCheck {

  private static final int MAX_WEB_SITE_LEN = 70;

  @Override
  public SensitiveCheckResult sensitiveCheck(String txt, int beginIndex, ValidModeEnum validModeEnum, WordContext wordContext) {
    int lengthCount = 0;
    int actualLength = 0;

    StringBuilder stringBuilder = new StringBuilder();
    for (int i = beginIndex; i < txt.length(); i++) {
      char currentChar = txt.charAt(i);
      char mappingChar = wordContext.formatChar(currentChar);
      ;

      if (CharUtil.isWebSiteChar(mappingChar) && lengthCount <= MAX_WEB_SITE_LEN) {
        lengthCount++;
        stringBuilder.append(currentChar);

        if (isCondition(stringBuilder.toString())) {
          actualLength = lengthCount;

          if (ValidModeEnum.FAIL_FAST.equals(validModeEnum)) {
            break;
          }
        }
      } else {
        break;
      }
    }

    return SensitiveCheckResult.of(actualLength, SensitiveCheckUrl.class);
  }

  private boolean isCondition(final String string) {
    return RegexUtil.isWebSite(string);
  }

}
