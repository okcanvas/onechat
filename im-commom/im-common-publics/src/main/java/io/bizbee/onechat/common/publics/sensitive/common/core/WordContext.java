package io.bizbee.onechat.common.publics.sensitive.common.core;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class WordContext {

  private NodeTree rootNode;

  private boolean ignoreRepeat;

  private int sensitiveCheckNumLen = -1;

  private List<ICharFormat> charFormats;

  private List<ISensitiveCheck> sensitiveChecks;

  public void addCharFormat(ICharFormat charFormat) {
    if (this.charFormats == null) {
      this.charFormats = new ArrayList<>();
    }
    this.charFormats.add(charFormat);
  }

  public void addSensitiveChecks(ISensitiveCheck sensitiveCheck) {
    if (this.sensitiveChecks == null) {
      this.sensitiveChecks = new ArrayList<>();
    }
    this.sensitiveChecks.add(sensitiveCheck);
  }

  public char formatChar(char origin) {
    if (this.charFormats == null || this.charFormats.size() < 1) {
      return origin;
    }
    for (ICharFormat iCharFormat : charFormats) {
      origin = iCharFormat.format(origin);
    }
    return origin;
  }

}
