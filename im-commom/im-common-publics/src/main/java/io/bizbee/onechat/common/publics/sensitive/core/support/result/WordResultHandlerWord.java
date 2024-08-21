package io.bizbee.onechat.common.publics.sensitive.core.support.result;

import io.bizbee.onechat.common.publics.sensitive.common.annotation.ThreadSafe;
import io.bizbee.onechat.common.publics.sensitive.core.api.IWordResult;
import io.bizbee.onechat.common.publics.sensitive.core.api.IWordResultHandler;

@ThreadSafe
public class WordResultHandlerWord implements IWordResultHandler<String> {

  @Override
  public String handle(IWordResult wordResult) {
    if (wordResult == null) {
      return null;
    }
    return wordResult.word();
  }

}
