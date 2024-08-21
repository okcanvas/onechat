package io.bizbee.onechat.common.publics.sensitive.core.support.result;

import io.bizbee.onechat.common.publics.sensitive.common.instance.Instances;
import io.bizbee.onechat.common.publics.sensitive.core.api.IWordResult;
import io.bizbee.onechat.common.publics.sensitive.core.api.IWordResultHandler;

public final class WordResultHandlers {

  private WordResultHandlers() {
  }

  public static IWordResultHandler<IWordResult> raw() {
    return Instances.singleton(WordResultHandlerRaw.class);
  }

  public static IWordResultHandler<String> word() {
    return Instances.singleton(WordResultHandlerWord.class);
  }

}
