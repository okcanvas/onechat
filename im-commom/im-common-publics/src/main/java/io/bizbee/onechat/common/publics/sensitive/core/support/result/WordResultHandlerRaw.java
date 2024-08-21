package io.bizbee.onechat.common.publics.sensitive.core.support.result;

import io.bizbee.onechat.common.publics.sensitive.common.annotation.ThreadSafe;
import io.bizbee.onechat.common.publics.sensitive.core.api.IWordResult;
import io.bizbee.onechat.common.publics.sensitive.core.api.IWordResultHandler;

@ThreadSafe
public class WordResultHandlerRaw implements IWordResultHandler<IWordResult> {

  @Override
  public IWordResult handle(IWordResult wordResult) {
    return wordResult;
  }

}
