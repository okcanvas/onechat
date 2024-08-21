package io.bizbee.onechat.sdk.listener;

import io.bizbee.onechat.common.core.model.SendResult;

public interface MessageListener {

  void process(SendResult result);

}
