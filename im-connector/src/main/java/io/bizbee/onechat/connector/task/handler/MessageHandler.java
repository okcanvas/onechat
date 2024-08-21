package io.bizbee.onechat.connector.task.handler;

public interface MessageHandler<T> {

  public void handler(T data);

}
