package io.bizbee.onechat.connector.remote;

public interface IMServer {

  default boolean enable() {
    return false;
  }

  void start();

  void stop();
}
