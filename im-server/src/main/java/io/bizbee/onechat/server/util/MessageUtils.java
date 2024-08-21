package io.bizbee.onechat.server.util;

import io.bizbee.onechat.common.core.enums.MessageType;

public class MessageUtils {

  public static String converMessageContent(Integer type, String resourceContent) {
    if (type == null) {
      return resourceContent;
    }
    /**
     * if(msgInfo.type == 1){ chat.lastContent = "[##]"; }else if(msgInfo.type ==
     * 2){ chat.lastContent = "[##]"; }else if(msgInfo.type == 3){ chat.lastContent
     * = "[##]"; }else{ chat.lastContent = msgInfo.content; }
     */
    MessageType messageType = MessageType.findByCode(type);
    if (messageType == null) {
      return resourceContent;
    }
    switch (messageType) {
      case TEXT:
        return resourceContent;
      default:
        return "[" + messageType.description() + "]";
    }
  }

}
