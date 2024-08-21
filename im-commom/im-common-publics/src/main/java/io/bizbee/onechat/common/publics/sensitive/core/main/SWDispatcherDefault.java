package io.bizbee.onechat.common.publics.sensitive.core.main;

import io.bizbee.onechat.common.publics.sensitive.common.core.ICharFormat;
import io.bizbee.onechat.common.publics.sensitive.common.handler.IHandler;
import io.bizbee.onechat.common.publics.sensitive.common.utils.ArgUtil;
import io.bizbee.onechat.common.publics.sensitive.common.utils.CollectionUtil;
import io.bizbee.onechat.common.publics.sensitive.common.utils.StringUtil;
import io.bizbee.onechat.common.publics.sensitive.core.api.*;
import io.bizbee.onechat.common.publics.sensitive.core.config.SensitiveWordConfig;
import io.bizbee.onechat.common.publics.sensitive.core.support.handler.SensitiveWordDefaultHandler;
import io.bizbee.onechat.common.publics.sensitive.core.support.replace.SensitiveWordReplaceChar;
import io.bizbee.onechat.common.publics.sensitive.core.support.result.WordResultHandlers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class SWDispatcherDefault implements SWDispatcher {

  private IWordHandler iWordHandler;

  private SensitiveWordConfig sensitiveWordConfig;

  private SWDispatcherDefault() {
  }

  private SWDispatcherDefault(SensitiveWordConfig config) {
    this.sensitiveWordConfig = config;
  }

  public static SWDispatcherDefault newInstance(SensitiveWordConfig config) {
    SWDispatcherDefault sensitiveWordDispatcher = new SWDispatcherDefault(config);
    sensitiveWordDispatcher.init();
    return sensitiveWordDispatcher;
  }

  public void init() {
    Set<String> denyList = new HashSet<>();
    for (IWordDeny wordDeny : sensitiveWordConfig.findWordDenys()) {
      List<String> deny = wordDeny.deny();
      denyList.addAll(deny);
    }

    Set<String> allows = new HashSet<>();
    for (IWordAllow wordAllow : sensitiveWordConfig.findWordAllows()) {
      List<String> deny = wordAllow.allow();
      allows.addAll(deny);
    }
    List<String> results = denyList.stream().filter(e -> !allows.contains(e))
        .collect(Collectors.toList());

    if (iWordHandler == null) {
      iWordHandler = new SensitiveWordDefaultHandler();
    }
    iWordHandler.initWord(formatWordList(results), sensitiveWordConfig);
  }

  private List<String> formatWordList(List<String> list) {
    if (CollectionUtil.isEmpty(list)) {
      return list;
    }
    List<ICharFormat> charFormats = sensitiveWordConfig.getCharFormats();
    if (charFormats == null || charFormats.size() < 1) {
      return list;
    }
    List<String> resultList = new ArrayList<>(list.size());
    for (String word : list) {
      if (StringUtil.isEmpty(word)) {
        continue;
      }
      StringBuilder stringBuilder = new StringBuilder();
      char[] chars = word.toCharArray();
      for (char c : chars) {
        char cf = sensitiveWordConfig.formatChar(c);
        stringBuilder.append(cf);
      }
      resultList.add(stringBuilder.toString());

    }

    return resultList;
  }

  @Override
  public boolean contains(final String target) {
    return iWordHandler.contains(target);
  }

  @Override
  public List<String> findAll(final String target) {
    return findAll(target, WordResultHandlers.word());
  }

  @Override
  public String findFirst(final String target) {
    return findFirst(target, WordResultHandlers.word());
  }

  @Override
  public <R> List<R> findAll(final String target, final IWordResultHandler<R> handler) {
    ArgUtil.notNull(handler, "handler");
    List<IWordResult> wordResults = iWordHandler.findAll(target);
    return CollectionUtil.toList(wordResults, new IHandler<IWordResult, R>() {

      @Override
      public R handle(IWordResult wordResult) {
        return handler.handle(wordResult);
      }
    });
  }

  @Override
  public <R> R findFirst(final String target, final IWordResultHandler<R> handler) {
    ArgUtil.notNull(handler, "handler");
    IWordResult wordResult = iWordHandler.findFirst(target);
    return handler.handle(wordResult);
  }

  @Override
  public String replace(final String target, final char replaceChar) {
    ISensitiveWordReplace replace = new SensitiveWordReplaceChar(replaceChar);

    return replace(target, replace);
  }

  @Override
  public String replace(final String target, final ISensitiveWordReplace replace) {
    return iWordHandler.replace(target, replace);
  }

  @Override
  public String replace(final String target) {
    return this.replace(target, '*');
  }

}
