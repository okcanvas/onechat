package io.bizbee.onechat.common.core.contant;

public class RedisKey {

  // im-server maximum id, incremented from 0
  public final static String IM_MAX_SERVER_ID = "im:max_server_id";
  // The ID of the IM-server to which the user ID is connected.
  public final static String IM_USER_SERVER_ID = "im:user:server_id:";
  // unread private chat message queue
  public final static String IM_UNREAD_PRIVATE_QUEUE = "im:unread:private:";
  // unread group chat message queue
  public final static String IM_UNREAD_GROUP_QUEUE = "im:unread:group:";
  // Private chat message sending result queue
  public final static String IM_RESULT_PRIVATE_QUEUE = "im:result:private";
  // Group chat message sending result queue
  public final static String IM_RESULT_GROUP_QUEUE = "im:result:group";

}
