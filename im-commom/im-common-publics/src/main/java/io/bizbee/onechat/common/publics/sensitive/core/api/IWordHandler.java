package io.bizbee.onechat.common.publics.sensitive.core.api;

import io.bizbee.onechat.common.publics.sensitive.common.core.WordContext;
import io.bizbee.onechat.common.publics.sensitive.common.enums.ValidModeEnum;

import java.util.Collection;
import java.util.List;

public interface IWordHandler {

  void initWord(Collection<String> collection, WordContext wordContext);

  boolean contains(final String string);

  List<IWordResult> findAll(final String string);

  IWordResult findFirst(final String string);

  String replace(final String target, final ISensitiveWordReplace replace);

}
