package io.bizbee.onechat.connector.processor;

import io.bizbee.onechat.common.core.enums.IMCmdType;
import io.bizbee.onechat.common.core.utils.SpringContextHolder;

public class ProcessorFactory {

  public static MessageProcessor createProcessor(IMCmdType cmd) {
    MessageProcessor processor = null;
    switch (cmd) {
      case LOGIN:
        processor = SpringContextHolder.getBean(LoginProcessor.class);
        break;
      case HEART_BEAT:
        processor = SpringContextHolder.getBean(HeartbeatProcessor.class);
        break;
      default:
        break;
    }
    return processor;
  }

}
