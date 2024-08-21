package io.bizbee.onechat.common.publics.sensitive.core.support.handler;

import io.bizbee.onechat.common.publics.sensitive.common.annotation.ThreadSafe;
import io.bizbee.onechat.common.publics.sensitive.common.core.ISensitiveCheck;
import io.bizbee.onechat.common.publics.sensitive.common.core.NodeTree;
import io.bizbee.onechat.common.publics.sensitive.common.core.SensitiveCheckResult;
import io.bizbee.onechat.common.publics.sensitive.common.core.WordContext;
import io.bizbee.onechat.common.publics.sensitive.common.enums.ValidModeEnum;
import io.bizbee.onechat.common.publics.sensitive.common.utils.CollectionUtil;
import io.bizbee.onechat.common.publics.sensitive.common.utils.FileUtils;
import io.bizbee.onechat.common.publics.sensitive.common.utils.StringUtil;
import io.bizbee.onechat.common.publics.sensitive.core.api.ISensitiveWordReplace;
import io.bizbee.onechat.common.publics.sensitive.core.api.ISensitiveWordReplaceContext;
import io.bizbee.onechat.common.publics.sensitive.core.api.IWordHandler;
import io.bizbee.onechat.common.publics.sensitive.core.api.IWordResult;
import io.bizbee.onechat.common.publics.sensitive.core.support.check.SensitiveCheckUrl;
import io.bizbee.onechat.common.publics.sensitive.core.support.replace.SensitiveWordReplaceContext;
import io.bizbee.onechat.common.publics.sensitive.core.support.result.WordResult;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
@ThreadSafe
public class SensitiveWordDefaultHandler implements IWordHandler {

  private WordContext wordContext;

  @Override
  public synchronized void initWord(Collection<String> collection, WordContext wordContext) {
    this.wordContext = wordContext;
    long startTime = System.currentTimeMillis();
    NodeTree rootNode = new NodeTree();
    for (String key : collection) {
      if (StringUtil.isEmpty(key)) {
        continue;
      }
      NodeTree tempNode = rootNode;
      for (int i = 0; i < key.length(); i++) {
        char c = key.charAt(i);
        NodeTree subNode = tempNode.getSubNode(c);
        if (subNode == null) {
          subNode = new NodeTree();
          tempNode.addSubNode(c, subNode);
        }
        tempNode = subNode;
        if (i == key.length() - 1) {
          tempNode.setKeywordEnd(true);
        }
      }
    }
    wordContext.setRootNode(rootNode);
    log.info("initialization completed, {} words, {}/s", collection.size(),
        (System.currentTimeMillis() - startTime) / 1000.0);
  }

  /**
   * Whether to include (1) directly iterate over all (2) If encountered, return true directly
   * @param string 
   * @return 
   */
  @Override
  public boolean contains(String string) {
    if (StringUtil.isEmpty(string)) {
      return false;
    }

    for (int i = 0; i < string.length(); i++) {
      SensitiveCheckResult checkResult = sensitiveCheck(string, i, ValidModeEnum.FAIL_FAST,
          wordContext);
      if (checkResult.index() > 0) {
        return true;
      }
    }
    return false;
  }

  /**
   * Returns all corresponding sensitive words (1) the result is ordered (2) in order to retain all subscripts, the result is from v0.1. No more weight after 0.
   * @param string 
   * @return 
   */
  @Override
  public List<IWordResult> findAll(String string) {
    return getSensitiveWords(string, ValidModeEnum.FAIL_OVER);
  }

  @Override
  public IWordResult findFirst(String string) {
    List<IWordResult> stringList = getSensitiveWords(string, ValidModeEnum.FAIL_FAST);

    if (CollectionUtil.isEmpty(stringList)) {
      return null;
    }

    return stringList.get(0);
  }

  @Override
  public String replace(String target, final ISensitiveWordReplace replace) {
    if (StringUtil.isEmpty(target)) {
      return target;
    }

    return this.replaceSensitiveWord(target, replace);
  }

  /**
   * @param text     
   * @param modeEnum 
   * @return 
   */
  private List<IWordResult> getSensitiveWords(final String text, final ValidModeEnum modeEnum) {
    if (StringUtil.isEmpty(text)) {
      return new ArrayList<>();
    }

    List<IWordResult> resultList = new ArrayList<>();
    for (int i = 0; i < text.length(); i++) {
      SensitiveCheckResult checkResult = sensitiveCheck(text, i, ValidModeEnum.FAIL_OVER,
          wordContext);
      int wordLength = checkResult.index();
      if (wordLength > 0) {
        String sensitiveWord = text.substring(i, i + wordLength);

        WordResult wordResult = WordResult.newInstance()
            .startIndex(i)
            .endIndex(i + wordLength)
            .word(sensitiveWord);
        resultList.add(wordResult);

        if (ValidModeEnum.FAIL_FAST.equals(modeEnum)) {
          break;
        }

        i += wordLength - 1;
      }
    }

    return resultList;
  }

  /**
   * @param target  
   * @param replace 
   * @return 
   */
  private String replaceSensitiveWord(final String target, final ISensitiveWordReplace replace) {
    if (StringUtil.isEmpty(target)) {
      return target;
    }
    StringBuilder resultBuilder = new StringBuilder(target.length());

    for (int i = 0; i < target.length(); i++) {
      char currentChar = target.charAt(i);
      SensitiveCheckResult checkResult = sensitiveCheck(target, i, ValidModeEnum.FAIL_OVER, wordContext);

      int wordLength = checkResult.index();
      if (wordLength > 0) {
        Class checkClass = checkResult.checkClass();
        String string = target.substring(i, i + wordLength);
        if (SensitiveCheckUrl.class.equals(checkClass) && FileUtils.isImage(string)) {
          resultBuilder.append(string);
        } else {
          ISensitiveWordReplaceContext replaceContext = SensitiveWordReplaceContext.newInstance()
              .sensitiveWord(string)
              .wordLength(wordLength);
          String replaceStr = replace.replace(replaceContext);

          resultBuilder.append(replaceStr);
        }

        i += wordLength - 1;
      } else {
        resultBuilder.append(currentChar);
      }
    }

    return resultBuilder.toString();
  }

  public SensitiveCheckResult sensitiveCheck(String txt, int beginIndex,
      ValidModeEnum validModeEnum,
      WordContext wordContext) {

    for (ISensitiveCheck sensitiveCheck : wordContext.getSensitiveChecks()) {
      SensitiveCheckResult result = sensitiveCheck.sensitiveCheck(txt, beginIndex, validModeEnum,
          wordContext);
      if (result.index() > 0) {
        return result;
      }
    }
    return SensitiveCheckResult.of(0, null);

  }

}
