package io.bizbee.onechat.common.publics.sensitive.common.core;

import io.bizbee.onechat.common.publics.sensitive.common.enums.ValidModeEnum;

public interface ISensitiveCheck {

  SensitiveCheckResult sensitiveCheck(final String txt, final int beginIndex,
      final ValidModeEnum validModeEnum,
      WordContext wordContext);

}
