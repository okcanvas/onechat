package io.bizbee.onechat.common.publics.sensitive.common.core;

import java.util.HashMap;
import java.util.Map;

public class NodeTree {

  private boolean isKeywordEnd;

  private Map<Character, NodeTree> subNodes;

  public boolean isKeywordEnd() {
    return Boolean.TRUE.equals(isKeywordEnd);
  }

  public void setKeywordEnd(boolean keywordEnd) {
    isKeywordEnd = keywordEnd;
  }

  public void addSubNode(Character c, NodeTree node) {
    if (subNodes == null) {
      subNodes = new HashMap<>(8);
    }
    subNodes.put(c, node);
  }

  public NodeTree getSubNode(Character c) {
    if (subNodes == null) {
      return null;
    }
    return subNodes.get(c);
  }

}
