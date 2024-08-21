package io.bizbee.onechat.common.publics.sensitive.core.main;

import io.bizbee.onechat.common.publics.sensitive.core.api.ISensitiveWordReplace;
import io.bizbee.onechat.common.publics.sensitive.core.api.IWordResultHandler;

import java.util.List;

public interface SWDispatcher {

  boolean contains(final String target);

  List<String> findAll(final String target);

  String findFirst(final String target);

  <R> List<R> findAll(final String target, final IWordResultHandler<R> handler);

  <R> R findFirst(final String target, final IWordResultHandler<R> handler);

  String replace(final String target, final char replaceChar);

  String replace(final String target, final ISensitiveWordReplace replace);

  String replace(final String target);

}
