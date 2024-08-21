package io.bizbee.onechat.common.publics.sensitive.core.support.replace;

import io.bizbee.onechat.common.publics.sensitive.core.api.ISensitiveWordReplaceContext;

public class SensitiveWordReplaceContext implements ISensitiveWordReplaceContext {

  public static SensitiveWordReplaceContext newInstance() {
    return new SensitiveWordReplaceContext();
  }

  private String sensitiveWord;

  private int wordLength;

  @Override
  public String sensitiveWord() {
    return sensitiveWord;
  }

  public SensitiveWordReplaceContext sensitiveWord(String sensitiveWord) {
    this.sensitiveWord = sensitiveWord;
    return this;
  }

  @Override
  public int wordLength() {
    return wordLength;
  }

  public SensitiveWordReplaceContext wordLength(int wordLength) {
    this.wordLength = wordLength;
    return this;
  }

  @Override
  public String toString() {
    return "SensitiveWordReplaceContext{" + "sensitiveWord='" + sensitiveWord + '\''
        + ", wordLength=" + wordLength
        + '}';
  }

}
