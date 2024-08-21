package io.bizbee.onechat.common.publics.sensitive.core.support.check;

import io.bizbee.onechat.common.publics.sensitive.common.annotation.ThreadSafe;
import io.bizbee.onechat.common.publics.sensitive.common.core.ISensitiveCheck;
import io.bizbee.onechat.common.publics.sensitive.common.core.NodeTree;
import io.bizbee.onechat.common.publics.sensitive.common.core.SensitiveCheckResult;
import io.bizbee.onechat.common.publics.sensitive.common.core.WordContext;
import io.bizbee.onechat.common.publics.sensitive.common.enums.ValidModeEnum;

@ThreadSafe
public class SensitiveCheckWord implements ISensitiveCheck {

  @Override
  public SensitiveCheckResult sensitiveCheck(String txt, int beginIndex,
      ValidModeEnum validModeEnum,
      WordContext wordContext) {
    NodeTree nowNode = wordContext.getRootNode();

    int lengthCount = 0;
    int actualLength = 0;

    for (int i = beginIndex; i < txt.length(); i++) {
      nowNode = getNowNode(nowNode, wordContext, txt, i);

      if (null != nowNode) {
        lengthCount++;
        if (nowNode.isKeywordEnd()) {
          actualLength = lengthCount;

          if (ValidModeEnum.FAIL_FAST.equals(validModeEnum)) {
            break;
          }
        }
      } else {
        break;
      }
    }

    return SensitiveCheckResult.of(actualLength, SensitiveCheckWord.class);
  }

  /**
   * @param nodeTree 
   * @param context  
   * @param txt      
   * @param index    
   * @return map
   */
  private NodeTree getNowNode(NodeTree nodeTree, final WordContext context, final String txt, final int index) {
    char c = txt.charAt(index);
    char mappingChar = context.formatChar(c);
    ;

    NodeTree subNode = nodeTree.getSubNode(mappingChar);
    if (context.isIgnoreRepeat() && index > 0) {
      char preChar = txt.charAt(index - 1);
      char preMappingChar = context.formatChar(preChar);
      ;

      if (preMappingChar == mappingChar) {
        return nodeTree;
      }
    }

    return subNode;
  }

}
